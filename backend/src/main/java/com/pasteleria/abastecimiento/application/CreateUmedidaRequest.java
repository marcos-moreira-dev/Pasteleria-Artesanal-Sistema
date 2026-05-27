package com.pasteleria.abastecimiento.application;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUmedidaRequest(
    @NotBlank @Size(max = 20) String codigo,
    @NotBlank @Size(max = 60) String nombre,
    @NotBlank @Size(max = 10) String abreviatura,
    @NotBlank String tipo,
    @DecimalMin("0") @DecimalMax("6") int decimales
) {
}