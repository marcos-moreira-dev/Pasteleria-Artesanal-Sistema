package com.pasteleria.contabilidad.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AsientoContableDetalleRequest(
    @NotBlank String cuentaCodigo,
    String descripcion,
    @NotNull BigDecimal debe,
    @NotNull BigDecimal haber
) {
}
