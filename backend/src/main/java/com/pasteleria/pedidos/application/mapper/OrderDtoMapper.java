package com.pasteleria.pedidos.application.mapper;

import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderDetailEntity;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.pedidos.application.OrderDetailSummary;
import com.pasteleria.pedidos.application.OrderSummary;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import org.springframework.stereotype.Component;

/**
 * Traduce la vista operativa del pedido a DTOs listos para el panel
 * administrativo y futuros contratos públicos.
 */
@Component
public class OrderDtoMapper {

  /**
   * Compacta el agregado de pedido en una respuesta apta para operación y lectura.
   */
  public OrderSummary toSummary(OrderEntity order) {
    ProductionEntity production = order.getProduction();

    return new OrderSummary(
        order.getId(),
        order.getCode(),
        order.getClient().getId(),
        order.getClient().getFullName(),
        order.getQuotation() != null ? order.getQuotation().getId() : null,
        order.getStatus(),
        order.getPriority(),
        order.getOrigin(),
        order.getOrderDate(),
        order.getEstimatedDeliveryAt(),
        order.getActualDeliveryAt(),
        order.getNotes(),
        order.getEstimatedTotal(),
        production != null ? production.getStatus() : null,
        production != null ? production.getPriority() : null,
        order.getDetails().stream()
            .map(this::toDetailSummary)
            .toList()
    );
  }

  private OrderDetailSummary toDetailSummary(OrderDetailEntity detail) {
    return new OrderDetailSummary(
        detail.getId(),
        detail.getProduct().getId(),
        detail.getItemDescription(),
        detail.getQuantity(),
        detail.getUnitPrice(),
        detail.getSubtotal(),
        detail.getNotes()
    );
  }
}


