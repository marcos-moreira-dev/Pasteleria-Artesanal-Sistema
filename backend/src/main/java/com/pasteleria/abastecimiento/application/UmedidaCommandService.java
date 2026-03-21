package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.application.port.UmedidaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity;
import com.pasteleria.abastecimiento.application.mapper.UmedidaDtoMapper;
import com.pasteleria.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class UmedidaCommandService {

  private final UmedidaRepositoryPort repository;
  private final UmedidaDtoMapper mapper;

  public UmedidaCommandService(UmedidaRepositoryPort repository, UmedidaDtoMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UmedidaSummary createUmedida(CreateUmedidaRequest request) {
    UmedidaEntity entity = new UmedidaEntity();
    mapper.applyCreateRequest(entity, request, OffsetDateTime.now());
    return mapper.toSummary(repository.save(entity));
  }

  @Transactional
  public UmedidaSummary updateUmedida(Long id, CreateUmedidaRequest request) {
    UmedidaEntity entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada."));
    mapper.applyCreateRequest(entity, request, OffsetDateTime.now());
    return mapper.toSummary(repository.save(entity));
  }
}