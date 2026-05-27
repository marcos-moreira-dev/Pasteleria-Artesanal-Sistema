package com.pasteleria.produccion.application.port;

import java.util.List;

import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionMaterialConsumptionEntity;

public interface ProductionMaterialConsumptionRepositoryPort {

  boolean existsByProductionId(Long productionId);

  List<ProductionMaterialConsumptionEntity> findByProductionIdOrderByCreatedAtAsc(Long productionId);

  ProductionMaterialConsumptionEntity save(ProductionMaterialConsumptionEntity entity);
}
