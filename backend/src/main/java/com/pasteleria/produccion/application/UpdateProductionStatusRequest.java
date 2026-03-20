package com.pasteleria.produccion.application;

import com.pasteleria.produccion.domain.model.ProductionStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductionStatusRequest(
    @NotNull ProductionStatus status,
    @Size(max = 1000) String reason
) {
}


