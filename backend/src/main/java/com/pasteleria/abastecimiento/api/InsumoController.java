package com.pasteleria.abastecimiento.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.InsumoCommandService;
import com.pasteleria.abastecimiento.application.InsumoQueryService;
import com.pasteleria.abastecimiento.application.InsumoSummary;
import com.pasteleria.abastecimiento.application.CreateInsumoRequest;
import com.pasteleria.abastecimiento.application.UpdateInsumoRequest;
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
@Tag(name = "Insumos", description = "Gestión de insumos para producción.")
@RestController
@RequestMapping("/api/v1/abastecimiento/insumos")
public class InsumoController {

  private final InsumoQueryService queryService;
  private final InsumoCommandService commandService;

  public InsumoController(InsumoQueryService queryService, InsumoCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar todos los insumos.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<InsumoSummary>>> listInsumos(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Insumos obtenidos correctamente.",
        queryService.listInsumos(),
        request
    ));
  }

  @Operation(summary = "Listar insumos en formato paginado.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<InsumoSummary>>> listInsumosPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de insumos obtenida correctamente.",
        queryService.listInsumosPage(page, size, query),
        request
    ));
  }

  @Operation(summary = "Registrar nuevo insumo.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<InsumoSummary>> createInsumo(
      @Valid @RequestBody CreateInsumoRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Insumo registrado correctamente.",
        commandService.createInsumo(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar insumo.")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<InsumoSummary>> updateInsumo(
      @PathVariable Long id,
      @Valid @RequestBody UpdateInsumoRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Insumo actualizado correctamente.",
        commandService.updateInsumo(id, body, request),
        request
    ));
  }

  @Operation(summary = "Obtener insumo por ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<InsumoSummary>> getById(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Insumo obtenido correctamente.",
        queryService.getById(id),
        request
    ));
  }

  @Operation(summary = "Eliminar insumo.")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteInsumo(@PathVariable Long id, HttpServletRequest request) {
    commandService.deleteInsumo(id, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Insumo eliminado correctamente.",
        null,
        request
    ));
  }
}