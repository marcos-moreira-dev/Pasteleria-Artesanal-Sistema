package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pasteleria.abastecimiento.application.mapper.RecetaDtoMapper;
import com.pasteleria.abastecimiento.application.port.DetalleRecetaRepositoryPort;
import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.port.RecetaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RecetaCommandService {

  private final RecetaRepositoryPort recetaRepository;
  private final DetalleRecetaRepositoryPort detalleRecetaRepository;
  private final IngredienteRepositoryPort ingredienteRepository;
  private final RecetaDtoMapper mapper;
  private final AuditTrailService auditTrailService;

  public RecetaCommandService(
      RecetaRepositoryPort recetaRepository,
      DetalleRecetaRepositoryPort detalleRecetaRepository,
      IngredienteRepositoryPort ingredienteRepository,
      RecetaDtoMapper mapper,
      AuditTrailService auditTrailService
  ) {
    this.recetaRepository = recetaRepository;
    this.detalleRecetaRepository = detalleRecetaRepository;
    this.ingredienteRepository = ingredienteRepository;
    this.mapper = mapper;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public RecetaDetailSummary createReceta(CreateRecetaRequest request, UserEntity user, HttpServletRequest httpRequest) {
    OffsetDateTime now = OffsetDateTime.now();

    RecetaEntity entity = new RecetaEntity();
    mapper.applyCreateRequest(entity, request, now, user);

    if (request.productoId() != null) {
      boolean hasOtherActiveRecipe = recetaRepository.existsByProductoIdAndEsActivaTrueAndIdNot(request.productoId(), -1L);
      if (hasOtherActiveRecipe) {
        throw new BusinessRuleException("Ya existe una receta activa para este producto. Desactivala primero.");
      }
    }

    RecetaEntity savedReceta = recetaRepository.save(entity);

    List<DetalleRecetaEntity> detalles = buildDetalles(request.detalles(), savedReceta, now);
    BigDecimal costoEstimado = mapper.calculateCostoEstimado(detalles);
    savedReceta.setCostoEstimado(costoEstimado);
    savedReceta = recetaRepository.save(savedReceta);

    RecetaDetailSummary summary = mapper.toDetailSummary(savedReceta, detalles);

    auditTrailService.recordChange(
        "RECETA_CREADA",
        "ABASTECIMIENTO",
        "receta",
        savedReceta.getId().toString(),
        "CREAR_RECETA",
        null,
        summary,
        "Receta creada.",
        httpRequest
    );

    return summary;
  }

  @Transactional
  public RecetaDetailSummary updateReceta(Long id, UpdateRecetaRequest request, HttpServletRequest httpRequest) {
    RecetaEntity entity = recetaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada."));

    OffsetDateTime now = OffsetDateTime.now();
    mapper.applyUpdateRequest(entity, request, now);

    List<DetalleRecetaEntity> detalles;
    if (request.detalles() != null) {
      detalleRecetaRepository.deleteByRecetaId(id);
      detalles = buildDetallesFromUpdate(request.detalles(), entity, now);
      BigDecimal costoEstimado = mapper.calculateCostoEstimado(detalles);
      entity.setCostoEstimado(costoEstimado);
    } else {
      detalles = detalleRecetaRepository.findByRecetaId(id);
    }

    RecetaEntity savedReceta = recetaRepository.save(entity);

    RecetaDetailSummary previous = mapper.toDetailSummary(entity, detalles);
    RecetaDetailSummary current = mapper.toDetailSummary(savedReceta, detalles);

    auditTrailService.recordChange(
        "RECETA_ACTUALIZADA",
        "ABASTECIMIENTO",
        "receta",
        savedReceta.getId().toString(),
        "ACTUALIZAR_RECETA",
        previous,
        current,
        "Receta actualizada.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public void deleteReceta(Long id, HttpServletRequest httpRequest) {
    RecetaEntity entity = recetaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada."));

    RecetaDetailSummary summary = mapper.toDetailSummary(entity, detalleRecetaRepository.findByRecetaId(id));

    auditTrailService.recordChange(
        "RECETA_ELIMINADA",
        "ABASTECIMIENTO",
        "receta",
        entity.getId().toString(),
        "ELIMINAR_RECETA",
        summary,
        null,
        "Receta eliminada.",
        httpRequest
    );

    recetaRepository.delete(entity);
  }

  @Transactional
  public RecetaDetailSummary toggleActiva(Long id, HttpServletRequest httpRequest) {
    RecetaEntity entity = recetaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada."));

    OffsetDateTime now = OffsetDateTime.now();
    boolean newState = !entity.getEsActiva();

    if (newState && entity.getProducto() != null) {
      boolean hasOtherActiveRecipe = recetaRepository.existsByProductoIdAndEsActivaTrueAndIdNot(entity.getProducto().getId(), id);
      if (hasOtherActiveRecipe) {
        throw new BusinessRuleException("Ya existe otra receta activa para este producto.");
      }
    }

    entity.setEsActiva(newState);
    entity.setUpdatedAt(now);
    RecetaEntity savedReceta = recetaRepository.save(entity);

    List<DetalleRecetaEntity> detalles = detalleRecetaRepository.findByRecetaId(id);
    RecetaDetailSummary summary = mapper.toDetailSummary(savedReceta, detalles);

    auditTrailService.recordChange(
        "RECETA_ACTUALIZADA",
        "ABASTECIMIENTO",
        "receta",
        savedReceta.getId().toString(),
        "TOGGLE_ACTIVA_RECETA",
        null,
        summary,
        newState ? "Receta activada." : "Receta desactivada.",
        httpRequest
    );

    return summary;
  }

  private List<DetalleRecetaEntity> buildDetalles(
      List<CreateRecetaRequest.DetalleRecetaItem> items,
      RecetaEntity receta,
      OffsetDateTime now
  ) {
    List<DetalleRecetaEntity> detalles = new ArrayList<>();
    for (CreateRecetaRequest.DetalleRecetaItem item : items) {
      IngredienteEntity ingrediente = ingredienteRepository.findById(item.ingredienteId())
          .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado con ID: " + item.ingredienteId()));

      DetalleRecetaEntity detalle = new DetalleRecetaEntity();
      detalle.setReceta(receta);
      detalle.setIngrediente(ingrediente);
      detalle.setCantidadBase(item.cantidadBase());
      detalle.setRendimientoPorUnidad(item.rendimientoPorUnidad() != null ? item.rendimientoPorUnidad() : BigDecimal.ONE);
      detalle.setEsParaPorcion(item.esParaPorcion() != null ? item.esParaPorcion() : true);
      detalle.setObservaciones(item.observaciones());
      detalle.setCreatedAt(now);
      detalles.add(detalleRecetaRepository.save(detalle));
    }
    return detalles;
  }

  private List<DetalleRecetaEntity> buildDetallesFromUpdate(
      List<UpdateRecetaRequest.DetalleRecetaItem> items,
      RecetaEntity receta,
      OffsetDateTime now
  ) {
    List<DetalleRecetaEntity> detalles = new ArrayList<>();
    for (UpdateRecetaRequest.DetalleRecetaItem item : items) {
      IngredienteEntity ingrediente = ingredienteRepository.findById(item.ingredienteId())
          .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado con ID: " + item.ingredienteId()));

      DetalleRecetaEntity detalle = new DetalleRecetaEntity();
      detalle.setReceta(receta);
      detalle.setIngrediente(ingrediente);
      detalle.setCantidadBase(item.cantidadBase());
      detalle.setRendimientoPorUnidad(item.rendimientoPorUnidad() != null ? item.rendimientoPorUnidad() : BigDecimal.ONE);
      detalle.setEsParaPorcion(item.esParaPorcion() != null ? item.esParaPorcion() : true);
      detalle.setObservaciones(item.observaciones());
      detalle.setCreatedAt(now);
      detalles.add(detalleRecetaRepository.save(detalle));
    }
    return detalles;
  }
}
