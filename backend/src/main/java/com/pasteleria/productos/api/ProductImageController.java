package com.pasteleria.productos.api;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.productos.application.ProductImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador para gestionar imágenes de productos.
 */
@RestController
@Tag(name = "Imagenes de Productos", description = "Subida y gestión de imágenes del catálogo.")
@RequestMapping("/api/v1/productos")
public class ProductImageController {

  private final ProductImageService productImageService;

  public ProductImageController(ProductImageService productImageService) {
    this.productImageService = productImageService;
  }

  @Operation(summary = "Subir imagen de producto.")
  @PostMapping("/{productId}/imagen")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<String>> uploadProductImage(
      @PathVariable Long productId,
      @RequestParam("imagen") MultipartFile imagen,
      HttpServletRequest request
  ) {
    String imagePath = productImageService.uploadProductImage(productId, imagen);
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Imagen subida correctamente.",
        imagePath,
        request
    ));
  }
}
