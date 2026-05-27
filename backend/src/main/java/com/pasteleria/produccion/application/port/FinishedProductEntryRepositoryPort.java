package com.pasteleria.produccion.application.port;

import java.util.List;

import com.pasteleria.produccion.infrastructure.persistence.entity.FinishedProductEntryEntity;

public interface FinishedProductEntryRepositoryPort {

  List<FinishedProductEntryEntity> findByProductionIdOrderByCreatedAtAsc(Long productionId);

  FinishedProductEntryEntity save(FinishedProductEntryEntity entity);
}
