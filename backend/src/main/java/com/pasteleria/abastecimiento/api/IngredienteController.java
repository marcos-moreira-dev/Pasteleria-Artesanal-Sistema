package com.pasteleria.abastecimiento.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.IngredienteCommandService;
import com.pasteleria.abastecimiento.application.IngredienteQueryService;
import com.pasteleria.abastecimiento.application.IngredienteSummary;
import com.pasteleria.abastecimiento.application.CreateIngredienteRequest;
import com.pasteleria.abastecimiento.application.UpdateIngredienteRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Ingredientes", description = "Gestión de ingredientes para producción.")
@RestController
@RequestMapping("/api/v1/abastecimiento/ingredientes")
public class IngredienteController {

  private final IngredienteQueryService queryService;
  private final IngredienteCommandService commandService;

  public IngredienteController(IngredienteQueryService queryService, IngredienteCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar todos los ingredientes.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<IngredienteSummary>>> listIngredientes(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Ingredientes obtenidos correctamente.",
        queryService.listIngredientes(),
        request
    ));
  }

  @Operation(summary = "Listar ingredientes en formato paginado.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<IngredienteSummary>>> listIngredientesPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de ingredientes obtenida correctamente.",
        queryService.listIngredientesPage(page, size, query),
        request
    ));
  }

  @Operation(summary = "Registrar nuevo ingrediente.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<IngredienteSummary>> createIngrediente(
      @Valid @RequestBody CreateIngredienteRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Ingrediente registrado correctamente.",
        commandService.createIngrediente(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar ingrediente.")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<IngredienteSummary>> updateIngrediente(
      @PathVariable Long id,
      @Valid @RequestBody UpdateIngredienteRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Ingrediente actualizado correctamente.",
        commandService.updateIngrediente(id, body, request),
        request
    ));
  }

  @Operation(summary = "Obtener ingrediente por ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<IngredienteSummary>> getById(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Ingrediente obtenido correctamente.",
        queryService.getById(id),
        request
    ));
  }

  @Operation(summary = "Eliminar ingrediente.")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteIngrediente(@PathVariable Long id, HttpServletRequest request) {
    commandService.deleteIngrediente(id, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Ingrediente eliminado correctamente.",
        null,
        request
    ));
  }
}