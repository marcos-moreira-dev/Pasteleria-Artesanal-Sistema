package com.pasteleria.abastecimiento.api;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import com.pasteleria.abastecimiento.application.CreateInventarioMovimientoRequest;
import com.pasteleria.abastecimiento.application.InventarioMovimientoCommandService;
import com.pasteleria.abastecimiento.application.InventarioMovimientoQueryService;
import com.pasteleria.abastecimiento.application.InventarioMovimientoSummary;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
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
@Tag(name = "Inventario - Movimientos", description = "Gestión de movimientos de inventario.")
@RestController
@RequestMapping("/api/v1/abastecimiento/inventario")
public class InventarioMovimientoController {

  private final InventarioMovimientoQueryService queryService;
  private final InventarioMovimientoCommandService commandService;

  public InventarioMovimientoController(
      InventarioMovimientoQueryService queryService,
      InventarioMovimientoCommandService commandService
  ) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar movimientos de inventario con filtros opcionales.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<InventarioMovimientoSummary>>> listMovimientos(
      @RequestParam(required = false) String itemTipo,
      @RequestParam(required = false) Long itemId,
      @RequestParam(required = false) String tipoMovimiento,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Movimientos de inventario obtenidos correctamente.",
        queryService.listMovimientos(
            itemTipo,
            itemId,
            tipoMovimiento,
            toStartOfDay(fechaDesde),
            toEndOfDay(fechaHasta)
        ),
        request
    ));
  }

  @Operation(summary = "Listar movimientos de inventario por item.")
  @GetMapping("/{itemTipo}/{itemId}")
  public ResponseEntity<ApiResponse<List<InventarioMovimientoSummary>>> listByItem(
      @PathVariable @NotBlank String itemTipo,
      @PathVariable @NotNull Long itemId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Movimientos de inventario obtenidos correctamente.",
        queryService.listByItem(itemTipo, itemId),
        request
    ));
  }

  @Operation(summary = "Listar movimientos de inventario por item en formato paginado.")
  @GetMapping("/{itemTipo}/{itemId}/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<InventarioMovimientoSummary>>> listByItemPage(
      @PathVariable @NotBlank String itemTipo,
      @PathVariable @NotNull Long itemId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de movimientos de inventario obtenida correctamente.",
        queryService.listByItemPage(itemTipo, itemId, page, size),
        request
    ));
  }

  @Operation(summary = "Listar movimientos de inventario por referencia.")
  @GetMapping("/referencia/{referenciaTipo}/{referenciaId}")
  public ResponseEntity<ApiResponse<List<InventarioMovimientoSummary>>> listByReferencia(
      @PathVariable @NotBlank String referenciaTipo,
      @PathVariable @NotBlank String referenciaId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Movimientos de inventario obtenidos correctamente.",
        queryService.listByReferencia(referenciaTipo, referenciaId),
        request
    ));
  }

  @Operation(summary = "Listar movimientos de inventario por referencia en formato paginado.")
  @GetMapping("/referencia/{referenciaTipo}/{referenciaId}/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<InventarioMovimientoSummary>>> listByReferenciaPage(
      @PathVariable @NotBlank String referenciaTipo,
      @PathVariable @NotBlank String referenciaId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de movimientos de inventario obtenida correctamente.",
        queryService.listByReferenciaPage(referenciaTipo, referenciaId, page, size),
        request
    ));
  }

  @Operation(summary = "Obtener movimiento de inventario por ID.")
  @GetMapping("/movimiento/{id}")
  public ResponseEntity<ApiResponse<InventarioMovimientoSummary>> getById(
      @PathVariable @NotNull Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Movimiento de inventario obtenido correctamente.",
        queryService.getById(id),
        request
    ));
  }

  @Operation(summary = "Registrar un movimiento de inventario.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<InventarioMovimientoSummary>> createMovimiento(
      @Valid @RequestBody CreateInventarioMovimientoRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Movimiento de inventario registrado correctamente.",
        commandService.createMovimiento(body, null, request),
        request
    ));
  }

  private OffsetDateTime toStartOfDay(LocalDate date) {
    if (date == null) {
      return null;
    }
    return date.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
  }

  private OffsetDateTime toEndOfDay(LocalDate date) {
    if (date == null) {
      return null;
    }
    return date.plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .minusNanos(1)
        .toOffsetDateTime();
  }
}
