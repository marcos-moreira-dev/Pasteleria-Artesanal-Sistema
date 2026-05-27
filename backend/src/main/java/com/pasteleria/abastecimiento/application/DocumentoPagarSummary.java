package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DocumentoPagarSummary(
    Long id,
    Long documentoCompraId,
    Long proveedorId,
    String proveedorNombre,
    String codigo,
    String estado,
    LocalDateTime fechaEmision,
    LocalDateTime fechaVencimiento,
    BigDecimal total,
    BigDecimal saldo,
    String observaciones,
    LocalDateTime createdAt
) {
}
