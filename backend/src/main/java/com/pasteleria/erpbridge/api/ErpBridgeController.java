package com.pasteleria.erpbridge.api;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.erpbridge.application.CajaErpBridgeService;
import com.pasteleria.erpbridge.application.CompraErpBridgeService;
import com.pasteleria.erpbridge.application.CuentasPagarErpBridgeService;
import com.pasteleria.erpbridge.application.ErpBridgeOperationResult;
import com.pasteleria.erpbridge.application.VentaErpBridgeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "ERP Bridges", description = "Puentes idempotentes entre operaciones y consecuencias ERP internas.")
@RestController
@RequestMapping("/api/v1/erp-bridges")
public class ErpBridgeController {

  private final VentaErpBridgeService ventaBridge;
  private final CajaErpBridgeService cajaBridge;
  private final CompraErpBridgeService compraBridge;
  private final CuentasPagarErpBridgeService cuentasPagarBridge;

  public ErpBridgeController(
      VentaErpBridgeService ventaBridge,
      CajaErpBridgeService cajaBridge,
      CompraErpBridgeService compraBridge,
      CuentasPagarErpBridgeService cuentasPagarBridge
  ) {
    this.ventaBridge = ventaBridge;
    this.cajaBridge = cajaBridge;
    this.compraBridge = compraBridge;
    this.cuentasPagarBridge = cuentasPagarBridge;
  }

  @Operation(summary = "Generar documento por cobrar desde pedido.")
  @PostMapping("/pedidos/{pedidoId}/documento-cobrar")
  public ResponseEntity<ApiResponse<ErpBridgeOperationResult>> generarDocumentoCobrar(
      @PathVariable Long pedidoId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Bridge de pedido a documento por cobrar ejecutado.",
        ventaBridge.generarDocumentoCobrarDesdePedido(pedidoId, request),
        request
    ));
  }

  @Operation(summary = "Registrar asiento de venta desde documento por cobrar.")
  @PostMapping("/documentos-cobrar/{documentoCobrarId}/asiento-venta")
  public ResponseEntity<ApiResponse<ErpBridgeOperationResult>> asientoVenta(
      @PathVariable Long documentoCobrarId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Bridge de documento por cobrar a asiento de venta ejecutado.",
        ventaBridge.registrarAsientoVentaDesdeDocumentoCobrar(documentoCobrarId, request),
        request
    ));
  }

  @Operation(summary = "Registrar asiento de cobro desde cobranza.")
  @PostMapping("/cobranzas/{cobranzaId}/asiento-cobro")
  public ResponseEntity<ApiResponse<ErpBridgeOperationResult>> asientoCobro(
      @PathVariable Long cobranzaId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Bridge de cobranza a asiento de cobro ejecutado.",
        cajaBridge.registrarAsientoCobroDesdeCobranza(cobranzaId, request),
        request
    ));
  }

  @Operation(summary = "Registrar asiento de compra desde documento por pagar.")
  @PostMapping("/documentos-pagar/{documentoPagarId}/asiento-compra")
  public ResponseEntity<ApiResponse<ErpBridgeOperationResult>> asientoCompra(
      @PathVariable Long documentoPagarId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Bridge de documento por pagar a asiento de compra ejecutado.",
        compraBridge.registrarAsientoCompraDesdeDocumentoPagar(documentoPagarId, request),
        request
    ));
  }

  @Operation(summary = "Registrar asiento de pago a proveedor.")
  @PostMapping("/pagos-proveedor/{pagoProveedorId}/asiento-pago")
  public ResponseEntity<ApiResponse<ErpBridgeOperationResult>> asientoPagoProveedor(
      @PathVariable Long pagoProveedorId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Bridge de pago proveedor a asiento de pago ejecutado.",
        cuentasPagarBridge.registrarAsientoPagoProveedor(pagoProveedorId, request),
        request
    ));
  }
}
