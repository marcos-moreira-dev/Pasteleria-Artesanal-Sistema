package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record InsumoSummary(
    Long id,
    Long umedidaId,
    String umedidaNombre,
    String codigo,
    String nombre,
    String descripcion,
    BigDecimal stockMinimo,
    BigDecimal stockActual,
    BigDecimal costoReferencial,
    boolean activo,
    OffsetDateTime createdAt
) {
}