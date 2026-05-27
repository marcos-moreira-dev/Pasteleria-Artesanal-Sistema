package com.pasteleria.productos.application.port;

import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryPort {

  List<ProductEntity> findByPublishedTrueAndActiveTrueOrderByNameAsc();

  List<ProductEntity> findAllByOrderByActiveDescPublishedDescNameAsc();

  Page<ProductEntity> findAllByOrderByActiveDescPublishedDescNameAsc(Pageable pageable);

  boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);

  Optional<ProductEntity> findById(Long productId);

  ProductEntity save(ProductEntity product);

  void delete(ProductEntity product);

  List<ProductEntity> findAll();
}
