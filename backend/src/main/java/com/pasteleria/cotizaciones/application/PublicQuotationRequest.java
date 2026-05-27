package com.pasteleria.cotizaciones.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud publica simplificada para capturar interes comercial desde la landing.
 */
public record PublicQuotationRequest(
    @NotBlank @Size(max = 160) String fullName,
    @Size(max = 30) String phone,
    @Email @Size(max = 120) String email,
    @NotBlank @Size(max = 1000) String celebrationType,
    @NotBlank @Size(max = 1000) String requestedProduct,
    @Min(1) int estimatedServings,
    @DecimalMin("0.00") BigDecimal estimatedBudget,
    @Size(max = 1000) String notes
) {
}


