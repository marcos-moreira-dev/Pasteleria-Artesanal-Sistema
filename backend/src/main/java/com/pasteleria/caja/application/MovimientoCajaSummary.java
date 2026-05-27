package com.pasteleria.caja.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MovimientoCajaSummary(
    Long id,
    Long turnoId,
    String cajaCodigo,
    String tipoMovimiento,
    String naturaleza,
    BigDecimal monto,
    String moneda,
    String referenciaTipo,
    String referenciaId,
    String descripcion,
    String estado,
    OffsetDateTime fechaMovimiento,
    String creadoPor
) {
}
