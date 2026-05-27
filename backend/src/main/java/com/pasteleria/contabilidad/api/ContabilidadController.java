package com.pasteleria.contabilidad.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.contabilidad.application.AsientoContableSummary;
import com.pasteleria.contabilidad.application.ContabilidadCommandService;
import com.pasteleria.contabilidad.application.ContabilidadQueryService;
import com.pasteleria.contabilidad.application.CuentaContableSummary;
import com.pasteleria.contabilidad.application.RegistrarAsientoContableRequest;
import com.pasteleria.contabilidad.application.TipoDiarioSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Contabilidad", description = "Plan de cuentas, diarios y asientos contables internos.")
@RestController
@RequestMapping("/api/v1/contabilidad")
public class ContabilidadController {

  private final ContabilidadQueryService queryService;
  private final ContabilidadCommandService commandService;

  public ContabilidadController(ContabilidadQueryService queryService, ContabilidadCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar cuentas contables activas.")
  @GetMapping("/cuentas")
  public ResponseEntity<ApiResponse<List<CuentaContableSummary>>> cuentas(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Cuentas contables obtenidas correctamente.",
        queryService.cuentas(),
        request
    ));
  }

  @Operation(summary = "Listar tipos de diario contable.")
  @GetMapping("/diarios")
  public ResponseEntity<ApiResponse<List<TipoDiarioSummary>>> diarios(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Tipos de diario contable obtenidos correctamente.",
        queryService.diarios(),
        request
    ));
  }

  @Operation(summary = "Listar asientos contables recientes.")
  @GetMapping("/asientos")
  public ResponseEntity<ApiResponse<List<AsientoContableSummary>>> asientos(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Asientos contables obtenidos correctamente.",
        queryService.asientos(),
        request
    ));
  }

  @Operation(summary = "Consultar asiento contable.")
  @GetMapping("/asientos/{id}")
  public ResponseEntity<ApiResponse<AsientoContableSummary>> asiento(@PathVariable Long id, HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Asiento contable obtenido correctamente.",
        queryService.asiento(id),
        request
    ));
  }

  @Operation(summary = "Registrar asiento contable manual.")
  @PostMapping("/asientos")
  public ResponseEntity<ApiResponse<AsientoContableSummary>> registrarAsiento(
      @Valid @RequestBody RegistrarAsientoContableRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Asiento contable registrado correctamente.",
        commandService.registrarAsiento(body, request),
        request
    ));
  }
}
