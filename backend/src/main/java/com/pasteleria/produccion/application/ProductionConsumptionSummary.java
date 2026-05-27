package com.pasteleria.produccion.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductionConsumptionSummary(
    Long id,
    Long productionId,
    Long orderDetailId,
    Long recetaId,
    Long ingredienteId,
    String ingredienteNombre,
    BigDecimal cantidadTeorica,
    BigDecimal cantidadConsumida,
    BigDecimal costoUnitario,
    BigDecimal costoTotal,
    Long movimientoInventarioId,
    String observaciones,
    OffsetDateTime createdAt
) {
}
