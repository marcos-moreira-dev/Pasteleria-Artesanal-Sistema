package com.pasteleria.abastecimiento.application.mapper;

import com.pasteleria.abastecimiento.application.CreateItemProveedorRequest;
import com.pasteleria.abastecimiento.application.ItemProveedorSummary;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ItemProveedorEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.IngredienteRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.InsumoRepository;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class ItemProveedorDtoMapper {

  private final IngredienteRepository ingredienteRepository;
  private final InsumoRepository insumoRepository;

  public ItemProveedorDtoMapper(IngredienteRepository ingredienteRepository, InsumoRepository insumoRepository) {
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
  }

  public void applyCreateRequest(ItemProveedorEntity entity, CreateItemProveedorRequest request, OffsetDateTime now) {
    entity.setItemTipo(ItemProveedorEntity.ItemTipo.valueOf(request.itemTipo()));
    entity.setItemId(request.itemId());
    entity.setPrecioSuministro(request.precioSuministro());
    entity.setEsPrincipal(request.esPrincipal() != null ? request.esPrincipal() : false);
    entity.setActive(true);
    entity.setCreatedAt(now);
  }

  public ItemProveedorSummary toSummary(ItemProveedorEntity entity) {
    String itemNombre = getItemNombre(entity.getItemTipo(), entity.getItemId());
    return new ItemProveedorSummary(
        entity.getId(),
        entity.getItemTipo(),
        entity.getItemId(),
        itemNombre,
        entity.getProveedor() != null ? entity.getProveedor().getId() : null,
        entity.getProveedor() != null ? entity.getProveedor().getName() : null,
        entity.getPrecioSuministro(),
        entity.isEsPrincipal(),
        entity.isActive()
    );
  }

  private String getItemNombre(ItemProveedorEntity.ItemTipo itemTipo, Long itemId) {
    if (itemTipo == ItemProveedorEntity.ItemTipo.INGREDIENTE) {
      return ((com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort)ingredienteRepository).findById(itemId)
          .map(IngredienteEntity::getName)
          .orElse("Desconocido");
    } else if (itemTipo == ItemProveedorEntity.ItemTipo.INSUMO) {
      return ((com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort)insumoRepository).findById(itemId)
          .map(InsumoEntity::getName)
          .orElse("Desconocido");
    }
    return "Desconocido";
  }
}