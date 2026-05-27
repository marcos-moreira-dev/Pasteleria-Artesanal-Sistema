package com.pasteleria.auditoria.api;

import java.util.List;

import com.pasteleria.auditoria.application.AuditDashboardSummary;
import com.pasteleria.auditoria.application.AuditEventSummary;
import com.pasteleria.auditoria.application.AuditQueryService;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

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
@Tag(name = "Auditoria", description = "Consulta de eventos auditables para soporte y trazabilidad administrativa.")
@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {

  private final AuditQueryService queryService;

  public AuditoriaController(AuditQueryService queryService) {
    this.queryService = queryService;
  }

  @Operation(summary = "Obtener resumen de auditoria.")
  @GetMapping("/resumen")
  public ResponseEntity<ApiResponse<AuditDashboardSummary>> resumen(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Resumen de auditoria obtenido correctamente.",
        queryService.dashboard(),
        request
    ));
  }

  @Operation(summary = "Listar eventos de auditoria recientes.")
  @GetMapping("/eventos")
  public ResponseEntity<ApiResponse<List<AuditEventSummary>>> eventos(
      @RequestParam(required = false) String modulo,
      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Eventos de auditoria obtenidos correctamente.",
        queryService.recent(modulo, limit),
        request
    ));
  }
}
