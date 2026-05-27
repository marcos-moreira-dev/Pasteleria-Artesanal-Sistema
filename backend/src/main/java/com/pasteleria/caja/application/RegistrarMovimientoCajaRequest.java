package com.pasteleria.caja.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarMovimientoCajaRequest(
    Long turnoId,
    @Size(max = 40) String cajaCodigo,
    @NotBlank @Size(max = 40) String tipoMovimiento,
    @NotBlank @Size(max = 20) String naturaleza,
    @NotNull @DecimalMin(value = "0.01") BigDecimal monto,
    @Size(max = 80) String referenciaTipo,
    @Size(max = 120) String referenciaId,
    @Size(max = 500) String descripcion
) {
}
