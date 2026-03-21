package com.pasteleria.productos.application.mapper;

import java.text.Normalizer;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.catalogos.infrastructure.persistence.entity.ProductCategoryEntity;
import com.pasteleria.common.assets.StaticCatalogAssetService;
import com.pasteleria.common.text.TextSupport;
import com.pasteleria.productos.application.CreateProductRequest;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.RecetaJsonDto;
import com.pasteleria.productos.application.UpdateProductRequest;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import org.springframework.stereotype.Component;

/**
 * Traduce entre entidad persistente y contratos de producto para panel y catálogo.
 *
 * <p>El slug del producto se vuelve la convención canónica para naming de assets:
 * si el equipo agrega una imagen con ese slug en {@code static/assets/products},
 * el frontend la recibirá automáticamente vía backend.</p>
 */
@Component
public class ProductDtoMapper {

  private final StaticCatalogAssetService assetService;
  private final ObjectMapper objectMapper;

  public ProductDtoMapper(StaticCatalogAssetService assetService, ObjectMapper objectMapper) {
    this.assetService = assetService;
    this.objectMapper = objectMapper;
  }

  /**
   * Aplica el alta del producto incluyendo slug, flags de visibilidad y categoría.
   */
  public void applyCreateRequest(ProductEntity product, ProductCategoryEntity category, CreateProductRequest request) {
    applyValues(product, category, request.code(), request.name(), request.description(), request.receta(), request.basePrice(),
        request.quotationRequired(), request.active(), request.published());
  }

  /**
   * Reaplica la normalización cuando un producto se corrige desde el backoffice.
   */
  public void applyUpdateRequest(ProductEntity product, ProductCategoryEntity category, UpdateProductRequest request) {
    applyValues(product, category, request.code(), request.name(), request.description(), request.receta(), request.basePrice(),
        request.quotationRequired(), request.active(), request.published());
  }

  /**
   * Expone una vista compacta apta para panel administrativo y catálogo público.
   */
  public ProductSummary toSummary(ProductEntity product) {
    RecetaJsonDto receta = parseRecetaJson(product.getRecetaJson());
    
    return new ProductSummary(
        product.getId(),
        product.getCode(),
        product.getSlug(),
        product.getName(),
        product.getDescription(),
        receta,
        product.getBasePrice(),
        product.isQuotationRequired(),
        product.getCategory().getCode(),
        product.getCategory().getName(),
        assetService.resolveProductImagePath(product.getSlug()),
        assetService.resolveProductImageAlt(product.getName()),
        product.isActive(),
        product.isPublished()
    );
  }

  private void applyValues(
      ProductEntity product,
      ProductCategoryEntity category,
      String code,
      String name,
      String description,
      RecetaJsonDto receta,
      java.math.BigDecimal basePrice,
      boolean quotationRequired,
      boolean active,
      boolean published
  ) {
    product.setCategory(category);
    product.setCode(code.trim().toUpperCase(Locale.ROOT));
    product.setName(name.trim());
    product.setSlug(buildSlug(name, code));
    product.setDescription(TextSupport.trimToNull(description));
    product.setRecetaJson(recetaToJson(receta));
    product.setBasePrice(basePrice);
    product.setQuotationRequired(quotationRequired);
    product.setActive(active);
    product.setPublished(published && active);
  }

  /**
   * Convierte RecetaJsonDto a String JSON para almacenar en BD
   */
  private String recetaToJson(RecetaJsonDto receta) {
    if (receta == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(receta);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  /**
   * Parsea el String JSON de la BD a RecetaJsonDto
   */
  private RecetaJsonDto parseRecetaJson(String recetaJson) {
    if (recetaJson == null || recetaJson.isBlank()) {
      return new RecetaJsonDto();
    }
    try {
      return objectMapper.readValue(recetaJson, RecetaJsonDto.class);
    } catch (JsonProcessingException e) {
      return new RecetaJsonDto();
    }
  }

  private String buildSlug(String name, String code) {
    String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .toLowerCase(Locale.ROOT)
        .replaceAll("[^a-z0-9]+", "-")
        .replaceAll("(^-|-$)", "");

    if (!normalized.isBlank()) {
      return normalized;
    }

    return code.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
  }
}
