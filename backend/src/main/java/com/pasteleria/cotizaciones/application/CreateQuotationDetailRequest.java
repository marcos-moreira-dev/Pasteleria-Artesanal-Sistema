package com.pasteleria.cotizaciones.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQuotationDetailRequest(
    Long productId,
    @NotBlank @Size(max = 1000) String itemDescription,
    @Min(1) int quantity,
    @DecimalMin("0.00") BigDecimal estimatedPrice,
    @Size(max = 1000) String notes
) {
}


