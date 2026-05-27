package com.pasteleria.produccion.application;

import org.springframework.stereotype.Component;

import com.pasteleria.produccion.infrastructure.persistence.entity.FinishedProductEntryEntity;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionBatchEntity;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionMaterialConsumptionEntity;

@Component
public class ProductionMaterialMapper {

  public ProductionConsumptionSummary toConsumptionSummary(ProductionMaterialConsumptionEntity entity) {
    return new ProductionConsumptionSummary(
        entity.getId(),
        entity.getProduction().getId(),
        entity.getOrderDetail().getId(),
        entity.getReceta() == null ? null : entity.getReceta().getId(),
        entity.getIngrediente().getId(),
        entity.getIngrediente().getName(),
        entity.getCantidadTeorica(),
        entity.getCantidadConsumida(),
        entity.getCostoUnitario(),
        entity.getCostoTotal(),
        entity.getMovimientoInventario() == null ? null : entity.getMovimientoInventario().getId(),
        entity.getObservaciones(),
        entity.getCreatedAt()
    );
  }

  public ProductionBatchSummary toBatchSummary(ProductionBatchEntity entity) {
    return new ProductionBatchSummary(
        entity.getId(),
        entity.getProduction().getId(),
        entity.getOrderDetail().getId(),
        entity.getProduct().getId(),
        entity.getProduct().getName(),
        entity.getCodigoLote(),
        entity.getFechaProduccion(),
        entity.getCantidadProducida(),
        entity.getCantidadDisponible(),
        entity.getCostoTotalEstimado(),
        entity.getEstado(),
        entity.getObservaciones(),
        entity.getCreatedAt()
    );
  }

  public ProductionFinishedEntrySummary toEntrySummary(FinishedProductEntryEntity entity) {
    return new ProductionFinishedEntrySummary(
        entity.getId(),
        entity.getBatch().getId(),
        entity.getProduction().getId(),
        entity.getProduct().getId(),
        entity.getProduct().getName(),
        entity.getCantidad(),
        entity.getCostoTotalEstimado(),
        entity.getObservaciones(),
        entity.getCreatedAt()
    );
  }
}
