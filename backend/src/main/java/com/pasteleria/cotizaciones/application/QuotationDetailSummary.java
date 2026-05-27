package com.pasteleria.cotizaciones.application;

import java.math.BigDecimal;

public record QuotationDetailSummary(
    Long id,
    Long productId,
    String itemDescription,
    int quantity,
    BigDecimal estimatedPrice,
    BigDecimal subtotal,
    String notes
) {
}


