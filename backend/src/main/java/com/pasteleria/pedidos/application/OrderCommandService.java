package com.pasteleria.pedidos.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.text.TextSupport;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationEntity;
import com.pasteleria.cotizaciones.application.port.QuotationRepositoryPort;
import com.pasteleria.cotizaciones.domain.model.QuotationStatus;
import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.application.NotificationCreateCommand;
import com.pasteleria.notificaciones.application.NotificationPublishService;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderDetailEntity;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.pedidos.application.port.OrderRepositoryPort;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.application.mapper.OrderDtoMapper;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.usuarios.domain.model.RoleCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Orquesta la vida operativa del pedido y su sincronizacion inicial con produccion.
 */
@Service
public class OrderCommandService {

  private final OrderRepositoryPort orderRepository;
  private final ClientRepositoryPort clientRepository;
  private final ProductRepositoryPort productRepository;
  private final QuotationRepositoryPort quotationRepository;
  private final AuditTrailService auditTrailService;
  private final NotificationPublishService notificationPublishService;
  private final OrderDtoMapper orderDtoMapper;

  public OrderCommandService(
      OrderRepositoryPort orderRepository,
      ClientRepositoryPort clientRepository,
      ProductRepositoryPort productRepository,
      QuotationRepositoryPort quotationRepository,
      AuditTrailService auditTrailService,
      NotificationPublishService notificationPublishService,
      OrderDtoMapper orderDtoMapper
  ) {
    this.orderRepository = orderRepository;
    this.clientRepository = clientRepository;
    this.productRepository = productRepository;
    this.quotationRepository = quotationRepository;
    this.auditTrailService = auditTrailService;
    this.notificationPublishService = notificationPublishService;
    this.orderDtoMapper = orderDtoMapper;
  }

  /**
   * Registra un pedido y crea automaticamente su frente de produccion.
   */
  @Transactional
  public OrderSummary createOrder(CreateOrderRequest request, HttpServletRequest httpRequest) {
    ClientEntity client = clientRepository.findById(request.clientId())
        .orElseThrow(() -> new ResourceNotFoundException("El cliente indicado no existe."));

    QuotationEntity quotation = null;
    if (request.quotationId() != null) {
      quotation = quotationRepository.findByIdAndStatus(request.quotationId(), QuotationStatus.APROBADA)
          .orElseThrow(() -> new ResourceNotFoundException("La cotizacion indicada no existe o no esta aprobada."));
      quotation.setStatus(QuotationStatus.CONVERTIDA);
      quotation.setUpdatedAt(OffsetDateTime.now());
    }

    OffsetDateTime now = OffsetDateTime.now();
    OrderEntity order = new OrderEntity();
    order.setClient(client);
    order.setQuotation(quotation);
    order.setCode("PED-" + System.currentTimeMillis());
    order.setOrderDate(now);
    order.setEstimatedDeliveryAt(request.estimatedDeliveryAt());
    order.setStatus(OrderStatus.REGISTRADO);
    order.setPriority(request.priority());
    order.setOrigin(request.origin());
    order.setNotes(TextSupport.trimToNull(request.notes()));
    order.setCreatedAt(now);
    order.setUpdatedAt(now);

    BigDecimal total = BigDecimal.ZERO;
    for (CreateOrderDetailRequest detailRequest : request.details()) {
      ProductEntity product = productRepository.findById(detailRequest.productId())
          .orElseThrow(() -> new ResourceNotFoundException("Uno de los productos indicados no existe."));

      OrderDetailEntity detail = new OrderDetailEntity();
      detail.setOrder(order);
      detail.setProduct(product);
      detail.setItemDescription(
          detailRequest.itemDescription() != null && !detailRequest.itemDescription().isBlank()
              ? detailRequest.itemDescription().trim()
              : product.getName()
      );
      detail.setQuantity(detailRequest.quantity());
      detail.setUnitPrice(detailRequest.unitPrice());
      detail.setSubtotal(detailRequest.unitPrice().multiply(BigDecimal.valueOf(detailRequest.quantity())));
      detail.setNotes(TextSupport.trimToNull(detailRequest.notes()));
      detail.setCreatedAt(now);

      order.getDetails().add(detail);
      total = total.add(detail.getSubtotal());
    }

    order.setEstimatedTotal(total);

    // Produccion nace con el pedido para evitar trabajos huerfanos entre ventas y cocina.
    ProductionEntity production = new ProductionEntity();
    production.setOrder(order);
    production.setStatus(ProductionStatus.PENDIENTE);
    production.setPriority(
        request.priority() == com.pasteleria.pedidos.domain.model.OrderPriority.URGENTE
            ? ProductionPriority.URGENTE
            : ProductionPriority.NORMAL
    );
    production.setCreatedAt(now);
    production.setUpdatedAt(now);
    production.setProductionNotes("Produccion creada automaticamente con el registro del pedido.");
    order.setProduction(production);

    OrderEntity savedOrder = orderRepository.save(order);
    OrderSummary summary = orderDtoMapper.toSummary(savedOrder);
    auditTrailService.recordChange(
        "PEDIDO_CREADO",
        "PEDIDOS",
        "pedido",
        savedOrder.getId().toString(),
        "CREAR_PEDIDO",
        null,
        summary,
        "Registro inicial del pedido.",
        httpRequest
    );
    notificationPublishService.notifyRoles(
        Set.of(RoleCode.ADMIN, RoleCode.ATENCION, RoleCode.PRODUCCION),
        new NotificationCreateCommand(
            "PEDIDO_CREADO",
            "Nuevo pedido en tablero",
            "Se registró el pedido " + savedOrder.getCode() + " para " + client.getFullName() + ".",
            "PEDIDOS",
            "pedido",
            savedOrder.getId().toString(),
            request.priority() == com.pasteleria.pedidos.domain.model.OrderPriority.URGENTE ? NotificationPriority.ALTA : NotificationPriority.MEDIA,
            "{\"status\":\"" + savedOrder.getStatus().name() + "\"}"
        )
    );
    return summary;
  }

  /**
   * Actualiza estado de pedido respetando la maquina de estados operativa.
   */
  @Transactional
  public OrderSummary updateOrderStatus(Long orderId, UpdateOrderStatusRequest request, HttpServletRequest httpRequest) {
    OrderEntity order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("El pedido indicado no existe."));

    validateStatusTransition(order.getStatus(), request.status());
    OrderSummary previous = orderDtoMapper.toSummary(order);

    order.setStatus(request.status());
    order.setUpdatedAt(OffsetDateTime.now());

    if (request.status() == OrderStatus.ENTREGADO) {
      order.setActualDeliveryAt(OffsetDateTime.now());
    }

    OrderSummary current = orderDtoMapper.toSummary(order);
    auditTrailService.recordChange(
        "PEDIDO_ESTADO_ACTUALIZADO",
        "PEDIDOS",
        "pedido",
        order.getId().toString(),
        "ACTUALIZAR_ESTADO_PEDIDO",
        previous,
        current,
        request.reason(),
        httpRequest
    );
    notificationPublishService.notifyRoles(
        Set.of(RoleCode.ADMIN, RoleCode.ATENCION, RoleCode.PRODUCCION),
        new NotificationCreateCommand(
            "PEDIDO_ESTADO",
            "Pedido " + order.getCode() + " actualizado",
            "El pedido " + order.getCode() + " ahora está en " + request.status().name() + ".",
            "PEDIDOS",
            "pedido",
            order.getId().toString(),
            request.status() == OrderStatus.CANCELADO ? NotificationPriority.ALTA : NotificationPriority.MEDIA,
            "{\"status\":\"" + request.status().name() + "\"}"
        )
    );

    return current;
  }

  /**
   * Elimina pedidos cancelados para limpiar el tablero sin perder control previo del flujo.
   */
  @Transactional
  public void deleteOrder(Long orderId, HttpServletRequest httpRequest) {
    OrderEntity order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("El pedido indicado no existe."));

    if (order.getStatus() != OrderStatus.CANCELADO) {
      throw new BusinessRuleException("Solo se pueden eliminar pedidos previamente cancelados.");
    }

    if (order.getQuotation() != null && order.getQuotation().getStatus() == QuotationStatus.CONVERTIDA) {
      order.getQuotation().setStatus(QuotationStatus.APROBADA);
      order.getQuotation().setUpdatedAt(OffsetDateTime.now());
    }

    auditTrailService.recordChange(
        "PEDIDO_ELIMINADO",
        "PEDIDOS",
        "pedido",
        order.getId().toString(),
        "ELIMINAR_PEDIDO",
        orderDtoMapper.toSummary(order),
        null,
        "Depuracion administrativa posterior a cancelacion.",
        httpRequest
    );

    orderRepository.delete(order);
  }

  private void validateStatusTransition(OrderStatus currentStatus, OrderStatus targetStatus) {
    if (currentStatus == targetStatus) {
      return;
    }

    // Se restringen saltos para que la operacion no pueda entregar o cancelar desde estados incoherentes.
    boolean valid = switch (currentStatus) {
      case REGISTRADO -> targetStatus == OrderStatus.EN_PREPARACION || targetStatus == OrderStatus.CANCELADO;
      case EN_PREPARACION -> targetStatus == OrderStatus.LISTO || targetStatus == OrderStatus.CANCELADO;
      case LISTO -> targetStatus == OrderStatus.ENTREGADO || targetStatus == OrderStatus.CANCELADO;
      case ENTREGADO, CANCELADO -> false;
    };

    if (!valid) {
      throw new IllegalStateException("La transicion de estado del pedido no es valida.");
    }
  }
}


