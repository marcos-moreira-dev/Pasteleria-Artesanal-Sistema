package com.pasteleria.cartera.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DocumentoCobrarSummary(
    Long id,
    Long clienteId,
    String clienteNombre,
    Long pedidoId,
    String pedidoCodigo,
    String codigo,
    String estado,
    LocalDateTime fechaEmision,
    LocalDateTime fechaVencimiento,
    BigDecimal total,
    BigDecimal saldo,
    String observaciones
) {
}
