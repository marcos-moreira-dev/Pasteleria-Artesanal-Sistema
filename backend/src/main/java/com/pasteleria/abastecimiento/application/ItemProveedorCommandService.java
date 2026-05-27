package com.pasteleria.abastecimiento.application;

import java.time.OffsetDateTime;

import com.pasteleria.abastecimiento.application.port.ItemProveedorRepositoryPort;
import com.pasteleria.abastecimiento.application.port.ProveedorRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ItemProveedorEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;
import com.pasteleria.abastecimiento.application.mapper.ItemProveedorDtoMapper;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ItemProveedorCommandService {

  private final ItemProveedorRepositoryPort itemProveedorRepository;
  private final ProveedorRepositoryPort proveedorRepository;
  private final ItemProveedorDtoMapper mapper;
  private final AuditTrailService auditTrailService;

  public ItemProveedorCommandService(
      ItemProveedorRepositoryPort itemProveedorRepository,
      ProveedorRepositoryPort proveedorRepository,
      ItemProveedorDtoMapper mapper,
      AuditTrailService auditTrailService
  ) {
    this.itemProveedorRepository = itemProveedorRepository;
    this.proveedorRepository = proveedorRepository;
    this.mapper = mapper;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public ItemProveedorSummary createItemProveedor(CreateItemProveedorRequest request, HttpServletRequest httpRequest) {
    ProveedorEntity proveedor = proveedorRepository.findById(request.proveedorId())
        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));

    if (Boolean.TRUE.equals(request.esPrincipal())) {
      itemProveedorRepository.findByItemAndEsPrincipalTrue(
          ItemProveedorEntity.ItemTipo.valueOf(request.itemTipo()),
          request.itemId()
      ).ifPresent(current -> {
        current.setEsPrincipal(false);
        itemProveedorRepository.save(current);
      });
    }

    ItemProveedorEntity entity = new ItemProveedorEntity();
    entity.setProveedor(proveedor);
    mapper.applyCreateRequest(entity, request, OffsetDateTime.now());

    ItemProveedorSummary summary = mapper.toSummary(itemProveedorRepository.save(entity));

    auditTrailService.recordChange(
        "ITEM_PROVEEDOR_CREADO",
        "ABASTECIMIENTO",
        "item_proveedor",
        summary.id().toString(),
        "CREAR_ITEM_PROVEEDOR",
        null,
        summary,
        "Alta de item-proveedor.",
        httpRequest
    );

    return summary;
  }

  @Transactional
  public ItemProveedorSummary updatePrincipal(Long id, boolean esPrincipal, HttpServletRequest httpRequest) {
    ItemProveedorEntity entity = itemProveedorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Item-Proveedor no encontrado."));

    if (esPrincipal) {
      itemProveedorRepository.findByItemAndEsPrincipalTrue(entity.getItemTipo(), entity.getItemId())
          .ifPresent(current -> {
            if (!current.getId().equals(id)) {
              current.setEsPrincipal(false);
              itemProveedorRepository.save(current);
            }
          });
    }

    ItemProveedorSummary previous = mapper.toSummary(entity);
    entity.setEsPrincipal(esPrincipal);
    ItemProveedorSummary current = mapper.toSummary(itemProveedorRepository.save(entity));

    auditTrailService.recordChange(
        "ITEM_PROVEEDOR_ACTUALIZADO",
        "ABASTECIMIENTO",
        "item_proveedor",
        entity.getId().toString(),
        "ACTUALIZAR_ITEM_PROVEEDOR",
        previous,
        current,
        "Actualización de item-proveedor.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public void deleteItemProveedor(Long id, HttpServletRequest httpRequest) {
    ItemProveedorEntity entity = itemProveedorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Item-Proveedor no encontrado."));

    auditTrailService.recordChange(
        "ITEM_PROVEEDOR_ELIMINADO",
        "ABASTECIMIENTO",
        "item_proveedor",
        entity.getId().toString(),
        "ELIMINAR_ITEM_PROVEEDOR",
        mapper.toSummary(entity),
        null,
        "Eliminación de item-proveedor.",
        httpRequest
    );

    itemProveedorRepository.delete(entity);
  }
}