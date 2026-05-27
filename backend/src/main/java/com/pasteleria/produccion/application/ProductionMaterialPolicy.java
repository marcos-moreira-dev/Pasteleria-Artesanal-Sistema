package com.pasteleria.produccion.application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.pasteleria.common.error.BusinessRuleException;

/**
 * Reglas puras para escalar recetas técnicas hacia una producción concreta.
 */
@Component
public class ProductionMaterialPolicy {

  public BigDecimal normalizarCantidadProducida(int cantidad) {
    if (cantidad <= 0) {
      throw new BusinessRuleException("La cantidad producida debe ser mayor a cero.");
    }
    return BigDecimal.valueOf(cantidad).setScale(4, RoundingMode.HALF_UP);
  }

  public BigDecimal calcularFactor(BigDecimal cantidadProducida, BigDecimal rendimientoBase) {
    BigDecimal rendimiento = rendimientoBase == null ? BigDecimal.ONE : rendimientoBase;
    if (rendimiento.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("La receta técnica tiene rendimiento base inválido.");
    }
    return cantidadProducida.divide(rendimiento, 8, RoundingMode.HALF_UP);
  }

  public BigDecimal calcularConsumo(BigDecimal cantidadBase, BigDecimal factor) {
    BigDecimal base = cantidadBase == null ? BigDecimal.ZERO : cantidadBase;
    if (base.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("La receta técnica contiene un detalle con cantidad inválida.");
    }
    return base.multiply(factor).setScale(4, RoundingMode.HALF_UP);
  }

  public BigDecimal calcularCosto(BigDecimal cantidadConsumida, BigDecimal costoUnitario) {
    BigDecimal unitario = costoUnitario == null ? BigDecimal.ZERO : costoUnitario;
    return cantidadConsumida.multiply(unitario).setScale(4, RoundingMode.HALF_UP);
  }
}
