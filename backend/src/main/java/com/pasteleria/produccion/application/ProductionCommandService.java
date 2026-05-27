package com.pasteleria.produccion.application;

import java.time.OffsetDateTime;
import java.util.Set;

import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.application.NotificationCreateCommand;
import com.pasteleria.notificaciones.application.NotificationPublishService;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.produccion.application.port.ProductionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.domain.service.ProductionStateMachine;
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
  private static final String DEFAULT_SUCURSAL_ID = "MATRIZ";

  private final NotificationPublishService notificationPublishService;
  private final ProductionDtoMapper productionDtoMapper;
  private final OperacionAutorizacionService autorizacionService;
  private final ProductionMaterialService productionMaterialService;
  private final ProductionStateMachine productionStateMachine = new ProductionStateMachine();

  public ProductionCommandService(
      ProductionRepositoryPort productionRepository,
      AuditTrailService auditTrailService,
      NotificationPublishService notificationPublishService,
      ProductionDtoMapper productionDtoMapper,
      OperacionAutorizacionService autorizacionService,
      ProductionMaterialService productionMaterialService
  ) {
    this.productionRepository = productionRepository;
    this.auditTrailService = auditTrailService;
    this.notificationPublishService = notificationPublishService;
    this.productionDtoMapper = productionDtoMapper;
    this.autorizacionService = autorizacionService;
    this.productionMaterialService = productionMaterialService;
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
    autorizacionService.exigirPermisoSucursal(DEFAULT_SUCURSAL_ID, Permisos.PRODUCCION_OPERAR);
    ProductionEntity production = productionRepository.findById(productionId)
        .orElseThrow(() -> new ResourceNotFoundException("El registro de produccion indicado no existe."));

    productionStateMachine.assertTransitionAllowed(production, request.status());
    ProductionSummary previous = productionDtoMapper.toSummary(production);

    if (request.status() == ProductionStatus.FINALIZADO) {
      productionMaterialService.generarConsumosYLotesSiFaltan(production, httpRequest);
    }

    applyProductionTransition(production, request.status());

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

  private void applyProductionTransition(ProductionEntity production, ProductionStatus targetStatus) {
    OffsetDateTime now = OffsetDateTime.now();
    production.setStatus(targetStatus);
    production.setUpdatedAt(now);

    if (targetStatus == ProductionStatus.PREPARACION
        || targetStatus == ProductionStatus.DECORACION
        || targetStatus == ProductionStatus.EMPAQUE
        || targetStatus == ProductionStatus.FINALIZADO) {
      if (production.getStartedAt() == null) {
        production.setStartedAt(now);
      }
      production.getOrder().setStatus(OrderStatus.EN_PREPARACION);
      production.getOrder().setUpdatedAt(now);
    }

    if (targetStatus == ProductionStatus.FINALIZADO) {
      production.setFinishedAt(now);
      production.getOrder().setStatus(OrderStatus.LISTO);
      production.getOrder().setUpdatedAt(now);
    }

    if (targetStatus == ProductionStatus.CANCELADO) {
      production.getOrder().setStatus(OrderStatus.CANCELADO);
      production.getOrder().setUpdatedAt(now);
    }
  }
}
