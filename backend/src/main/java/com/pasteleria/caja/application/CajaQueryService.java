package com.pasteleria.caja.application;

import java.util.List;

import com.pasteleria.caja.infrastructure.persistence.entity.CajaOperativaEntity;
import com.pasteleria.caja.infrastructure.persistence.entity.TurnoCajaEntity;
import com.pasteleria.caja.infrastructure.persistence.repository.CajaOperativaRepository;
import com.pasteleria.caja.infrastructure.persistence.repository.MovimientoCajaRepository;
import com.pasteleria.caja.infrastructure.persistence.repository.TurnoCajaRepository;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CajaQueryService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final CajaOperativaRepository cajaRepository;
  private final TurnoCajaRepository turnoRepository;
  private final MovimientoCajaRepository movimientoRepository;
  private final CajaPolicy cajaPolicy;
  private final CajaMapper mapper;
  private final OperacionAutorizacionService autorizacionService;

  public CajaQueryService(
      CajaOperativaRepository cajaRepository,
      TurnoCajaRepository turnoRepository,
      MovimientoCajaRepository movimientoRepository,
      CajaPolicy cajaPolicy,
      CajaMapper mapper,
      OperacionAutorizacionService autorizacionService
  ) {
    this.cajaRepository = cajaRepository;
    this.turnoRepository = turnoRepository;
    this.movimientoRepository = movimientoRepository;
    this.cajaPolicy = cajaPolicy;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
  }

  public CajaEstadoSummary estado(String cajaCodigo) {
    exigirConsultaCaja();
    CajaOperativaEntity caja = findCaja(cajaCodigo);
    TurnoCajaEntity turno = turnoRepository
        .findFirstByCajaCodigoAndEstadoOrderByFechaAperturaDesc(caja.getCodigo(), EstadoTurnoCaja.ABIERTO.name())
        .orElse(null);
    return mapper.toEstado(caja, turno);
  }

  public TurnoCajaSummary turnoAbierto(String cajaCodigo) {
    exigirConsultaCaja();
    String codigo = cajaPolicy.cajaCodigoOrDefault(cajaCodigo);
    return turnoRepository.findFirstByCajaCodigoAndEstadoOrderByFechaAperturaDesc(codigo, EstadoTurnoCaja.ABIERTO.name())
        .map(mapper::toTurno)
        .orElseThrow(() -> new ResourceNotFoundException("No hay un turno de caja abierto para " + codigo + "."));
  }

  public List<MovimientoCajaSummary> movimientos(Long turnoId) {
    exigirConsultaCaja();
    if (!turnoRepository.existsById(turnoId)) {
      throw new ResourceNotFoundException("Turno de caja no encontrado.");
    }
    return movimientoRepository.findByTurnoIdOrderByFechaMovimientoDesc(turnoId).stream()
        .map(mapper::toMovimiento)
        .toList();
  }

  private CajaOperativaEntity findCaja(String cajaCodigo) {
    String codigo = cajaPolicy.cajaCodigoOrDefault(cajaCodigo);
    return cajaRepository.findByCodigoAndActivaTrue(codigo)
        .orElseThrow(() -> new ResourceNotFoundException("Caja operativa no encontrada: " + codigo));
  }

  private void exigirConsultaCaja() {
    autorizacionService.exigirAlgunoPermisoSucursal(
        SUCURSAL_TRANSICIONAL,
        List.of(Permisos.CAJA_VER, Permisos.CAJA_OPERAR)
    );
  }
}
