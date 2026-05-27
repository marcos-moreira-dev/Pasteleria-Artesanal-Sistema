package com.pasteleria.cotizaciones.application.mapper;

import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationDetailEntity;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationEntity;
import com.pasteleria.cotizaciones.application.QuotationDetailSummary;
import com.pasteleria.cotizaciones.application.QuotationSummary;

import org.springframework.stereotype.Component;

/**
 * Concentra la salida de cotizaciones y sus detalles para evitar duplicación en
 * servicios de comando y consulta.
 */
@Component
public class QuotationDtoMapper {

  /**
   * Devuelve una cotización lista para tablero, auditoría y seguimiento comercial.
   */
  public QuotationSummary toSummary(QuotationEntity quotation) {
    return new QuotationSummary(
        quotation.getId(),
        quotation.getCode(),
        quotation.getClient().getId(),
        quotation.getClient().getFullName(),
        quotation.getStatus(),
        quotation.getOrigin(),
        quotation.getNotes(),
        quotation.getEstimatedTotal(),
        quotation.getCreatedAt(),
        quotation.getDetails().stream()
            .map(this::toDetailSummary)
            .toList()
    );
  }

  private QuotationDetailSummary toDetailSummary(QuotationDetailEntity detail) {
    return new QuotationDetailSummary(
        detail.getId(),
        detail.getProduct() != null ? detail.getProduct().getId() : null,
        detail.getItemDescription(),
        detail.getQuantity(),
        detail.getEstimatedPrice(),
        detail.getSubtotal(),
        detail.getNotes()
    );
  }
}


