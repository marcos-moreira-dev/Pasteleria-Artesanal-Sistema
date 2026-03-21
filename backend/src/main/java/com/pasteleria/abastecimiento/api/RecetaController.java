package com.pasteleria.abastecimiento.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.CreateRecetaRequest;
import com.pasteleria.abastecimiento.application.RecetaCommandService;
import com.pasteleria.abastecimiento.application.RecetaDetailSummary;
import com.pasteleria.abastecimiento.application.RecetaQueryService;
import com.pasteleria.abastecimiento.application.RecetaSummary;
import com.pasteleria.abastecimiento.application.UpdateRecetaRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Recetas", description = "Gestión de recetas y formulas de producción.")
@RestController
@RequestMapping("/api/v1/abastecimiento/recetas")
public class RecetaController {

  private final RecetaQueryService queryService;
  private final RecetaCommandService commandService;

  public RecetaController(RecetaQueryService queryService, RecetaCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar todas las recetas.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<RecetaSummary>>> listRecetas(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Recetas obtenidas correctamente.",
        queryService.listRecetas(),
        request
    ));
  }

  @Operation(summary = "Listar recetas en formato paginado.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<RecetaSummary>>> listRecetasPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de recetas obtenida correctamente.",
        queryService.listRecetasPage(page, size, query),
        request
    ));
  }

  @Operation(summary = "Obtener detalle de una receta con sus ingredientes.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<RecetaDetailSummary>> getRecetaDetail(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Receta obtenida correctamente.",
        queryService.getRecetaDetail(id),
        request
    ));
  }

  @Operation(summary = "Obtener recetas de un producto.")
  @GetMapping("/producto/{productoId}")
  public ResponseEntity<ApiResponse<List<RecetaSummary>>> getRecetasByProducto(
      @PathVariable Long productoId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Recetas del producto obtenidas correctamente.",
        queryService.getRecetasByProducto(productoId),
        request
    ));
  }

  @Operation(summary = "Crear una nueva receta.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<RecetaDetailSummary>> createReceta(
      @Valid @RequestBody CreateRecetaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Receta creada correctamente.",
        commandService.createReceta(body, null, request),
        request
    ));
  }

  @Operation(summary = "Actualizar una receta existente.")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<RecetaDetailSummary>> updateReceta(
      @PathVariable Long id,
      @Valid @RequestBody UpdateRecetaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Receta actualizada correctamente.",
        commandService.updateReceta(id, body, request),
        request
    ));
  }

  @Operation(summary = "Eliminar una receta.")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteReceta(@PathVariable Long id, HttpServletRequest request) {
    commandService.deleteReceta(id, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Receta eliminada correctamente.",
        null,
        request
    ));
  }

  @Operation(summary = "Activar o desactivar una receta.")
  @PatchMapping("/{id}/toggle-activa")
  public ResponseEntity<ApiResponse<RecetaDetailSummary>> toggleActiva(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Estado de receta actualizado correctamente.",
        commandService.toggleActiva(id, request),
        request
    ));
  }
}
