package com.pasteleria.contabilidad.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AsientoContableSummary(
    Long id,
    String codigo,
    LocalDateTime fechaAsiento,
    String tipoDiarioCodigo,
    String descripcion,
    String origenTipo,
    String origenId,
    String estado,
    BigDecimal totalDebe,
    BigDecimal totalHaber,
    List<AsientoContableDetalleSummary> lineas
) {
}
