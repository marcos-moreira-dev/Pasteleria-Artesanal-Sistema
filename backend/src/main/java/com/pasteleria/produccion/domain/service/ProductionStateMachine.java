package com.pasteleria.produccion.domain.service;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

/**
 * Maquina de estados de produccion/obrador.
 *
 * <p>Conserva los estados visibles V1 de la pasteleria para no romper Angular,
 * pero agrega un estado CANCELADO para que la cancelacion de pedido no deje una
 * produccion activa huerfana.</p>
 */
public class ProductionStateMachine {

  public void assertTransitionAllowed(ProductionEntity production, ProductionStatus targetStatus) {
    ProductionStatus currentStatus = production.getStatus();
    if (currentStatus == targetStatus) {
      return;
    }

    ensureOrderAllowsProductionChange(production.getOrder(), targetStatus);

    boolean valid = switch (currentStatus) {
      case PENDIENTE -> targetStatus == ProductionStatus.PREPARACION || targetStatus == ProductionStatus.CANCELADO;
      case PREPARACION -> targetStatus == ProductionStatus.DECORACION || targetStatus == ProductionStatus.CANCELADO;
      case DECORACION -> targetStatus == ProductionStatus.EMPAQUE || targetStatus == ProductionStatus.CANCELADO;
      case EMPAQUE -> targetStatus == ProductionStatus.FINALIZADO || targetStatus == ProductionStatus.CANCELADO;
      case FINALIZADO, CANCELADO -> false;
    };

    if (!valid) {
      throw new BusinessRuleException(
          "La transicion de produccion de " + currentStatus + " a " + targetStatus + " no es valida."
      );
    }
  }

  public boolean isActive(ProductionStatus status) {
    return status != ProductionStatus.FINALIZADO && status != ProductionStatus.CANCELADO;
  }

  private void ensureOrderAllowsProductionChange(OrderEntity order, ProductionStatus targetStatus) {
    if (order == null) {
      return;
    }
    if (order.getStatus() == OrderStatus.ENTREGADO) {
      throw new BusinessRuleException("No se puede cambiar la produccion de un pedido ya entregado.");
    }
    if (order.getStatus() == OrderStatus.CANCELADO && targetStatus != ProductionStatus.CANCELADO) {
      throw new BusinessRuleException("No se puede avanzar produccion de un pedido cancelado.");
    }
  }
}
