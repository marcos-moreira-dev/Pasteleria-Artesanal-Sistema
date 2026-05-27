package com.pasteleria.produccion.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductionFinishedEntrySummary(
    Long id,
    Long batchId,
    Long productionId,
    Long productId,
    String productName,
    BigDecimal cantidad,
    BigDecimal costoTotalEstimado,
    String observaciones,
    OffsetDateTime createdAt
) {
}
