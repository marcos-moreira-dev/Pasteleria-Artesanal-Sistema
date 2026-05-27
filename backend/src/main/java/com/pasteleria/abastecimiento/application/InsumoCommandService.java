package com.pasteleria.abastecimiento.application;

import java.time.OffsetDateTime;

import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.application.port.UmedidaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity;
import com.pasteleria.abastecimiento.application.mapper.InsumoDtoMapper;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class InsumoCommandService {

  private final InsumoRepositoryPort insumoRepository;
  private final UmedidaRepositoryPort umedidaRepository;
  private final InsumoDtoMapper mapper;
  private final AuditTrailService auditTrailService;

  public InsumoCommandService(
      InsumoRepositoryPort insumoRepository,
      UmedidaRepositoryPort umedidaRepository,
      InsumoDtoMapper mapper,
      AuditTrailService auditTrailService
  ) {
    this.insumoRepository = insumoRepository;
    this.umedidaRepository = umedidaRepository;
    this.mapper = mapper;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public InsumoSummary createInsumo(CreateInsumoRequest request, HttpServletRequest httpRequest) {
    UmedidaEntity umedida = umedidaRepository.findById(request.umedidaId())
        .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada."));

    InsumoEntity entity = new InsumoEntity();
    entity.setUmedida(umedida);
    mapper.applyCreateRequest(entity, request, OffsetDateTime.now());

    InsumoSummary summary = mapper.toSummary(insumoRepository.save(entity));

    auditTrailService.recordChange(
        "INSUMO_CREADO",
        "ABASTECIMIENTO",
        "insumo",
        summary.id().toString(),
        "CREAR_INSUMO",
        null,
        summary,
        "Alta de insumo.",
        httpRequest
    );

    return summary;
  }

  @Transactional
  public InsumoSummary updateInsumo(Long id, UpdateInsumoRequest request, HttpServletRequest httpRequest) {
    InsumoEntity entity = insumoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado."));

    if (request.umedidaId() != null) {
      UmedidaEntity umedida = umedidaRepository.findById(request.umedidaId())
          .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada."));
      entity.setUmedida(umedida);
    }

    InsumoSummary previous = mapper.toSummary(entity);
    mapper.applyUpdateRequest(entity, request, OffsetDateTime.now());
    InsumoSummary current = mapper.toSummary(insumoRepository.save(entity));

    auditTrailService.recordChange(
        "INSUMO_ACTUALIZADO",
        "ABASTECIMIENTO",
        "insumo",
        entity.getId().toString(),
        "ACTUALIZAR_INSUMO",
        previous,
        current,
        "Actualización de insumo.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public void deleteInsumo(Long id, HttpServletRequest httpRequest) {
    InsumoEntity entity = insumoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado."));

    auditTrailService.recordChange(
        "INSUMO_ELIMINADO",
        "ABASTECIMIENTO",
        "insumo",
        entity.getId().toString(),
        "ELIMINAR_INSUMO",
        mapper.toSummary(entity),
        null,
        "Eliminación de insumo.",
        httpRequest
    );

    insumoRepository.delete(entity);
  }
}