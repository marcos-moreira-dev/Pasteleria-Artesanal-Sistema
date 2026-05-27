package com.pasteleria.abastecimiento.application;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateItemProveedorRequest(
    @NotBlank String itemTipo,
    @NotNull Long itemId,
    @NotNull Long proveedorId,
    @NotNull @DecimalMin("0.0") BigDecimal precioSuministro,
    Boolean esPrincipal
) {
}