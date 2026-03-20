package com.pasteleria.common.assets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Centraliza la resolucion de branding e imagenes de producto servidas
 * por Spring Boot desde el filesystem ({@code ./storage/assets}).
 *
 * <p>Las imagenes viven en {@code ./storage/assets/} fuera del JAR para
 * facilitar updates sin rebuild y versionado en el repo.</p>
 */
@Service
public class StaticCatalogAssetService {

  private static final String STORAGE_BASE = "./storage";
  private static final List<String> SUPPORTED_EXTENSIONS = List.of(".png", ".jpg", ".jpeg", ".webp");
  private static final String PRODUCT_PLACEHOLDER_PATH = "/assets/placeholders/product-placeholder.png";
  private static final String LANDING_PLACEHOLDER_PATH = "/assets/placeholders/landing-placeholder.png";
  private static final String LOGO_SQUARE_PATH = "/assets/branding/logo-cuadrado.png";
  private static final String LOGO_HORIZONTAL_PATH = "/assets/branding/logo-horizontal.png";
  private static final String BANNER_PATH = "/assets/branding/banner-chicas.png";
  private static final String CATEGORY_BANNER_PATH = "/assets/branding/banner-pastel.png";

  public BrandingAssetSummary getBrandingAssets() {
    return new BrandingAssetSummary(
        resolveOrFallback(LOGO_SQUARE_PATH, PRODUCT_PLACEHOLDER_PATH),
        resolveOrFallback(LOGO_HORIZONTAL_PATH, PRODUCT_PLACEHOLDER_PATH),
        resolveOrFallback(BANNER_PATH, LANDING_PLACEHOLDER_PATH),
        resolveOrFallback(PRODUCT_PLACEHOLDER_PATH, PRODUCT_PLACEHOLDER_PATH),
        resolveOrFallback(CATEGORY_BANNER_PATH, BANNER_PATH)
    );
  }

  public String resolveProductImagePath(String slug) {
    if (slug == null || slug.isBlank()) {
      return PRODUCT_PLACEHOLDER_PATH;
    }

    for (String extension : SUPPORTED_EXTENSIONS) {
      String candidate = "/assets/products/" + slug + extension;
      if (exists(candidate)) {
        return candidate;
      }
    }

    return PRODUCT_PLACEHOLDER_PATH;
  }

  public String resolveProductImageAlt(String productName) {
    if (productName == null || productName.isBlank()) {
      return "Imagen referencial del catálogo de pastelería";
    }
    return "Imagen de " + productName.trim();
  }

  private String resolveOrFallback(String assetPath, String fallbackPath) {
    return exists(assetPath) ? assetPath : fallbackPath;
  }

  private boolean exists(String publicPath) {
    Path filePath = Paths.get(STORAGE_BASE, publicPath);
    return Files.exists(filePath);
  }
}
