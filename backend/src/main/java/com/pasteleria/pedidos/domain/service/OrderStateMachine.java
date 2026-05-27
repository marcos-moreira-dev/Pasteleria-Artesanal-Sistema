package com.pasteleria.pedidos.domain.service;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

/**
 * Maquina de estados del pedido administrativo de pasteleria.
 *
 * <p>Esta clase concentra las reglas de avance/cancelacion para evitar que
 * controladores o servicios actualicen estados con ifs dispersos. En T12 se
 * mantiene compatibilidad con los estados V1 usados por Angular, pero se
 * endurece la regla principal: un pedido no puede marcarse LISTO/ENTREGADO si
 * su produccion asociada no esta finalizada.</p>
 */
public class OrderStateMachine {

  public void assertTransitionAllowed(OrderEntity order, OrderStatus targetStatus) {
    OrderStatus currentStatus = order.getStatus();
    if (currentStatus == targetStatus) {
      return;
    }

    boolean valid = switch (currentStatus) {
      case REGISTRADO -> targetStatus == OrderStatus.EN_PREPARACION || targetStatus == OrderStatus.CANCELADO;
      case EN_PREPARACION -> targetStatus == OrderStatus.LISTO || targetStatus == OrderStatus.CANCELADO;
      case LISTO -> targetStatus == OrderStatus.ENTREGADO || targetStatus == OrderStatus.CANCELADO;
      case ENTREGADO, CANCELADO -> false;
    };

    if (!valid) {
      throw new BusinessRuleException(
          "La transicion del pedido de " + currentStatus + " a " + targetStatus + " no es valida."
      );
    }

    if (targetStatus == OrderStatus.LISTO || targetStatus == OrderStatus.ENTREGADO) {
      requireProductionFinished(order, targetStatus);
    }
  }

  public boolean isTerminal(OrderStatus status) {
    return status == OrderStatus.ENTREGADO || status == OrderStatus.CANCELADO;
  }

  private void requireProductionFinished(OrderEntity order, OrderStatus targetStatus) {
    ProductionEntity production = order.getProduction();
    if (production == null) {
      return;
    }
    if (production.getStatus() != ProductionStatus.FINALIZADO) {
      throw new BusinessRuleException(
          "No se puede marcar el pedido como " + targetStatus
              + " porque la produccion asociada no esta finalizada."
      );
    }
  }
}
