package com.pasteleria.pedidos.domain.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import org.junit.jupiter.api.Test;

class OrderStateMachineTest {

  private final OrderStateMachine stateMachine = new OrderStateMachine();

  @Test
  void shouldAllowLinearOrderFlowWhenProductionIsFinishedForReadyState() {
    OrderEntity order = order(OrderStatus.EN_PREPARACION, ProductionStatus.FINALIZADO);

    assertThatCode(() -> stateMachine.assertTransitionAllowed(order, OrderStatus.LISTO))
        .doesNotThrowAnyException();
  }

  @Test
  void shouldRejectReadyStateWhenProductionIsNotFinished() {
    OrderEntity order = order(OrderStatus.EN_PREPARACION, ProductionStatus.PREPARACION);

    assertThatThrownBy(() -> stateMachine.assertTransitionAllowed(order, OrderStatus.LISTO))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("produccion asociada no esta finalizada");
  }

  @Test
  void shouldRejectChangesFromTerminalOrder() {
    OrderEntity delivered = order(OrderStatus.ENTREGADO, ProductionStatus.FINALIZADO);

    assertThatThrownBy(() -> stateMachine.assertTransitionAllowed(delivered, OrderStatus.CANCELADO))
        .isInstanceOf(BusinessRuleException.class);
  }

  private OrderEntity order(OrderStatus orderStatus, ProductionStatus productionStatus) {
    OrderEntity order = new OrderEntity();
    order.setStatus(orderStatus);
    ProductionEntity production = new ProductionEntity();
    production.setOrder(order);
    production.setStatus(productionStatus);
    order.setProduction(production);
    return order;
  }
}
