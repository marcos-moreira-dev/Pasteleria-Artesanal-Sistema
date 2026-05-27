package com.pasteleria.abastecimiento.application.mapper;

import com.pasteleria.abastecimiento.application.CreateUmedidaRequest;
import com.pasteleria.abastecimiento.application.UmedidaSummary;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity;
import com.pasteleria.common.text.TextSupport;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class UmedidaDtoMapper {

  public void applyCreateRequest(UmedidaEntity entity, CreateUmedidaRequest request, OffsetDateTime now) {
    entity.setCode(request.codigo().trim().toUpperCase());
    entity.setName(request.nombre().trim());
    entity.setAbbreviation(request.abreviatura().trim().toUpperCase());
    entity.setTipo(UmedidaEntity.UmedidaTipo.valueOf(request.tipo()));
    entity.setDecimals((short) request.decimales());
    entity.setActive(true);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);
  }

  public UmedidaSummary toSummary(UmedidaEntity entity) {
    return new UmedidaSummary(
        entity.getId(),
        entity.getCode(),
        entity.getName(),
        entity.getAbbreviation(),
        entity.getTipo(),
        entity.getDecimals(),
        entity.isActive()
    );
  }
}