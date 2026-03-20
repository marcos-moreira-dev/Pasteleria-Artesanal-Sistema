package com.pasteleria.pedidos.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrderDetailRequest(
    @NotNull Long productId,
    @Size(max = 1000) String itemDescription,
    @Min(1) int quantity,
    @DecimalMin("0.00") BigDecimal unitPrice,
    @Size(max = 1000) String notes
) {
}


