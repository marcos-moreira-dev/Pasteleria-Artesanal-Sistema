package com.pasteleria.produccion.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pasteleria.produccion.application.port.FinishedProductEntryRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.FinishedProductEntryEntity;

public interface FinishedProductEntryRepository extends JpaRepository<FinishedProductEntryEntity, Long>, FinishedProductEntryRepositoryPort {

  List<FinishedProductEntryEntity> findByProductionIdOrderByCreatedAtAsc(Long productionId);
}
