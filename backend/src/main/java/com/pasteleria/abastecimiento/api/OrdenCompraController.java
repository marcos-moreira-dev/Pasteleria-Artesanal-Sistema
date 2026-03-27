package com.pasteleria.abastecimiento.api;

import com.pasteleria.abastecimiento.application.CreateOrdenCompraRequest;
import com.pasteleria.abastecimiento.application.OrdenCompraCommandService;
import com.pasteleria.abastecimiento.application.OrdenCompraDetailDto;
import com.pasteleria.abastecimiento.application.OrdenCompraQueryService;
import com.pasteleria.abastecimiento.application.OrdenCompraSummaryDto;
import com.pasteleria.abastecimiento.application.RecibirOrdenCompraRequest;
import com.pasteleria.abastecimiento.application.UpdateOrdenCompraEstadoRequest;
import com.pasteleria.abastecimiento.application.UpdateOrdenCompraRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Órdenes de Compra", description = "Gestión de órdenes de compra del módulo de abastecimiento.")
@RequestMapping("/api/v1/abastecimiento/ordenes-compra")
public class OrdenCompraController {

  private final OrdenCompraQueryService ordenCompraQueryService;
  private final OrdenCompraCommandService ordenCompraCommandService;

  public OrdenCompraController(
      OrdenCompraQueryService ordenCompraQueryService,
      OrdenCompraCommandService ordenCompraCommandService
  ) {
    this.ordenCompraQueryService = ordenCompraQueryService;
    this.ordenCompraCommandService = ordenCompraCommandService;
  }

  @Operation(summary = "Listar órdenes de compra paginadas", 
             description = "Retorna lista paginada de órdenes de compra con filtros opcionales.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<Page<OrdenCompraSummaryDto>>> getOrdenesPage(
      @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "8") int size,
      @Parameter(description = "Texto de búsqueda") @RequestParam(required = false) String query,
      @Parameter(description = "Estado de la orden") @RequestParam(required = false) String estado,
      HttpServletRequest request) {
    
    Pageable pageable = PageRequest.of(page, size);
    Page<OrdenCompraSummaryDto> ordenes = ordenCompraQueryService.findOrdenes(pageable, query, estado);
    
    return ResponseEntity.ok(ResponseFactory.ok(
        "Órdenes de compra obtenidas correctamente.",
        ordenes,
        request
    ));
  }

  @Operation(summary = "Obtener detalle de orden de compra", 
             description = "Retorna el detalle completo de una orden de compra por su ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<OrdenCompraDetailDto>> getOrdenDetail(
      @Parameter(description = "ID de la orden de compra") @PathVariable Long id,
      HttpServletRequest request) {
    
    OrdenCompraDetailDto orden = ordenCompraQueryService.findOrdenById(id);
    
    return ResponseEntity.ok(ResponseFactory.ok(
        "Detalle de orden de compra obtenido correctamente.",
        orden,
        request
    ));
  }

  @Operation(summary = "Listar órdenes por proveedor", 
             description = "Retorna todas las órdenes de compra de un proveedor específico.")
  @GetMapping("/por-proveedor/{proveedorId}")
  public ResponseEntity<ApiResponse<List<OrdenCompraSummaryDto>>> getOrdenesByProveedor(
      @Parameter(description = "ID del proveedor") @PathVariable Long proveedorId,
      HttpServletRequest request) {
    
    List<OrdenCompraSummaryDto> ordenes = ordenCompraQueryService.findOrdenesByProveedor(proveedorId);
    
    return ResponseEntity.ok(ResponseFactory.ok(
        "Órdenes del proveedor obtenidas correctamente.",
        ordenes,
        request
    ));
  }

  @Operation(summary = "Crear orden de compra",
             description = "Registra una nueva orden de compra en estado borrador.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<OrdenCompraSummaryDto>> createOrdenCompra(
      @Valid @RequestBody CreateOrdenCompraRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Orden de compra registrada correctamente.",
        ordenCompraCommandService.createOrdenCompra(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar orden de compra",
             description = "Permite editar una orden de compra en estado borrador.")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<OrdenCompraSummaryDto>> updateOrdenCompra(
      @PathVariable Long id,
      @Valid @RequestBody UpdateOrdenCompraRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Orden de compra actualizada correctamente.",
        ordenCompraCommandService.updateOrdenCompra(id, body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar estado de orden de compra",
             description = "Permite enviar o cancelar una orden de compra desde borrador.")
  @PatchMapping("/{id}/estado")
  public ResponseEntity<ApiResponse<OrdenCompraSummaryDto>> updateOrdenCompraEstado(
      @PathVariable Long id,
      @Valid @RequestBody UpdateOrdenCompraEstadoRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Estado de orden de compra actualizado correctamente.",
        ordenCompraCommandService.updateEstado(id, body, request),
        request
    ));
  }

  @Operation(summary = "Registrar recepción de orden de compra",
             description = "Aplica cantidades recibidas y genera entradas de inventario.")
  @PostMapping("/{id}/recibir")
  public ResponseEntity<ApiResponse<OrdenCompraDetailDto>> receiveOrdenCompra(
      @PathVariable Long id,
      @Valid @RequestBody RecibirOrdenCompraRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Recepción de orden de compra registrada correctamente.",
        ordenCompraCommandService.receiveOrdenCompra(id, body, request),
        request
    ));
  }
}
