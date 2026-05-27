package com.pasteleria.abastecimiento.application.mapper;

import com.pasteleria.abastecimiento.application.CreateProveedorRequest;
import com.pasteleria.abastecimiento.application.ProveedorSummary;
import com.pasteleria.abastecimiento.application.UpdateProveedorRequest;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;
import com.pasteleria.common.text.TextSupport;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class ProveedorDtoMapper {

  public void applyCreateRequest(ProveedorEntity entity, CreateProveedorRequest request, OffsetDateTime now) {
    entity.setCode(request.codigo().trim().toUpperCase());
    entity.setName(request.nombre().trim());
    entity.setPhone(TextSupport.trimToNull(request.telefono()));
    entity.setEmail(TextSupport.trimToNull(request.correo()));
    entity.setAddress(TextSupport.trimToNull(request.direccion()));
    entity.setObservations(TextSupport.trimToNull(request.observaciones()));
    entity.setActive(true);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);
  }

  public void applyUpdateRequest(ProveedorEntity entity, UpdateProveedorRequest request, OffsetDateTime now) {
    entity.setCode(request.codigo().trim().toUpperCase());
    entity.setName(request.nombre().trim());
    entity.setPhone(TextSupport.trimToNull(request.telefono()));
    entity.setEmail(TextSupport.trimToNull(request.correo()));
    entity.setAddress(TextSupport.trimToNull(request.direccion()));
    entity.setObservations(TextSupport.trimToNull(request.observaciones()));
    entity.setUpdatedAt(now);
  }

  public ProveedorSummary toSummary(ProveedorEntity entity) {
    return new ProveedorSummary(
        entity.getId(),
        entity.getCode(),
        entity.getName(),
        entity.getPhone(),
        entity.getEmail(),
        entity.getAddress(),
        entity.getObservations(),
        entity.isActive(),
        entity.getCreatedAt()
    );
  }
}