package com.pasteleria.cuentaspagar.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.DocumentoPagarSummary;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.cuentaspagar.application.CuentasPagarCommandService;
import com.pasteleria.cuentaspagar.application.CuentasPagarQueryService;
import com.pasteleria.cuentaspagar.application.PagoProveedorSummary;
import com.pasteleria.cuentaspagar.application.RegistrarPagoProveedorRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Cuentas por pagar", description = "Obligaciones con proveedores y pagos aplicados contra saldos.")
@RestController
@RequestMapping("/api/v1/cuentas-pagar")
public class CuentasPagarController {

  private final CuentasPagarQueryService queryService;
  private final CuentasPagarCommandService commandService;

  public CuentasPagarController(CuentasPagarQueryService queryService, CuentasPagarCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar documentos por pagar.")
  @GetMapping("/documentos")
  public ResponseEntity<ApiResponse<List<DocumentoPagarSummary>>> documentos(
      @RequestParam(required = false) String estado,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documentos por pagar obtenidos correctamente.",
        queryService.documentos(estado),
        request
    ));
  }

  @Operation(summary = "Listar pagos a proveedor.")
  @GetMapping("/pagos")
  public ResponseEntity<ApiResponse<List<PagoProveedorSummary>>> pagos(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pagos a proveedor obtenidos correctamente.",
        queryService.pagos(),
        request
    ));
  }

  @Operation(summary = "Registrar pago a proveedor aplicado contra documentos por pagar.")
  @PostMapping("/pagos")
  public ResponseEntity<ApiResponse<PagoProveedorSummary>> registrarPago(
      @Valid @RequestBody RegistrarPagoProveedorRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Pago a proveedor registrado correctamente.",
        commandService.registrarPago(body, request),
        request
    ));
  }
}
