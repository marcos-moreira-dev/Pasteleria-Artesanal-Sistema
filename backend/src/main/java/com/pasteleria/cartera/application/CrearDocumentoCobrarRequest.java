package com.pasteleria.cartera.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record CrearDocumentoCobrarRequest(
    Long pedidoId,
    Long clienteId,
    @Size(max = 60) String codigo,
    LocalDateTime fechaEmision,
    LocalDateTime fechaVencimiento,
    @DecimalMin(value = "0.01") BigDecimal total,
    @Size(max = 1000) String observaciones
) {
}
