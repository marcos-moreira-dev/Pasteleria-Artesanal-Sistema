package com.pasteleria.abastecimiento.api;

import com.pasteleria.abastecimiento.application.CompraFinancieraService;
import com.pasteleria.abastecimiento.application.CompraFinancieraSummary;
import com.pasteleria.abastecimiento.application.DocumentoPagarSummary;
import com.pasteleria.abastecimiento.application.RegistrarDocumentoCompraRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Compras financieras", description = "Documentos de compra y cuentas por pagar preparadas desde recepción de órdenes.")
@RequestMapping("/api/v1/abastecimiento")
public class CompraFinancieraController {

  private final CompraFinancieraService compraFinancieraService;

  public CompraFinancieraController(CompraFinancieraService compraFinancieraService) {
    this.compraFinancieraService = compraFinancieraService;
  }

  @Operation(summary = "Registrar documento de compra",
      description = "Crea el documento de compra y la cuenta por pagar inicial desde una orden recibida.")
  @PostMapping("/ordenes-compra/{id}/documento-compra")
  public ResponseEntity<ApiResponse<CompraFinancieraSummary>> registrarDocumentoCompra(
      @PathVariable Long id,
      @Valid @RequestBody RegistrarDocumentoCompraRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Documento de compra y cuenta por pagar registrados correctamente.",
        compraFinancieraService.registrarDocumentoCompra(id, body, request),
        request
    ));
  }

  @Operation(summary = "Consultar documento financiero de la orden",
      description = "Obtiene documento de compra y cuenta por pagar asociados a una orden de compra.")
  @GetMapping("/ordenes-compra/{id}/documento-compra")
  public ResponseEntity<ApiResponse<CompraFinancieraSummary>> findDocumentoCompra(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documento de compra obtenido correctamente.",
        compraFinancieraService.findByOrdenCompra(id),
        request
    ));
  }

  @Operation(summary = "Listar cuentas por pagar preparadas",
      description = "Lista cuentas por pagar generadas desde documentos de compra.")
  @GetMapping("/cuentas-pagar")
  public ResponseEntity<ApiResponse<List<DocumentoPagarSummary>>> findCuentasPagar(
      @RequestParam(required = false) String estado,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Cuentas por pagar obtenidas correctamente.",
        compraFinancieraService.findCuentasPagar(estado),
        request
    ));
  }
}
