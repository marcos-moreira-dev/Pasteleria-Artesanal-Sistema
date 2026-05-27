package com.pasteleria.contabilidad.application;

import java.math.BigDecimal;

public record AsientoContableDetalleSummary(
    Long id,
    String cuentaCodigo,
    String cuentaNombre,
    String descripcion,
    BigDecimal debe,
    BigDecimal haber
) {
}
