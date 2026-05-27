package com.pasteleria.abastecimiento.application.mapper;

import com.pasteleria.abastecimiento.application.CreateIngredienteRequest;
import com.pasteleria.abastecimiento.application.IngredienteSummary;
import com.pasteleria.abastecimiento.application.UpdateIngredienteRequest;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.common.text.TextSupport;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class IngredienteDtoMapper {

  public void applyCreateRequest(IngredienteEntity entity, CreateIngredienteRequest request, OffsetDateTime now) {
    entity.setCode(request.codigo().trim().toUpperCase());
    entity.setName(request.nombre().trim());
    entity.setDescription(TextSupport.trimToNull(request.descripcion()));
    entity.setStockMinimo(request.stockMinimo() != null ? request.stockMinimo() : java.math.BigDecimal.ZERO);
    entity.setStockActual(request.stockActual() != null ? request.stockActual() : java.math.BigDecimal.ZERO);
    entity.setCostoReferencial(request.costoReferencial());
    entity.setActive(true);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);
  }

  public void applyUpdateRequest(IngredienteEntity entity, UpdateIngredienteRequest request, OffsetDateTime now) {
    entity.setCode(request.codigo().trim().toUpperCase());
    entity.setName(request.nombre().trim());
    entity.setDescription(TextSupport.trimToNull(request.descripcion()));
    entity.setStockMinimo(request.stockMinimo() != null ? request.stockMinimo() : java.math.BigDecimal.ZERO);
    entity.setStockActual(request.stockActual() != null ? request.stockActual() : java.math.BigDecimal.ZERO);
    entity.setCostoReferencial(request.costoReferencial());
    entity.setUpdatedAt(now);
  }

  public IngredienteSummary toSummary(IngredienteEntity entity) {
    return new IngredienteSummary(
        entity.getId(),
        entity.getUmedida() != null ? entity.getUmedida().getId() : null,
        entity.getUmedida() != null ? entity.getUmedida().getName() : null,
        entity.getCode(),
        entity.getName(),
        entity.getDescription(),
        entity.getStockMinimo(),
        entity.getStockActual(),
        entity.getCostoReferencial(),
        entity.isActive(),
        entity.getCreatedAt()
    );
  }
}