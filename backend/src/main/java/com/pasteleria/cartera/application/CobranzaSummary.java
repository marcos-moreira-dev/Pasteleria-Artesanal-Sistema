package com.pasteleria.cartera.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CobranzaSummary(
    Long id,
    Long clienteId,
    String clienteNombre,
    String codigo,
    LocalDateTime fechaCobranza,
    BigDecimal montoTotal,
    String medioPago,
    String referenciaPago,
    String estado,
    String observaciones,
    List<CobranzaDetalleSummary> detalles
) {
}
