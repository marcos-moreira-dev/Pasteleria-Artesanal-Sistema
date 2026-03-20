package com.pasteleria.produccion.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.produccion.application.ProductionCommandService;
import com.pasteleria.produccion.application.ProductionQueryService;
import com.pasteleria.produccion.application.ProductionSummary;
import com.pasteleria.produccion.application.UpdateProductionStatusRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone la cola operativa del obrador y sus transiciones de estado.
 */
@Validated
@Tag(name = "Produccion", description = "Seguimiento del flujo de cocina, decoracion y empaque.")
@RestController
@RequestMapping("/api/v1/produccion")
public class ProductionController {

  private final ProductionQueryService productionQueryService;
  private final ProductionCommandService productionCommandService;

  public ProductionController(
      ProductionQueryService productionQueryService,
      ProductionCommandService productionCommandService
  ) {
    this.productionQueryService = productionQueryService;
    this.productionCommandService = productionCommandService;
  }

  @Operation(summary = "Listar cola de produccion.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductionSummary>>> listProductionQueue(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Cola de produccion obtenida correctamente.",
        productionQueryService.listProductionQueue(),
        request
    ));
  }

  @Operation(summary = "Listar cola de produccion paginada para tableros operativos.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ProductionSummary>>> listProductionQueuePage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pagina de produccion obtenida correctamente.",
        productionQueryService.listProductionQueuePage(page, size),
        request
    ));
  }

  @Operation(summary = "Mover pedido a la siguiente etapa de produccion.")
  @PatchMapping("/{productionId}/estado")
  public ResponseEntity<ApiResponse<ProductionSummary>> updateProductionStatus(
      @PathVariable Long productionId,
      @Valid @RequestBody UpdateProductionStatusRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Estado de produccion actualizado correctamente.",
        productionCommandService.updateProductionStatus(productionId, body, request),
        request
    ));
  }
}


