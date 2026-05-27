package com.pasteleria.pedidos.application;

import java.math.BigDecimal;

public record OrderDetailSummary(
    Long id,
    Long productId,
    String itemDescription,
    int quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal,
    String notes
) {
}


