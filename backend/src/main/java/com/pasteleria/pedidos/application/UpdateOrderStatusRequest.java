package com.pasteleria.pedidos.application;

import com.pasteleria.pedidos.domain.model.OrderStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateOrderStatusRequest(
    @NotNull OrderStatus status,
    @Size(max = 1000) String reason
) {
}


