package com.pasteleria.produccion.infrastructure.persistence.repository;

import com.pasteleria.produccion.application.port.ProductionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRepository extends JpaRepository<ProductionEntity, Long>, ProductionRepositoryPort {

  List<ProductionEntity> findAllByOrderByPriorityAscCreatedAtDesc();
}



