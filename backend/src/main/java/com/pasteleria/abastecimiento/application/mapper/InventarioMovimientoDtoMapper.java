package com.pasteleria.abastecimiento.application.mapper;

import com.pasteleria.abastecimiento.application.InventarioMovimientoSummary;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;

import org.springframework.stereotype.Component;

@Component
public class InventarioMovimientoDtoMapper {

  public InventarioMovimientoSummary toSummary(InventarioMovimientoEntity entity) {
    return new InventarioMovimientoSummary(
        entity.getId(),
        entity.getItemTipo(),
        entity.getItemId(),
        null,
        entity.getTipoMovimiento(),
        entity.getCantidad(),
        entity.getSaldoAnterior(),
        entity.getSaldoPosterior(),
        entity.getReferenciaTipo(),
        entity.getReferenciaId(),
        entity.getMotivoSalida(),
        entity.getObservaciones(),
        entity.getFechaMovimiento(),
        entity.getRegistradoPor() != null ? entity.getRegistradoPor().getId() : null,
        entity.getRegistradoPor() != null ? entity.getRegistradoPor().getUsername() : null
    );
  }

  public InventarioMovimientoSummary toSummary(InventarioMovimientoEntity entity, String itemNombre) {
    return new InventarioMovimientoSummary(
        entity.getId(),
        entity.getItemTipo(),
        entity.getItemId(),
        itemNombre,
        entity.getTipoMovimiento(),
        entity.getCantidad(),
        entity.getSaldoAnterior(),
        entity.getSaldoPosterior(),
        entity.getReferenciaTipo(),
        entity.getReferenciaId(),
        entity.getMotivoSalida(),
        entity.getObservaciones(),
        entity.getFechaMovimiento(),
        entity.getRegistradoPor() != null ? entity.getRegistradoPor().getId() : null,
        entity.getRegistradoPor() != null ? entity.getRegistradoPor().getUsername() : null
    );
  }
}
