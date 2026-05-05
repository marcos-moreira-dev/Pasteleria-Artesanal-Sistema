package com.pasteleria.casosuso.api;

import java.util.List;

import com.pasteleria.casosuso.api.dto.CasoUsoHubResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoOperativoResponse;
import com.pasteleria.casosuso.application.CasoUsoOperativoService;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Guía operativa", description = "Flujos de trabajo consultivos para operar la pastelería.")
@RestController
@RequestMapping("/api/v1/casos-uso")
public class CasoUsoOperativoController {

  private final CasoUsoOperativoService service;

  public CasoUsoOperativoController(CasoUsoOperativoService service) {
    this.service = service;
  }

  @Operation(summary = "Listar flujos de guía operativa.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<CasoUsoOperativoResponse>>> listar(
      @RequestParam(required = false) String modulo,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Flujos de guía operativa obtenidos correctamente.",
        service.listar(modulo),
        request
    ));
  }

  @Operation(summary = "Obtener hub de guía operativa agrupado por áreas.")
  @GetMapping("/hub")
  public ResponseEntity<ApiResponse<CasoUsoHubResponse>> hub(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Guía operativa obtenida correctamente.",
        service.hub(),
        request
    ));
  }

  @Operation(summary = "Consultar un flujo de guía operativa por código.")
  @GetMapping("/{codigo}")
  public ResponseEntity<ApiResponse<CasoUsoOperativoResponse>> obtenerPorCodigo(
      @PathVariable String codigo,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Flujo de guía operativa obtenido correctamente.",
        service.obtenerPorCodigo(codigo),
        request
    ));
  }
}
