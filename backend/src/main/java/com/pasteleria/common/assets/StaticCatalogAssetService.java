package com.pasteleria.common.assets;

import com.pasteleria.common.config.StorageProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Centraliza la resolucion de branding e imagenes publicas del catalogo.
 *
 * <p>La UI recibe URLs publicas del backend; nunca rutas fisicas del storage.
 * La existencia real se valida contra {@code app.storage.root/assets}.</p>
 */
@Service
public class StaticCatalogAssetService {

  private static final List<String> SUPPORTED_EXTENSIONS = List.of(".png", ".jpg", ".jpeg", ".webp");
  private static final String PRODUCT_PLACEHOLDER = "product-placeholder.png";
  private static final String LANDING_PLACEHOLDER = "landing-placeholder.png";
  private static final String LOGO_SQUARE = "logo-cuadrado.png";
  private static final String LOGO_HORIZONTAL = "logo-horizontal.png";
  private static final String BANNER = "banner-chicas.png";
  private static final String CATEGORY_BANNER = "banner-pastel.png";

  private final StorageProperties storageProperties;

  public StaticCatalogAssetService(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  public BrandingAssetSummary getBrandingAssets() {
    return new BrandingAssetSummary(
        resolveOrFallback("branding", LOGO_SQUARE, "placeholders", PRODUCT_PLACEHOLDER),
        resolveOrFallback("branding", LOGO_HORIZONTAL, "placeholders", PRODUCT_PLACEHOLDER),
        resolveOrFallback("branding", BANNER, "placeholders", LANDING_PLACEHOLDER),
        resolveOrFallback("placeholders", PRODUCT_PLACEHOLDER, "branding", LOGO_SQUARE),
        resolveOrFallback("branding", CATEGORY_BANNER, "branding", BANNER)
    );
  }

  public String resolveProductImagePath(String slug) {
    if (slug == null || slug.isBlank()) {
      return resolveLogoFallback();
    }

    for (String extension : SUPPORTED_EXTENSIONS) {
      String filename = slug + extension;
      if (exists("products", filename)) {
        return publicAssetPath("products", filename);
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
    return exists("branding", LOGO_SQUARE)
        ? publicAssetPath("branding", LOGO_SQUARE)
        : publicAssetPath("placeholders", PRODUCT_PLACEHOLDER);
  }

  private String resolveOrFallback(String type, String filename, String fallbackType, String fallbackFilename) {
    return exists(type, filename) ? publicAssetPath(type, filename) : publicAssetPath(fallbackType, fallbackFilename);
  }

  private boolean exists(String type, String filename) {
    Path filePath = storageProperties.assetsPath().resolve(type).resolve(filename).normalize();
    return filePath.startsWith(storageProperties.assetsPath()) && Files.exists(filePath);
  }

  private String publicAssetPath(String type, String filename) {
    return "/api/v1/assets/" + type + "/" + filename;
  }
}
