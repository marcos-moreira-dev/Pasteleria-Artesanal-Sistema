package com.pasteleria.produccion.infrastructure.persistence.repository;

import com.pasteleria.produccion.application.port.ProductionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductionRepository extends JpaRepository<ProductionEntity, Long>, ProductionRepositoryPort {

  List<ProductionEntity> findAllByOrderByPriorityAscCreatedAtDesc();

  @Query("""
      select distinct production
        from ProductionEntity production
        join fetch production.order customerOrder
        join fetch customerOrder.client
      """)
  List<ProductionEntity> findAllWithOrderAndClient();
}



