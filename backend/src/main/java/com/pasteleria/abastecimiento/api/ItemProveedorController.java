package com.pasteleria.abastecimiento.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.ItemProveedorCommandService;
import com.pasteleria.abastecimiento.application.ItemProveedorQueryService;
import com.pasteleria.abastecimiento.application.ItemProveedorSummary;
import com.pasteleria.abastecimiento.application.CreateItemProveedorRequest;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Items Proveedor", description = "Gestión de relaciones entre items y proveedores.")
@RestController
@RequestMapping("/api/v1/abastecimiento/items-proveedor")
public class ItemProveedorController {

  private final ItemProveedorQueryService queryService;
  private final ItemProveedorCommandService commandService;

  public ItemProveedorController(ItemProveedorQueryService queryService, ItemProveedorCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar items de un proveedor específico.")
  @GetMapping("/proveedor/{proveedorId}")
  public ResponseEntity<ApiResponse<List<ItemProveedorSummary>>> listByProveedor(
      @PathVariable @NotNull Long proveedorId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Items del proveedor obtenidos correctamente.",
        queryService.listByProveedor(proveedorId),
        request
    ));
  }

  @Operation(summary = "Listar items de un proveedor en formato paginado.")
  @GetMapping("/proveedor/{proveedorId}/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ItemProveedorSummary>>> listByProveedorPage(
      @PathVariable @NotNull Long proveedorId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de items del proveedor obtenida correctamente.",
        queryService.listByProveedorPage(proveedorId, page, size),
        request
    ));
  }

  @Operation(summary = "Listar proveedores de un item específico.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ItemProveedorSummary>>> listByItem(
      @RequestParam @NotBlank String itemTipo,
      @RequestParam @NotNull Long itemId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Proveedores del item obtenidos correctamente.",
        queryService.listByItem(itemTipo, itemId),
        request
    ));
  }

  @Operation(summary = "Listar proveedores de un item en formato paginado.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ItemProveedorSummary>>> listByItemPage(
      @RequestParam @NotBlank String itemTipo,
      @RequestParam @NotNull Long itemId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de proveedores del item obtenida correctamente.",
        queryService.listByItemPage(itemTipo, itemId, page, size),
        request
    ));
  }

  @Operation(summary = "Obtener item-proveedor por ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ItemProveedorSummary>> getById(
      @PathVariable @NotNull Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Item-Proveedor obtenido correctamente.",
        queryService.getById(id),
        request
    ));
  }

  @Operation(summary = "Registrar nuevo item-proveedor.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<ItemProveedorSummary>> createItemProveedor(
      @Valid @RequestBody CreateItemProveedorRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Item-Proveedor registrado correctamente.",
        commandService.createItemProveedor(body, request),
        request
    ));
  }

  @Operation(summary = "Eliminar item-proveedor.")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteItemProveedor(
      @PathVariable @NotNull Long id,
      HttpServletRequest request
  ) {
    commandService.deleteItemProveedor(id, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Item-Proveedor eliminado correctamente.",
        null,
        request
    ));
  }
}