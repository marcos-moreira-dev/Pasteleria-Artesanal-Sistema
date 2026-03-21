package com.pasteleria.abastecimiento.application;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateInventarioMovimientoRequest(
    @NotBlank String itemTipo,
    @NotNull Long itemId,
    @NotBlank String tipoMovimiento,
    @NotNull @DecimalMin("0.001") BigDecimal cantidad,
    String referenciaTipo,
    String referenciaId,
    String motivoSalida,
    String observaciones
) {
}
