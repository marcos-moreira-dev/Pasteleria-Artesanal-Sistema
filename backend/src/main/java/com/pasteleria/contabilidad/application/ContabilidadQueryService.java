package com.pasteleria.contabilidad.application;

import java.util.List;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.AsientoContableDetalleRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.AsientoContableRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.CuentaContableRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.TipoDiarioContableRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContabilidadQueryService {

  private final CuentaContableRepository cuentaRepository;
  private final TipoDiarioContableRepository diarioRepository;
  private final AsientoContableRepository asientoRepository;
  private final AsientoContableDetalleRepository detalleRepository;
  private final ContabilidadMapper mapper;
  private final OperacionAutorizacionService autorizacionService;

  public ContabilidadQueryService(
      CuentaContableRepository cuentaRepository,
      TipoDiarioContableRepository diarioRepository,
      AsientoContableRepository asientoRepository,
      AsientoContableDetalleRepository detalleRepository,
      ContabilidadMapper mapper,
      OperacionAutorizacionService autorizacionService
  ) {
    this.cuentaRepository = cuentaRepository;
    this.diarioRepository = diarioRepository;
    this.asientoRepository = asientoRepository;
    this.detalleRepository = detalleRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
  }

  @Transactional(readOnly = true)
  public List<CuentaContableSummary> cuentas() {
    autorizacionService.exigirPermisoGlobal(Permisos.CONTABILIDAD_VER);
    return cuentaRepository.findByActivaTrueOrderByCodigoAsc().stream().map(mapper::toCuenta).toList();
  }

  @Transactional(readOnly = true)
  public List<TipoDiarioSummary> diarios() {
    autorizacionService.exigirPermisoGlobal(Permisos.CONTABILIDAD_VER);
    return diarioRepository.findByActivoTrueOrderByCodigoAsc().stream().map(mapper::toDiario).toList();
  }

  @Transactional(readOnly = true)
  public List<AsientoContableSummary> asientos() {
    autorizacionService.exigirPermisoGlobal(Permisos.CONTABILIDAD_VER);
    return asientoRepository.findTop80ByOrderByFechaAsientoDescIdDesc().stream()
        .map(asiento -> mapper.toAsiento(asiento, detalleRepository.findByAsientoIdOrderByIdAsc(asiento.getId())))
        .toList();
  }

  @Transactional(readOnly = true)
  public AsientoContableSummary asiento(Long id) {
    autorizacionService.exigirPermisoGlobal(Permisos.CONTABILIDAD_VER);
    var asiento = asientoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Asiento contable no encontrado."));
    return mapper.toAsiento(asiento, detalleRepository.findByAsientoIdOrderByIdAsc(asiento.getId()));
  }
}
