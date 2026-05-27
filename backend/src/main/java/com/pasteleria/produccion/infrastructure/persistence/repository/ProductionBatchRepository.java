package com.pasteleria.produccion.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pasteleria.produccion.application.port.ProductionBatchRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionBatchEntity;

public interface ProductionBatchRepository extends JpaRepository<ProductionBatchEntity, Long>, ProductionBatchRepositoryPort {

  boolean existsByProductionId(Long productionId);

  List<ProductionBatchEntity> findByProductionIdOrderByCreatedAtAsc(Long productionId);
}
