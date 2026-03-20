package com.pasteleria.catalogos.application;

public record ProductCategorySummary(
    Long id,
    String code,
    String name,
    String description,
    int visualOrder
) {
}


