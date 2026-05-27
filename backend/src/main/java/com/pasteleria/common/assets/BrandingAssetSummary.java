package com.pasteleria.common.assets;

/**
 * Expone el paquete minimo de branding publico servido por el backend.
 * El frontend no conoce rutas locales del repositorio; solo consume este contrato.
 */
public record BrandingAssetSummary(
    String logoSquarePath,
    String logoHorizontalPath,
    String bannerPath,
    String productPlaceholderPath,
    String categoryBannerPath
) {
}
