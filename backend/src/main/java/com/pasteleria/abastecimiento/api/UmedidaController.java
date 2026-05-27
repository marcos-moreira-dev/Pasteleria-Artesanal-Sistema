package com.pasteleria.abastecimiento.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.UmedidaCommandService;
import com.pasteleria.abastecimiento.application.UmedidaQueryService;
import com.pasteleria.abastecimiento.application.UmedidaSummary;
import com.pasteleria.abastecimiento.application.CreateUmedidaRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Unidades de Medida", description = "Gestión de unidades de medida para ingredientes e insumos.")
@RestController
@RequestMapping("/api/v1/abastecimiento/unidades-medida")
public class UmedidaController {

  private final UmedidaQueryService queryService;
  private final UmedidaCommandService commandService;

  public UmedidaController(UmedidaQueryService queryService, UmedidaCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar todas las unidades de medida.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<UmedidaSummary>>> listUmedidas(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Unidades de medida obtenidas correctamente.",
        queryService.listUmedidas(),
        request
    ));
  }

  @Operation(summary = "Listar unidades de medida en formato paginado.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<UmedidaSummary>>> listUmedidasPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de unidades de medida obtenida correctamente.",
        queryService.listUmedidasPage(page, size, query),
        request
    ));
  }

  @Operation(summary = "Obtener unidad de medida por ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UmedidaSummary>> getById(
      @PathVariable @NotNull Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Unidad de medida obtenida correctamente.",
        queryService.getById(id),
        request
    ));
  }

  @Operation(summary = "Registrar nueva unidad de medida.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<UmedidaSummary>> createUmedida(
      @Valid @RequestBody CreateUmedidaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Unidad de medida registrada correctamente.",
        commandService.createUmedida(body),
        request
    ));
  }
}