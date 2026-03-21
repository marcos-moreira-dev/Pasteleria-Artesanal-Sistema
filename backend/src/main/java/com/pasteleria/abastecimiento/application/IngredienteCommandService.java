package com.pasteleria.abastecimiento.application;

import java.time.OffsetDateTime;

import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.port.UmedidaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity;
import com.pasteleria.abastecimiento.application.mapper.IngredienteDtoMapper;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class IngredienteCommandService {

  private final IngredienteRepositoryPort ingredienteRepository;
  private final UmedidaRepositoryPort umedidaRepository;
  private final IngredienteDtoMapper mapper;
  private final AuditTrailService auditTrailService;

  public IngredienteCommandService(
      IngredienteRepositoryPort ingredienteRepository,
      UmedidaRepositoryPort umedidaRepository,
      IngredienteDtoMapper mapper,
      AuditTrailService auditTrailService
  ) {
    this.ingredienteRepository = ingredienteRepository;
    this.umedidaRepository = umedidaRepository;
    this.mapper = mapper;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public IngredienteSummary createIngrediente(CreateIngredienteRequest request, HttpServletRequest httpRequest) {
    UmedidaEntity umedida = umedidaRepository.findById(request.umedidaId())
        .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada."));

    IngredienteEntity entity = new IngredienteEntity();
    entity.setUmedida(umedida);
    mapper.applyCreateRequest(entity, request, OffsetDateTime.now());

    IngredienteSummary summary = mapper.toSummary(ingredienteRepository.save(entity));

    auditTrailService.recordChange(
        "INGREDIENTE_CREADO",
        "ABASTECIMIENTO",
        "ingrediente",
        summary.id().toString(),
        "CREAR_INGREDIENTE",
        null,
        summary,
        "Alta de ingrediente.",
        httpRequest
    );

    return summary;
  }

  @Transactional
  public IngredienteSummary updateIngrediente(Long id, UpdateIngredienteRequest request, HttpServletRequest httpRequest) {
    IngredienteEntity entity = ingredienteRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado."));

    if (request.umedidaId() != null) {
      UmedidaEntity umedida = umedidaRepository.findById(request.umedidaId())
          .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada."));
      entity.setUmedida(umedida);
    }

    IngredienteSummary previous = mapper.toSummary(entity);
    mapper.applyUpdateRequest(entity, request, OffsetDateTime.now());
    IngredienteSummary current = mapper.toSummary(ingredienteRepository.save(entity));

    auditTrailService.recordChange(
        "INGREDIENTE_ACTUALIZADO",
        "ABASTECIMIENTO",
        "ingrediente",
        entity.getId().toString(),
        "ACTUALIZAR_INGREDIENTE",
        previous,
        current,
        "Actualización de ingrediente.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public void deleteIngrediente(Long id, HttpServletRequest httpRequest) {
    IngredienteEntity entity = ingredienteRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado."));

    auditTrailService.recordChange(
        "INGREDIENTE_ELIMINADO",
        "ABASTECIMIENTO",
        "ingrediente",
        entity.getId().toString(),
        "ELIMINAR_INGREDIENTE",
        mapper.toSummary(entity),
        null,
        "Eliminación de ingrediente.",
        httpRequest
    );

    ingredienteRepository.delete(entity);
  }
}