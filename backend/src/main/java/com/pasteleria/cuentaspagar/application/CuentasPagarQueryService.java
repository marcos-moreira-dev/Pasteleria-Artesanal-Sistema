package com.pasteleria.cuentaspagar.application;

import java.util.List;

import com.pasteleria.abastecimiento.application.DocumentoPagarSummary;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.DocumentoPagarRepository;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;
import com.pasteleria.cuentaspagar.infrastructure.persistence.repository.PagoProveedorAplicacionRepository;
import com.pasteleria.cuentaspagar.infrastructure.persistence.repository.PagoProveedorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CuentasPagarQueryService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final DocumentoPagarRepository documentoRepository;
  private final PagoProveedorRepository pagoRepository;
  private final PagoProveedorAplicacionRepository aplicacionRepository;
  private final CuentasPagarMapper mapper;
  private final OperacionAutorizacionService autorizacionService;

  public CuentasPagarQueryService(
      DocumentoPagarRepository documentoRepository,
      PagoProveedorRepository pagoRepository,
      PagoProveedorAplicacionRepository aplicacionRepository,
      CuentasPagarMapper mapper,
      OperacionAutorizacionService autorizacionService
  ) {
    this.documentoRepository = documentoRepository;
    this.pagoRepository = pagoRepository;
    this.aplicacionRepository = aplicacionRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
  }

  @Transactional(readOnly = true)
  public List<DocumentoPagarSummary> documentos(String estado) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CUENTAS_PAGAR_VER);
    if (estado != null && !estado.isBlank()) {
      return documentoRepository.findByEstadoOrderByCreatedAtDesc(estado.trim().toUpperCase()).stream()
          .map(mapper::toDocumento)
          .toList();
    }
    return documentoRepository.findTop50ByOrderByCreatedAtDesc().stream().map(mapper::toDocumento).toList();
  }

  @Transactional(readOnly = true)
  public List<PagoProveedorSummary> pagos() {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CUENTAS_PAGAR_VER);
    return pagoRepository.findTop80ByOrderByFechaPagoDesc().stream()
        .map(pago -> mapper.toPago(pago, aplicacionRepository.findByPagoProveedorIdOrderByIdAsc(pago.getId())))
        .toList();
  }
}
