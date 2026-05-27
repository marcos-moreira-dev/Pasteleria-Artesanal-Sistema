package com.pasteleria.catalogos.application.port;

import com.pasteleria.catalogos.infrastructure.persistence.entity.ProductCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepositoryPort {

  List<ProductCategoryEntity> findByActiveTrueOrderByVisualOrderAscNameAsc();

  boolean existsByIdAndActiveTrue(Long categoryId);

  Optional<ProductCategoryEntity> findById(Long categoryId);
}
