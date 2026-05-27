package com.pasteleria.productos.infrastructure.persistence.repository;

import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryPort {

  List<ProductEntity> findByPublishedTrueAndActiveTrueOrderByNameAsc();

  List<ProductEntity> findAllByOrderByActiveDescPublishedDescNameAsc();

  Page<ProductEntity> findAllByOrderByActiveDescPublishedDescNameAsc(Pageable pageable);

  boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);
}



