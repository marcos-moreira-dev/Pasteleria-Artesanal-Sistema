package com.pasteleria.abastecimiento.application.mapper;

import com.pasteleria.abastecimiento.application.DetalleRecetaSummary;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;

import org.springframework.stereotype.Component;

@Component
public class DetalleRecetaDtoMapper {

  public DetalleRecetaSummary toSummary(DetalleRecetaEntity entity) {
    return new DetalleRecetaSummary(
        entity.getId(),
        entity.getReceta() != null ? entity.getReceta().getId() : null,
        entity.getIngrediente() != null ? entity.getIngrediente().getId() : null,
        entity.getIngrediente() != null ? entity.getIngrediente().getName() : null,
        entity.getIngrediente() != null && entity.getIngrediente().getUmedida() != null
            ? entity.getIngrediente().getUmedida().getAbbreviation()
            : null,
        entity.getCantidadBase(),
        entity.getRendimientoPorUnidad(),
        entity.getEsParaPorcion(),
        entity.getObservaciones()
    );
  }
}
