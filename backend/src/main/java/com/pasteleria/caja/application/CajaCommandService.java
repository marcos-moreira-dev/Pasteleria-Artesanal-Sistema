package com.pasteleria.caja.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import com.pasteleria.caja.infrastructure.persistence.entity.ArqueoCajaEntity;
import com.pasteleria.caja.infrastructure.persistence.entity.CajaOperativaEntity;
import com.pasteleria.caja.infrastructure.persistence.entity.MovimientoCajaEntity;
import com.pasteleria.caja.infrastructure.persistence.entity.TurnoCajaEntity;
import com.pasteleria.caja.infrastructure.persistence.repository.ArqueoCajaRepository;
import com.pasteleria.caja.infrastructure.persistence.repository.CajaOperativaRepository;
import com.pasteleria.caja.infrastructure.persistence.repository.MovimientoCajaRepository;
import com.pasteleria.caja.infrastructure.persistence.repository.TurnoCajaRepository;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CajaCommandService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final CajaOperativaRepository cajaRepository;
  private final TurnoCajaRepository turnoRepository;
  private final MovimientoCajaRepository movimientoRepository;
  private final ArqueoCajaRepository arqueoRepository;
  private final CajaPolicy cajaPolicy;
  private final CajaMapper mapper;
  private final OperacionAutorizacionService autorizacionService;
  private final AuthenticatedUserService authenticatedUserService;
  private final AuditTrailService auditTrailService;

  public CajaCommandService(
      CajaOperativaRepository cajaRepository,
      TurnoCajaRepository turnoRepository,
      MovimientoCajaRepository movimientoRepository,
      ArqueoCajaRepository arqueoRepository,
      CajaPolicy cajaPolicy,
      CajaMapper mapper,
      OperacionAutorizacionService autorizacionService,
      AuthenticatedUserService authenticatedUserService,
      AuditTrailService auditTrailService
  ) {
    this.cajaRepository = cajaRepository;
    this.turnoRepository = turnoRepository;
    this.movimientoRepository = movimientoRepository;
    this.arqueoRepository = arqueoRepository;
    this.cajaPolicy = cajaPolicy;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
    this.authenticatedUserService = authenticatedUserService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public TurnoCajaSummary abrir(AbrirCajaRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CAJA_OPERAR);
    String cajaCodigo = cajaPolicy.cajaCodigoOrDefault(request == null ? null : request.cajaCodigo());
    BigDecimal montoApertura = cajaPolicy.normalizeMoney(request == null ? null : request.montoApertura());
    cajaPolicy.exigirMontoNoNegativo(montoApertura, "El monto de apertura");

    CajaOperativaEntity caja = cajaRepository.findByCodigoAndActivaTrueForUpdate(cajaCodigo)
        .orElseThrow(() -> new ResourceNotFoundException("Caja operativa no encontrada: " + cajaCodigo));
    if (turnoRepository.existsByCajaIdAndEstado(caja.getId(), EstadoTurnoCaja.ABIERTO.name())) {
      throw new BusinessRuleException("La caja ya tiene un turno abierto.");
    }

    UserEntity actor = authenticatedUserService.currentUserOrThrow();
    TurnoCajaEntity turno = new TurnoCajaEntity();
    turno.setCaja(caja);
    turno.setUsuarioApertura(actor);
    turno.setMontoApertura(montoApertura);
    turno.setEstado(EstadoTurnoCaja.ABIERTO.name());
    turno.setFechaApertura(OffsetDateTime.now());
    turno.setObservacionesApertura(cajaPolicy.blankToNull(request == null ? null : request.observaciones()));
    TurnoCajaEntity savedTurno = turnoRepository.save(turno);

    MovimientoCajaEntity apertura = buildMovimiento(
        savedTurno,
        TipoMovimientoCaja.APERTURA,
        NaturalezaMovimientoCaja.ENTRADA,
        montoApertura,
        CajaPolicy.DEFAULT_REFERENCIA_TIPO,
        "TURNO-" + savedTurno.getId(),
        "Apertura de turno de caja.",
        actor
    );
    movimientoRepository.save(apertura);
    caja.setSaldoActual(montoApertura);
    cajaRepository.save(caja);

    TurnoCajaSummary summary = mapper.toTurno(savedTurno);
    auditTrailService.recordChange(
        "CAJA_TURNO_ABIERTO",
        "CAJA",
        "turno_caja",
        savedTurno.getId().toString(),
        "ABRIR_TURNO",
        null,
        summary,
        "Turno de caja abierto.",
        httpRequest
    );
    return summary;
  }

  @Transactional
  public MovimientoCajaSummary registrarMovimiento(RegistrarMovimientoCajaRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CAJA_OPERAR);
    TipoMovimientoCaja tipo = cajaPolicy.parseTipo(request.tipoMovimiento());
    NaturalezaMovimientoCaja naturaleza = cajaPolicy.parseNaturaleza(request.naturaleza());
    if (tipo == TipoMovimientoCaja.APERTURA) {
      throw new BusinessRuleException("La apertura de caja debe registrarse con el flujo de apertura, no como movimiento manual.");
    }
    BigDecimal monto = cajaPolicy.normalizeMoney(request.monto());
    cajaPolicy.exigirMontoPositivo(monto, "El monto del movimiento");

    TurnoCajaEntity turno = resolveTurnoForUpdate(request.turnoId(), request.cajaCodigo());
    cajaPolicy.exigirTurnoAbierto(turno.getEstado());
    CajaOperativaEntity caja = cajaRepository.findByCodigoAndActivaTrueForUpdate(turno.getCaja().getCodigo())
        .orElseThrow(() -> new ResourceNotFoundException("Caja operativa no encontrada."));

    BigDecimal nuevoSaldo = cajaPolicy.aplicarMovimiento(caja.getSaldoActual(), naturaleza, monto);
    UserEntity actor = authenticatedUserService.currentUserOrThrow();
    MovimientoCajaEntity movimiento = buildMovimiento(
        turno,
        tipo,
        naturaleza,
        monto,
        cajaPolicy.referenciaTipoOrDefault(request.referenciaTipo()),
        cajaPolicy.referenciaIdOrDefault(request.referenciaId()),
        cajaPolicy.blankToNull(request.descripcion()),
        actor
    );
    MovimientoCajaEntity saved = movimientoRepository.save(movimiento);
    caja.setSaldoActual(nuevoSaldo);
    cajaRepository.save(caja);

    MovimientoCajaSummary summary = mapper.toMovimiento(saved);
    auditTrailService.recordChange(
        "CAJA_MOVIMIENTO_REGISTRADO",
        "CAJA",
        "movimiento_caja",
        saved.getId().toString(),
        "REGISTRAR_MOVIMIENTO",
        null,
        summary,
        "Movimiento de caja registrado.",
        httpRequest
    );
    return summary;
  }

  @Transactional
  public TurnoCajaSummary cerrar(CerrarCajaRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CAJA_OPERAR);
    BigDecimal declarado = cajaPolicy.normalizeMoney(request.montoDeclarado());
    cajaPolicy.exigirMontoNoNegativo(declarado, "El monto declarado");

    TurnoCajaEntity turno = resolveTurnoForUpdate(request.turnoId(), request.cajaCodigo());
    cajaPolicy.exigirTurnoAbierto(turno.getEstado());
    CajaOperativaEntity caja = cajaRepository.findByCodigoAndActivaTrueForUpdate(turno.getCaja().getCodigo())
        .orElseThrow(() -> new ResourceNotFoundException("Caja operativa no encontrada."));

    BigDecimal sistema = calcularMontoSistema(turno.getId());
    BigDecimal diferencia = declarado.subtract(sistema);
    UserEntity actor = authenticatedUserService.currentUserOrThrow();

    ArqueoCajaEntity arqueo = new ArqueoCajaEntity();
    arqueo.setTurno(turno);
    arqueo.setMontoSistema(sistema);
    arqueo.setMontoDeclarado(declarado);
    arqueo.setDiferencia(diferencia);
    arqueo.setEstado(EstadoArqueoCaja.CONFIRMADO.name());
    arqueo.setObservaciones(cajaPolicy.blankToNull(request.observaciones()));
    arqueo.setCreadoPor(actor);
    arqueoRepository.save(arqueo);

    turno.setEstado(EstadoTurnoCaja.CERRADO.name());
    turno.setUsuarioCierre(actor);
    turno.setFechaCierre(OffsetDateTime.now());
    turno.setMontoCierreSistema(sistema);
    turno.setMontoCierreDeclarado(declarado);
    turno.setDiferenciaCierre(diferencia);
    turno.setObservacionesCierre(cajaPolicy.blankToNull(request.observaciones()));
    TurnoCajaEntity savedTurno = turnoRepository.save(turno);

    // En esta fase transicional la caja queda con el monto declarado. La diferencia
    // queda trazada en arqueo; los asientos y ajustes contables vendran en T17-T19.
    caja.setSaldoActual(declarado);
    cajaRepository.save(caja);

    TurnoCajaSummary summary = mapper.toTurno(savedTurno);
    auditTrailService.recordChange(
        "CAJA_TURNO_CERRADO",
        "CAJA",
        "turno_caja",
        savedTurno.getId().toString(),
        "CERRAR_TURNO",
        null,
        summary,
        "Turno de caja cerrado con arqueo confirmado.",
        httpRequest
    );
    return summary;
  }

  private BigDecimal calcularMontoSistema(Long turnoId) {
    BigDecimal expected = BigDecimal.ZERO;
    for (MovimientoCajaEntity movimiento : movimientoRepository.findByTurnoIdAndEstadoOrderByFechaMovimientoAsc(
        turnoId,
        EstadoMovimientoCaja.REGISTRADO.name()
    )) {
      expected = cajaPolicy.calcularEsperado(
          expected,
          NaturalezaMovimientoCaja.valueOf(movimiento.getNaturaleza()),
          movimiento.getMonto()
      );
    }
    return cajaPolicy.normalizeMoney(expected);
  }

  private TurnoCajaEntity resolveTurnoForUpdate(Long turnoId, String cajaCodigo) {
    if (turnoId != null) {
      return turnoRepository.findByIdForUpdate(turnoId)
          .orElseThrow(() -> new ResourceNotFoundException("Turno de caja no encontrado."));
    }
    String codigo = cajaPolicy.cajaCodigoOrDefault(cajaCodigo);
    return turnoRepository.findFirstByCajaCodigoAndEstadoForUpdate(
            codigo,
            EstadoTurnoCaja.ABIERTO.name()
        )
        .orElseThrow(() -> new ResourceNotFoundException("No hay un turno de caja abierto para " + codigo + "."));
  }

  private MovimientoCajaEntity buildMovimiento(
      TurnoCajaEntity turno,
      TipoMovimientoCaja tipo,
      NaturalezaMovimientoCaja naturaleza,
      BigDecimal monto,
      String referenciaTipo,
      String referenciaId,
      String descripcion,
      UserEntity actor
  ) {
    MovimientoCajaEntity movimiento = new MovimientoCajaEntity();
    movimiento.setTurno(turno);
    movimiento.setTipoMovimiento(tipo.name());
    movimiento.setNaturaleza(naturaleza.name());
    movimiento.setMonto(cajaPolicy.normalizeMoney(monto));
    movimiento.setMoneda(turno.getCaja().getMoneda());
    movimiento.setReferenciaTipo(referenciaTipo);
    movimiento.setReferenciaId(referenciaId);
    movimiento.setDescripcion(descripcion);
    movimiento.setEstado(EstadoMovimientoCaja.REGISTRADO.name());
    movimiento.setCreadoPor(actor);
    return movimiento;
  }
}
