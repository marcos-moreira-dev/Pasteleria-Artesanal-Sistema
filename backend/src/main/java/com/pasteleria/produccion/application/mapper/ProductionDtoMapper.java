package com.pasteleria.produccion.application.mapper;

import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.produccion.application.ProductionSummary;

import org.springframework.stereotype.Component;

/**
 * Traduce el frente de producción a una vista operativa estable para tablero y
 * seguimiento interno.
 */
@Component
public class ProductionDtoMapper {

  /**
   * Resume el frente de trabajo con el contexto mínimo que necesita cocina y ventas.
   */
  public ProductionSummary toSummary(ProductionEntity production) {
    return new ProductionSummary(
        production.getId(),
        production.getOrder().getId(),
        production.getOrder().getCode(),
        production.getOrder().getClient().getFullName(),
        production.getStatus(),
        production.getPriority(),
        production.getStartedAt(),
        production.getFinishedAt(),
        production.getProductionNotes(),
        production.getCreatedAt()
    );
  }
}


