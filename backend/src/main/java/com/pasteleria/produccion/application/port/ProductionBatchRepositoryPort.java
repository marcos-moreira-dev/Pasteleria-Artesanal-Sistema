package com.pasteleria.produccion.application.port;

import java.util.List;

import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionBatchEntity;

public interface ProductionBatchRepositoryPort {

  boolean existsByProductionId(Long productionId);

  List<ProductionBatchEntity> findByProductionIdOrderByCreatedAtAsc(Long productionId);

  ProductionBatchEntity save(ProductionBatchEntity entity);
}
