package com.pasteleria.pedidos.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.pedidos.domain.model.OrderOrigin;
import com.pasteleria.pedidos.domain.model.OrderPriority;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;

public record OrderSummary(
    Long id,
    String code,
    Long clientId,
    String clientName,
    Long quotationId,
    OrderStatus status,
    OrderPriority priority,
    OrderOrigin origin,
    OffsetDateTime orderDate,
    OffsetDateTime estimatedDeliveryAt,
    OffsetDateTime actualDeliveryAt,
    String notes,
    BigDecimal estimatedTotal,
    ProductionStatus productionStatus,
    ProductionPriority productionPriority,
    List<OrderDetailSummary> details
) {
}


