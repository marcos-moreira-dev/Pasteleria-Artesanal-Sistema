package com.pasteleria.catalogos.application.mapper;

import com.pasteleria.catalogos.infrastructure.persistence.entity.ProductCategoryEntity;
import com.pasteleria.catalogos.application.ProductCategorySummary;

import org.springframework.stereotype.Component;

/**
 * Traduce categorías persistentes a contratos de lectura para catálogo y
 * backoffice.
 */
@Component
public class ProductCategoryDtoMapper {

  /**
   * Proyecta la categoría persistente a una vista estable para frontend público y panel.
   */
  public ProductCategorySummary toSummary(ProductCategoryEntity category) {
    return new ProductCategorySummary(
        category.getId(),
        category.getCode(),
        category.getName(),
        category.getDescription(),
        category.getVisualOrder()
    );
  }
}


