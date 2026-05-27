package com.pasteleria.productos.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.productos.application.CreateProductRequest;
import com.pasteleria.productos.application.ProductCommandService;
import com.pasteleria.productos.application.ProductQueryService;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.UpdateProductRequest;

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

/**
 * Controlador administrativo del catalogo interno de productos.
 */
@Validated
@Tag(name = "Productos", description = "Mantenimiento del catalogo comercial y operativo.")
@RestController
@RequestMapping("/api/v1/productos")
public class ProductController {

  private final ProductQueryService productQueryService;
  private final ProductCommandService productCommandService;

  public ProductController(
      ProductQueryService productQueryService,
      ProductCommandService productCommandService
  ) {
    this.productQueryService = productQueryService;
    this.productCommandService = productCommandService;
  }

  @Operation(summary = "Listar productos administrativos.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductSummary>>> listProducts(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Productos obtenidos correctamente.",
        productQueryService.listProducts(),
        request
    ));
  }

  @Operation(summary = "Listar productos en formato paginado para mantenimiento administrativo.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ProductSummary>>> listProductsPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pagina de productos obtenida correctamente.",
        productQueryService.listProductsPage(page, size),
        request
    ));
  }

  @Operation(summary = "Registrar producto.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<ProductSummary>> createProduct(
      @Valid @RequestBody CreateProductRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Producto registrado correctamente.",
        productCommandService.createProduct(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar producto.")
  @PutMapping("/{productId}")
  public ResponseEntity<ApiResponse<ProductSummary>> updateProduct(
      @PathVariable Long productId,
      @Valid @RequestBody UpdateProductRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Producto actualizado correctamente.",
        productCommandService.updateProduct(productId, body, request),
        request
    ));
  }

  @Operation(summary = "Eliminar producto sin historial transaccional asociado.")
  @DeleteMapping("/{productId}")
  public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId, HttpServletRequest request) {
    productCommandService.deleteProduct(productId, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Producto eliminado correctamente.",
        null,
        request
    ));
  }
}


