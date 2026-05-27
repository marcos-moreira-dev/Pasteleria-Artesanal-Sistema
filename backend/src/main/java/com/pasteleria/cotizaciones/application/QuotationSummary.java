package com.pasteleria.cotizaciones.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.cotizaciones.domain.model.QuotationOrigin;
import com.pasteleria.cotizaciones.domain.model.QuotationStatus;

public record QuotationSummary(
    Long id,
    String code,
    Long clientId,
    String clientName,
    QuotationStatus status,
    QuotationOrigin origin,
    String notes,
    BigDecimal estimatedTotal,
    OffsetDateTime createdAt,
    List<QuotationDetailSummary> details
) {
}


