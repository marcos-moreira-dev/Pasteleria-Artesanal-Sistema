package com.pasteleria.produccion.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import org.junit.jupiter.api.Test;

class ProductionStateMachineTest {

  private final ProductionStateMachine stateMachine = new ProductionStateMachine();

  @Test
  void shouldAllowLinearProductionFlow() {
    ProductionEntity production = production(ProductionStatus.PENDIENTE, OrderStatus.REGISTRADO);

    assertThatCode(() -> stateMachine.assertTransitionAllowed(production, ProductionStatus.PREPARACION))
        .doesNotThrowAnyException();
  }

  @Test
  void shouldRejectBackwardProductionFlow() {
    ProductionEntity production = production(ProductionStatus.DECORACION, OrderStatus.EN_PREPARACION);

    assertThatThrownBy(() -> stateMachine.assertTransitionAllowed(production, ProductionStatus.PREPARACION))
        .isInstanceOf(BusinessRuleException.class);
  }

  @Test
  void shouldRejectAdvancingCancelledOrderProduction() {
    ProductionEntity production = production(ProductionStatus.PENDIENTE, OrderStatus.CANCELADO);

    assertThatThrownBy(() -> stateMachine.assertTransitionAllowed(production, ProductionStatus.PREPARACION))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("pedido cancelado");
  }

  @Test
  void shouldTreatCancelledProductionAsInactive() {
    assertThat(stateMachine.isActive(ProductionStatus.CANCELADO)).isFalse();
  }

  private ProductionEntity production(ProductionStatus status, OrderStatus orderStatus) {
    OrderEntity order = new OrderEntity();
    order.setStatus(orderStatus);
    ProductionEntity production = new ProductionEntity();
    production.setStatus(status);
    production.setOrder(order);
    order.setProduction(production);
    return production;
  }
}
