package com.pasteleria.productos.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
    @NotNull Long categoryId,
    @NotBlank @Size(max = 50) String code,
    @NotBlank @Size(max = 150) String name,
    @Size(max = 1000) String description,
    @NotNull @DecimalMin("0.00") BigDecimal basePrice,
    boolean quotationRequired,
    boolean active,
    boolean published
) {
}


