package com.pasteleria.pedidos.application;

import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.pedidos.domain.model.OrderOrigin;
import com.pasteleria.pedidos.domain.model.OrderPriority;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
    @NotNull Long clientId,
    Long quotationId,
    @NotNull @FutureOrPresent OffsetDateTime estimatedDeliveryAt,
    @NotNull OrderPriority priority,
    @NotNull OrderOrigin origin,
    @Size(max = 1000) String notes,
    @Valid @NotEmpty List<CreateOrderDetailRequest> details
) {
}


