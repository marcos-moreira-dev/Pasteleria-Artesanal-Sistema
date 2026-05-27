package com.pasteleria.produccion.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pasteleria.produccion.application.port.ProductionMaterialConsumptionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionMaterialConsumptionEntity;

public interface ProductionMaterialConsumptionRepository extends JpaRepository<ProductionMaterialConsumptionEntity, Long>, ProductionMaterialConsumptionRepositoryPort {

  boolean existsByProductionId(Long productionId);

  List<ProductionMaterialConsumptionEntity> findByProductionIdOrderByCreatedAtAsc(Long productionId);
}
