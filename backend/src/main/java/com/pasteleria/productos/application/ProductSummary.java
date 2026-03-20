package com.pasteleria.productos.application;

import java.math.BigDecimal;

public record ProductSummary(
    Long id,
    String code,
    String slug,
    String name,
    String description,
    BigDecimal basePrice,
    boolean quotationRequired,
    String categoryCode,
    String categoryName,
    String imagePath,
    String imageAlt,
    boolean active,
    boolean published
) {
}


