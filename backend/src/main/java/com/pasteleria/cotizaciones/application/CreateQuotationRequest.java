package com.pasteleria.cotizaciones.application;

import java.util.List;

import com.pasteleria.cotizaciones.domain.model.QuotationOrigin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateQuotationRequest(
    @NotNull Long clientId,
    @NotNull QuotationOrigin origin,
    @Size(max = 1000) String notes,
    @Valid @NotEmpty List<CreateQuotationDetailRequest> details
) {
}


