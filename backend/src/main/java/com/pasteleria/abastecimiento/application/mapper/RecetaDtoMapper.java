package com.pasteleria.abastecimiento.application.mapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.abastecimiento.application.CreateRecetaRequest;
import com.pasteleria.abastecimiento.application.DetalleRecetaSummary;
import com.pasteleria.abastecimiento.application.RecetaDetailSummary;
import com.pasteleria.abastecimiento.application.RecetaSummary;
import com.pasteleria.abastecimiento.application.UpdateRecetaRequest;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Component;

@Component
public class RecetaDtoMapper {

  private final DetalleRecetaDtoMapper detalleRecetaDtoMapper;

  public RecetaDtoMapper(DetalleRecetaDtoMapper detalleRecetaDtoMapper) {
    this.detalleRecetaDtoMapper = detalleRecetaDtoMapper;
  }

  public RecetaSummary toSummary(RecetaEntity entity) {
    return new RecetaSummary(
        entity.getId(),
        entity.getProducto() != null ? entity.getProducto().getId() : null,
        entity.getProducto() != null ? entity.getProducto().getName() : null,
        entity.getNombre(),
        entity.getRendimientoBase(),
        entity.getCostoEstimado(),
        entity.getObservaciones(),
        entity.getEsActiva(),
        entity.getCreatedAt(),
        entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null,
        entity.getCreatedBy() != null ? entity.getCreatedBy().getUsername() : null
    );
  }

  public RecetaDetailSummary toDetailSummary(RecetaEntity entity, List<DetalleRecetaEntity> detalles) {
    List<DetalleRecetaSummary> detalleSummaries = detalles.stream()
        .map(detalleRecetaDtoMapper::toSummary)
        .toList();

    return new RecetaDetailSummary(
        entity.getId(),
        entity.getProducto() != null ? entity.getProducto().getId() : null,
        entity.getProducto() != null ? entity.getProducto().getName() : null,
        entity.getNombre(),
        entity.getRendimientoBase(),
        entity.getCostoEstimado(),
        entity.getObservaciones(),
        entity.getEsActiva(),
        entity.getCreatedAt(),
        detalleSummaries
    );
  }

  public void applyCreateRequest(RecetaEntity entity, CreateRecetaRequest request, OffsetDateTime now, UserEntity user) {
    entity.setNombre(request.nombre().trim());
    entity.setRendimientoBase(request.rendimientoBase() != null ? request.rendimientoBase() : BigDecimal.ONE);
    entity.setObservaciones(request.observaciones());
    entity.setEsActiva(request.esActiva() != null ? request.esActiva() : false);
    entity.setCreatedBy(user);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);
  }

  public void applyUpdateRequest(RecetaEntity entity, UpdateRecetaRequest request, OffsetDateTime now) {
    if (request.nombre() != null) {
      entity.setNombre(request.nombre().trim());
    }
    if (request.rendimientoBase() != null) {
      entity.setRendimientoBase(request.rendimientoBase());
    }
    if (request.observaciones() != null) {
      entity.setObservaciones(request.observaciones());
    }
    if (request.esActiva() != null) {
      entity.setEsActiva(request.esActiva());
    }
    entity.setUpdatedAt(now);
  }

  public BigDecimal calculateCostoEstimado(List<DetalleRecetaEntity> detalles) {
    if (detalles == null || detalles.isEmpty()) {
      return BigDecimal.ZERO;
    }
    return detalles.stream()
        .map(d -> {
          BigDecimal cantidad = d.getCantidadBase();
          BigDecimal costo = d.getIngrediente() != null && d.getIngrediente().getCostoReferencial() != null
              ? d.getIngrediente().getCostoReferencial()
              : BigDecimal.ZERO;
          return cantidad.multiply(costo);
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
