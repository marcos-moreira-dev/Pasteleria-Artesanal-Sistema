package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record InventarioMovimientoSummary(
    Long id,
    String itemTipo,
    Long itemId,
    String itemNombre,
    String tipoMovimiento,
    BigDecimal cantidad,
    BigDecimal saldoPosterior,
    String referenciaTipo,
    String referenciaId,
    String motivoSalida,
    String observaciones,
    OffsetDateTime fechaMovimiento,
    Long registradoPorId,
    String registradoPorNombre
) {
}
