package com.pasteleria.erpbridge.application;

import java.util.List;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.contabilidad.application.AsientoContableDetalleRequest;
import com.pasteleria.cuentaspagar.infrastructure.persistence.repository.PagoProveedorRepository;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Bridge de pagos a proveedor hacia contabilidad interna. */
@Service
public class CuentasPagarErpBridgeService {

  private final PagoProveedorRepository pagoProveedorRepository;
  private final ContabilidadBridgeService contabilidadBridgeService;

  public CuentasPagarErpBridgeService(PagoProveedorRepository pagoProveedorRepository, ContabilidadBridgeService contabilidadBridgeService) {
    this.pagoProveedorRepository = pagoProveedorRepository;
    this.contabilidadBridgeService = contabilidadBridgeService;
  }

  @Transactional
  public ErpBridgeOperationResult registrarAsientoPagoProveedor(Long pagoProveedorId, HttpServletRequest request) {
    var pago = pagoProveedorRepository.findById(pagoProveedorId)
        .orElseThrow(() -> new ResourceNotFoundException("Pago a proveedor no encontrado."));
    var total = ErpFinancialPolicy.money(pago.getMontoTotal());
    ErpFinancialPolicy.exigirMontoPositivo(total, "El pago a proveedor debe tener monto mayor a cero.");

    List<AsientoContableDetalleRequest> lineas = List.of(
        contabilidadBridgeService.debe("2.1.01", "Disminucion de cuenta por pagar - " + pago.getCodigo(), total),
        contabilidadBridgeService.haber("1.1.01", "Salida de caja por pago proveedor - " + pago.getCodigo(), total)
    );

    return contabilidadBridgeService.registrarAsientoSiNoExiste(
        "REGISTRAR_ASIENTO_PAGO_PROVEEDOR",
        "PAGO_PROVEEDOR",
        pago.getId().toString(),
        "CAJA",
        "ASI-PAG-" + pago.getCodigo(),
        pago.getFechaPago(),
        "Asiento interno de pago a proveedor generado desde pago " + pago.getCodigo() + ".",
        lineas,
        request
    );
  }
}
