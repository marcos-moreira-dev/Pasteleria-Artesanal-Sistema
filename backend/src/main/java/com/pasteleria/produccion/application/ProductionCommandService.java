package com.pasteleria.produccion.application;

import java.time.OffsetDateTime;
import java.util.Set;

import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.application.NotificationCreateCommand;
import com.pasteleria.notificaciones.application.NotificationPublishService;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.produccion.application.port.ProductionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.application.mapper.ProductionDtoMapper;
import com.pasteleria.usuarios.domain.model.RoleCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controla la maquina de estados de produccion y la refleja en el pedido asociado.
 */
@Service
public class ProductionCommandService {

  private final ProductionRepositoryPort productionRepository;
  private final AuditTrailService auditTrailService;
  private final NotificationPublishService notificationPublishService;
  private final ProductionDtoMapper productionDtoMapper;

  public ProductionCommandService(
      ProductionRepositoryPort productionRepository,
      AuditTrailService auditTrailService,
      NotificationPublishService notificationPublishService,
      ProductionDtoMapper productionDtoMapper
  ) {
    this.productionRepository = productionRepository;
    this.auditTrailService = auditTrailService;
    this.notificationPublishService = notificationPublishService;
    this.productionDtoMapper = productionDtoMapper;
  }

  /**
   * Mueve el frente de produccion y sincroniza el pedido para que ventas vea el mismo estado.
   */
  @Transactional
  public ProductionSummary updateProductionStatus(
      Long productionId,
      UpdateProductionStatusRequest request,
      HttpServletRequest httpRequest
  ) {
    ProductionEntity production = productionRepository.findById(productionId)
        .orElseThrow(() -> new ResourceNotFoundException("El registro de produccion indicado no existe."));

    validateStatusTransition(production.getStatus(), request.status());
    ProductionSummary previous = productionDtoMapper.toSummary(production);

    production.setStatus(request.status());
    production.setUpdatedAt(OffsetDateTime.now());

    if (request.status() == ProductionStatus.PREPARACION && production.getStartedAt() == null) {
      // Preparacion real en cocina implica que el pedido ya no puede seguir "registrado".
      production.setStartedAt(OffsetDateTime.now());
      production.getOrder().setStatus(OrderStatus.EN_PREPARACION);
      production.getOrder().setUpdatedAt(OffsetDateTime.now());
    }

    if (request.status() == ProductionStatus.DECORACION || request.status() == ProductionStatus.EMPAQUE) {
      if (production.getStartedAt() == null) {
        production.setStartedAt(OffsetDateTime.now());
      }
      production.getOrder().setStatus(OrderStatus.EN_PREPARACION);
      production.getOrder().setUpdatedAt(OffsetDateTime.now());
    }

    if (request.status() == ProductionStatus.FINALIZADO) {
      if (production.getStartedAt() == null) {
        production.setStartedAt(OffsetDateTime.now());
      }
      // Cuando produccion termina, el pedido queda listo para entrega sin doble carga manual.
      production.setFinishedAt(OffsetDateTime.now());
      production.getOrder().setStatus(OrderStatus.LISTO);
      production.getOrder().setUpdatedAt(OffsetDateTime.now());
    }

    ProductionSummary current = productionDtoMapper.toSummary(production);
    auditTrailService.recordChange(
        "PRODUCCION_ESTADO_ACTUALIZADO",
        "PRODUCCION",
        "produccion",
        production.getId().toString(),
        "ACTUALIZAR_ESTADO_PRODUCCION",
        previous,
        current,
        request.reason(),
        httpRequest
    );
    notificationPublishService.notifyRoles(
        Set.of(RoleCode.ADMIN, RoleCode.ATENCION, RoleCode.PRODUCCION),
        new NotificationCreateCommand(
            "PRODUCCION_ESTADO",
            "Producción " + production.getOrder().getCode() + " en movimiento",
            "El pedido " + production.getOrder().getCode() + " pasó a " + production.getStatus().name() + ".",
            "PRODUCCION",
            "produccion",
            production.getId().toString(),
            production.getPriority() == com.pasteleria.produccion.domain.model.ProductionPriority.URGENTE ? NotificationPriority.ALTA : NotificationPriority.MEDIA,
            "{\"status\":\"" + production.getStatus().name() + "\"}"
        )
    );

    return current;
  }

  private void validateStatusTransition(ProductionStatus currentStatus, ProductionStatus targetStatus) {
    if (currentStatus == targetStatus) {
      return;
    }

    // Validar transiciones permitidas (avance y retroceso)
    boolean valid = switch (currentStatus) {
      case PENDIENTE -> targetStatus == ProductionStatus.PREPARACION;
      case PREPARACION -> targetStatus == ProductionStatus.PENDIENTE || targetStatus == ProductionStatus.DECORACION;
      case DECORACION -> targetStatus == ProductionStatus.PREPARACION || targetStatus == ProductionStatus.EMPAQUE;
      case EMPAQUE -> targetStatus == ProductionStatus.DECORACION || targetStatus == ProductionStatus.FINALIZADO;
      case FINALIZADO -> targetStatus == ProductionStatus.EMPAQUE;
    };

    if (!valid) {
      throw new IllegalStateException("La transicion de estado de produccion no es valida.");
    }
  }
}


