package com.pasteleria.contabilidad.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.contabilidad.infrastructure.persistence.entity.AsientoContableDetalleEntity;
import com.pasteleria.contabilidad.infrastructure.persistence.entity.AsientoContableEntity;
import com.pasteleria.contabilidad.infrastructure.persistence.entity.CuentaContableEntity;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.AsientoContableDetalleRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.AsientoContableRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.CuentaContableRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.TipoDiarioContableRepository;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContabilidadCommandService {

  private final CuentaContableRepository cuentaRepository;
  private final TipoDiarioContableRepository diarioRepository;
  private final AsientoContableRepository asientoRepository;
  private final AsientoContableDetalleRepository detalleRepository;
  private final ContabilidadMapper mapper;
  private final OperacionAutorizacionService autorizacionService;
  private final AuditTrailService auditTrailService;

  public ContabilidadCommandService(
      CuentaContableRepository cuentaRepository,
      TipoDiarioContableRepository diarioRepository,
      AsientoContableRepository asientoRepository,
      AsientoContableDetalleRepository detalleRepository,
      ContabilidadMapper mapper,
      OperacionAutorizacionService autorizacionService,
      AuditTrailService auditTrailService
  ) {
    this.cuentaRepository = cuentaRepository;
    this.diarioRepository = diarioRepository;
    this.asientoRepository = asientoRepository;
    this.detalleRepository = detalleRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public AsientoContableSummary registrarAsiento(RegistrarAsientoContableRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoGlobal(Permisos.ASIENTOS_REGISTRAR);
    LocalDateTime now = LocalDateTime.now();

    var diario = diarioRepository.findByCodigo(normalizarCodigo(request.tipoDiarioCodigo()))
        .orElseThrow(() -> new ResourceNotFoundException("Tipo de diario contable no encontrado."));
    if (!Boolean.TRUE.equals(diario.getActivo())) {
      throw new BusinessRuleException("El tipo de diario contable no esta activo.");
    }

    String codigo = codigoAsiento(request.codigo());
    if (asientoRepository.existsByCodigo(codigo)) {
      throw new BusinessRuleException("Ya existe un asiento contable con el codigo indicado.");
    }

    String origenTipo = blankToNull(request.origenTipo());
    String origenId = blankToNull(request.origenId());
    if ((origenTipo == null) != (origenId == null)) {
      throw new BusinessRuleException("El origen contable requiere tipo e identificador juntos.");
    }
    if (origenTipo != null && asientoRepository.existsByOrigenTipoAndOrigenId(origenTipo, origenId)) {
      throw new BusinessRuleException("Ya existe un asiento contable para el origen indicado.");
    }

    List<ErpFinancialPolicy.AccountingLine> lineasValidacion = request.lineas().stream()
        .map(linea -> new ErpFinancialPolicy.AccountingLine(linea.debe(), linea.haber()))
        .toList();
    ErpFinancialPolicy.validarCuadreContable(lineasValidacion);

    BigDecimal totalDebe = BigDecimal.ZERO.setScale(2);
    BigDecimal totalHaber = BigDecimal.ZERO.setScale(2);
    for (AsientoContableDetalleRequest linea : request.lineas()) {
      totalDebe = totalDebe.add(ErpFinancialPolicy.money(linea.debe()));
      totalHaber = totalHaber.add(ErpFinancialPolicy.money(linea.haber()));
    }
    totalDebe = ErpFinancialPolicy.money(totalDebe);
    totalHaber = ErpFinancialPolicy.money(totalHaber);

    AsientoContableEntity asiento = new AsientoContableEntity();
    asiento.setTipoDiario(diario);
    asiento.setCodigo(codigo);
    asiento.setFechaAsiento(request.fechaAsiento() == null ? now : request.fechaAsiento());
    asiento.setDescripcion(request.descripcion().trim());
    asiento.setOrigenTipo(origenTipo);
    asiento.setOrigenId(origenId);
    asiento.setEstado(EstadoAsientoContable.REGISTRADO.name());
    asiento.setTotalDebe(totalDebe);
    asiento.setTotalHaber(totalHaber);
    asiento.setCreatedAt(now);
    asiento.setUpdatedAt(now);
    AsientoContableEntity savedAsiento = asientoRepository.save(asiento);

    for (AsientoContableDetalleRequest linea : request.lineas()) {
      CuentaContableEntity cuenta = cuentaRepository.findByCodigo(normalizarCodigo(linea.cuentaCodigo()))
          .orElseThrow(() -> new ResourceNotFoundException("Cuenta contable no encontrada: " + linea.cuentaCodigo()));
      if (!Boolean.TRUE.equals(cuenta.getActiva())) {
        throw new BusinessRuleException("La cuenta contable no esta activa: " + cuenta.getCodigo());
      }
      ErpFinancialPolicy.exigirCuentaImputable(Boolean.TRUE.equals(cuenta.getImputable()), cuenta.getCodigo());

      AsientoContableDetalleEntity detalle = new AsientoContableDetalleEntity();
      detalle.setAsiento(savedAsiento);
      detalle.setCuenta(cuenta);
      detalle.setDescripcion(blankToNull(linea.descripcion()));
      detalle.setDebe(ErpFinancialPolicy.money(linea.debe()));
      detalle.setHaber(ErpFinancialPolicy.money(linea.haber()));
      detalle.setCreatedAt(now);
      detalleRepository.save(detalle);
    }

    AsientoContableSummary summary = mapper.toAsiento(savedAsiento, detalleRepository.findByAsientoIdOrderByIdAsc(savedAsiento.getId()));
    auditTrailService.recordChange(
        "CONTABILIDAD_ASIENTO_REGISTRADO",
        "CONTABILIDAD",
        "asiento_contable",
        savedAsiento.getId().toString(),
        "REGISTRAR_ASIENTO",
        null,
        summary,
        "Asiento contable registrado con partida doble validada.",
        httpRequest
    );
    return summary;
  }

  private String codigoAsiento(String codigo) {
    String limpio = blankToNull(codigo);
    if (limpio != null) {
      return limpio.toUpperCase();
    }
    return "ASI-" + System.currentTimeMillis();
  }

  private String normalizarCodigo(String codigo) {
    return codigo == null ? null : codigo.trim().toUpperCase();
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
