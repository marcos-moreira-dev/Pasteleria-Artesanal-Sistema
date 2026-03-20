package com.pasteleria.produccion.application;

import java.time.OffsetDateTime;

import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;

public record ProductionSummary(
    Long id,
    Long orderId,
    String orderCode,
    String clientName,
    ProductionStatus status,
    ProductionPriority priority,
    OffsetDateTime startedAt,
    OffsetDateTime finishedAt,
    String productionNotes,
    OffsetDateTime createdAt
) {
}


