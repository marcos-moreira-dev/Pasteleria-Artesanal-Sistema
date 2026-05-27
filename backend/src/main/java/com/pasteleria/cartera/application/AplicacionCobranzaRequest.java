package com.pasteleria.cartera.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AplicacionCobranzaRequest(
    @NotNull Long documentoCobrarId,
    @NotNull @DecimalMin(value = "0.01") BigDecimal monto
) {
}
