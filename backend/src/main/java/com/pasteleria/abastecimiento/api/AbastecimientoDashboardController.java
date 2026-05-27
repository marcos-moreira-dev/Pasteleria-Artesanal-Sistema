package com.pasteleria.abastecimiento.api;

import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardQueryService;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Abastecimiento Dashboard", description = "Dashboard consolidado del módulo de abastecimiento.")
@RequestMapping("/api/v1/abastecimiento")
public class AbastecimientoDashboardController {

  private final AbastecimientoDashboardQueryService dashboardService;

  public AbastecimientoDashboardController(AbastecimientoDashboardQueryService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @Operation(summary = "Obtener dashboard de abastecimiento", 
             description = "Retorna métricas, alertas, sugerencias y datos consolidados del módulo.")
  @GetMapping("/dashboard")
  public ResponseEntity<ApiResponse<AbastecimientoDashboardDto>> getDashboard(HttpServletRequest request) {
    AbastecimientoDashboardDto dashboard = dashboardService.buildDashboard();
    return ResponseEntity.ok(ResponseFactory.ok(
        "Dashboard de abastecimiento obtenido correctamente.",
        dashboard,
        request
    ));
  }
}
