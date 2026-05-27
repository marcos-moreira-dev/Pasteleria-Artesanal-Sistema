package com.pasteleria.caja.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record AbrirCajaRequest(
    @Size(max = 40) String cajaCodigo,
    @DecimalMin(value = "0.00") BigDecimal montoApertura,
    @Size(max = 500) String observaciones
) {
}
