package com.pasteleria.catalogos.api;

import java.util.List;

import com.pasteleria.catalogos.application.CatalogQueryService;
import com.pasteleria.catalogos.application.ProductCategorySummary;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.assets.BrandingAssetSummary;
import com.pasteleria.common.assets.StaticCatalogAssetService;
import com.pasteleria.productos.application.ProductSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador publico del catalogo comercial.
 */
@Validated
@Tag(name = "Catalogo publico", description = "Lecturas publicas para landing y cotizador.")
@RestController
@RequestMapping("/api/v1/public/catalogo")
public class PublicCatalogController {

  private final CatalogQueryService catalogQueryService;
  private final StaticCatalogAssetService assetService;

  public PublicCatalogController(CatalogQueryService catalogQueryService, StaticCatalogAssetService assetService) {
    this.catalogQueryService = catalogQueryService;
    this.assetService = assetService;
  }

  @Operation(summary = "Listar categorias activas.")
  @GetMapping("/categorias")
  public ResponseEntity<ApiResponse<List<ProductCategorySummary>>> listCategories(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Categorias activas obtenidas correctamente.",
        catalogQueryService.listActiveCategories(),
        request
    ));
  }

  @Operation(summary = "Listar productos publicados.")
  @GetMapping("/productos")
  public ResponseEntity<ApiResponse<List<ProductSummary>>> listProducts(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Productos publicados obtenidos correctamente.",
        catalogQueryService.listPublishedProducts(),
        request
    ));
  }

  @Operation(summary = "Obtener branding público y assets base del catálogo.")
  @GetMapping("/branding")
  public ResponseEntity<ApiResponse<BrandingAssetSummary>> getBranding(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Branding público obtenido correctamente.",
        assetService.getBrandingAssets(),
        request
    ));
  }
}


