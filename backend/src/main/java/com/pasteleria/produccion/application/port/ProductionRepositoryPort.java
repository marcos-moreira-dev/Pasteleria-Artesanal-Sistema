package com.pasteleria.produccion.application.port;

import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import java.util.List;
import java.util.Optional;

public interface ProductionRepositoryPort {

  List<ProductionEntity> findAllByOrderByPriorityAscCreatedAtDesc();

  Optional<ProductionEntity> findById(Long productionId);

  ProductionEntity save(ProductionEntity production);

  List<ProductionEntity> findAll();
}
