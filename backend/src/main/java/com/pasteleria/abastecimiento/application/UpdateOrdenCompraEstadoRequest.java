package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.domain.model.EstadoOrdenCompra;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateOrdenCompraEstadoRequest(
    @NotNull EstadoOrdenCompra estado,
    @Size(max = 2000) String observaciones
) {
}
