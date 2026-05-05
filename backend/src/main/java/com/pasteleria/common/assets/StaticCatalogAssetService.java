package com.pasteleria.common.assets;

import com.pasteleria.common.config.StorageProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Centraliza la resolución de branding e imágenes de producto servidas desde
 * el storage configurable de la aplicación.
 */
@Service
public class StaticCatalogAssetService {

  private static final List<String> SUPPORTED_EXTENSIONS = List.of(".png", ".jpg", ".jpeg", ".webp");
  private static final String PRODUCT_PLACEHOLDER_PATH = "/assets/placeholders/product-placeholder.png";
  private static final String LANDING_PLACEHOLDER_PATH = "/assets/placeholders/landing-placeholder.png";
  private static final String LOGO_SQUARE_PATH = "/assets/branding/logo-cuadrado.png";
  private static final String LOGO_HORIZONTAL_PATH = "/assets/branding/logo-horizontal.png";
  private static final String BANNER_PATH = "/assets/branding/banner-chicas.png";
  private static final String CATEGORY_BANNER_PATH = "/assets/branding/banner-pastel.png";

  private final StorageProperties storageProperties;

  public StaticCatalogAssetService(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  public BrandingAssetSummary getBrandingAssets() {
    return new BrandingAssetSummary(
        resolveOrFallback(LOGO_SQUARE_PATH, PRODUCT_PLACEHOLDER_PATH),
        resolveOrFallback(LOGO_HORIZONTAL_PATH, PRODUCT_PLACEHOLDER_PATH),
        resolveOrFallback(BANNER_PATH, LANDING_PLACEHOLDER_PATH),
        resolveOrFallback(PRODUCT_PLACEHOLDER_PATH, LOGO_SQUARE_PATH),
        resolveOrFallback(CATEGORY_BANNER_PATH, BANNER_PATH)
    );
  }

  public String resolveProductImagePath(String slug) {
    if (slug == null || slug.isBlank()) {
      return resolveLogoFallback();
    }

    for (String extension : SUPPORTED_EXTENSIONS) {
      String candidate = "/assets/products/" + slug + extension;
      if (exists(candidate)) {
        return candidate;
      }
    }

    return resolveLogoFallback();
  }

  public String resolveProductImageAlt(String productName) {
    if (productName == null || productName.isBlank()) {
      return "Logo de la pastelería usado como imagen referencial del catálogo";
    }
    return "Imagen referencial de " + productName.trim();
  }

  private String resolveLogoFallback() {
    return exists(LOGO_SQUARE_PATH) ? LOGO_SQUARE_PATH : PRODUCT_PLACEHOLDER_PATH;
  }

  private String resolveOrFallback(String assetPath, String fallbackPath) {
    return exists(assetPath) ? assetPath : fallbackPath;
  }

  private boolean exists(String publicPath) {
    Path filePath = storageProperties.publicPath(publicPath);
    return Files.exists(filePath);
  }
}
