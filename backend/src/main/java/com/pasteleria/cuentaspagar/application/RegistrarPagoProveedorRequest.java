package com.pasteleria.cuentaspagar.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarPagoProveedorRequest(
    Long proveedorId,
    LocalDateTime fechaPago,
    @NotNull @DecimalMin(value = "0.01") BigDecimal montoTotal,
    @Size(max = 40) String medioPago,
    @Size(max = 120) String referenciaPago,
    @Size(max = 1000) String observaciones,
    @NotEmpty List<@Valid AplicacionPagoProveedorRequest> aplicaciones
) {
}
