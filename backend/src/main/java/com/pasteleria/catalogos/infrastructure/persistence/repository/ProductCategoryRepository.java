package com.pasteleria.catalogos.infrastructure.persistence.repository;

import com.pasteleria.catalogos.application.port.ProductCategoryRepositoryPort;
import com.pasteleria.catalogos.infrastructure.persistence.entity.ProductCategoryEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long>, ProductCategoryRepositoryPort {

  List<ProductCategoryEntity> findByActiveTrueOrderByVisualOrderAscNameAsc();

  boolean existsByIdAndActiveTrue(Long categoryId);
}



