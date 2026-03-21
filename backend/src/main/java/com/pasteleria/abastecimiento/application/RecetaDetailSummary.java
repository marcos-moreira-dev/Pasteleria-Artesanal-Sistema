package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record RecetaDetailSummary(
    Long id,
    Long productoId,
    String productoNombre,
    String nombre,
    BigDecimal rendimientoBase,
    BigDecimal costoEstimado,
    String observaciones,
    Boolean esActiva,
    OffsetDateTime createdAt,
    List<DetalleRecetaSummary> detalles
) {
}
