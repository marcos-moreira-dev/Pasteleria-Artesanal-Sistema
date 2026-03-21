package com.pasteleria.abastecimiento.application;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateInsumoRequest(
    Long umedidaId,
    @NotBlank @Size(max = 40) String codigo,
    @NotBlank @Size(max = 120) String nombre,
    @Size(max = 500) String descripcion,
    @DecimalMin("0.0") BigDecimal stockMinimo,
    @DecimalMin("0.0") BigDecimal stockActual,
    @DecimalMin("0.0") BigDecimal costoReferencial
) {
}