package com.pasteleria.cuentaspagar.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AplicacionPagoProveedorRequest(
    @NotNull Long documentoPagarId,
    @NotNull @DecimalMin(value = "0.01") BigDecimal monto
) {
}
