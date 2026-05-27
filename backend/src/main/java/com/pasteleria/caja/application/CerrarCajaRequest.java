package com.pasteleria.caja.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CerrarCajaRequest(
    Long turnoId,
    @Size(max = 40) String cajaCodigo,
    @NotNull @DecimalMin(value = "0.00") BigDecimal montoDeclarado,
    @Size(max = 500) String observaciones
) {
}
