package com.pasteleria.produccion.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductionBatchSummary(
    Long id,
    Long productionId,
    Long orderDetailId,
    Long productId,
    String productName,
    String codigoLote,
    OffsetDateTime fechaProduccion,
    BigDecimal cantidadProducida,
    BigDecimal cantidadDisponible,
    BigDecimal costoTotalEstimado,
    String estado,
    String observaciones,
    OffsetDateTime createdAt
) {
}
