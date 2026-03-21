package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record RecetaSummary(
    Long id,
    Long productoId,
    String productoNombre,
    String nombre,
    BigDecimal rendimientoBase,
    BigDecimal costoEstimado,
    String observaciones,
    Boolean esActiva,
    OffsetDateTime createdAt,
    Long createdById,
    String createdByNombre
) {
}
