package com.pasteleria.inteligencia.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.inteligencia.application.CajaMovimientoSemanticRow;
import com.pasteleria.inteligencia.application.CarteraSemanticRow;
import com.pasteleria.inteligencia.application.ContabilidadSemanticRow;
import com.pasteleria.inteligencia.application.CuentasPagarSemanticRow;
import com.pasteleria.inteligencia.application.DashboardErpSummary;
import com.pasteleria.inteligencia.application.FiscalSemanticRow;
import com.pasteleria.inteligencia.application.InteligenciaQueryService;
import com.pasteleria.inteligencia.application.StockBajoSemanticRow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Inteligencia", description = "Consultas semanticas de solo lectura para tableros y reportes ERP.")
@RestController
@RequestMapping("/api/v1/inteligencia")
public class InteligenciaController {

  private final InteligenciaQueryService queryService;

  public InteligenciaController(InteligenciaQueryService queryService) {
    this.queryService = queryService;
  }

  @Operation(summary = "Obtener resumen ERP del tablero administrativo.")
  @GetMapping("/dashboard")
  public ResponseEntity<ApiResponse<DashboardErpSummary>> dashboard(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Resumen ERP obtenido correctamente.",
        queryService.dashboard(),
        request
    ));
  }

  @Operation(summary = "Consultar vista semantica de cartera.")
  @GetMapping("/cartera")
  public ResponseEntity<ApiResponse<List<CarteraSemanticRow>>> cartera(
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Vista semantica de cartera obtenida correctamente.",
        queryService.cartera(limit),
        request
    ));
  }

  @Operation(summary = "Consultar vista semantica de cuentas por pagar.")
  @GetMapping("/cuentas-pagar")
  public ResponseEntity<ApiResponse<List<CuentasPagarSemanticRow>>> cuentasPagar(
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Vista semantica de cuentas por pagar obtenida correctamente.",
        queryService.cuentasPagar(limit),
        request
    ));
  }

  @Operation(summary = "Consultar vista semantica de movimientos de caja.")
  @GetMapping("/caja")
  public ResponseEntity<ApiResponse<List<CajaMovimientoSemanticRow>>> caja(
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Vista semantica de caja obtenida correctamente.",
        queryService.caja(limit),
        request
    ));
  }

  @Operation(summary = "Consultar vista semantica de contabilidad.")
  @GetMapping("/contabilidad")
  public ResponseEntity<ApiResponse<List<ContabilidadSemanticRow>>> contabilidad(
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Vista semantica de contabilidad obtenida correctamente.",
        queryService.contabilidad(limit),
        request
    ));
  }

  @Operation(summary = "Consultar vista semantica de documentos fiscales internos.")
  @GetMapping("/fiscal")
  public ResponseEntity<ApiResponse<List<FiscalSemanticRow>>> fiscal(
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Vista semantica fiscal obtenida correctamente.",
        queryService.fiscal(limit),
        request
    ));
  }

  @Operation(summary = "Consultar vista semantica de items bajo minimo.")
  @GetMapping("/stock-bajo")
  public ResponseEntity<ApiResponse<List<StockBajoSemanticRow>>> stockBajo(
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Vista semantica de stock bajo obtenida correctamente.",
        queryService.stockBajo(limit),
        request
    ));
  }
}
