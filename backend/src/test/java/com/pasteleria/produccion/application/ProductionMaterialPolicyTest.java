package com.pasteleria.produccion.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.pasteleria.common.error.BusinessRuleException;

class ProductionMaterialPolicyTest {

  private final ProductionMaterialPolicy policy = new ProductionMaterialPolicy();

  @Test
  void shouldCalculateRecipeScalingFactor() {
    BigDecimal factor = policy.calcularFactor(new BigDecimal("24.0000"), new BigDecimal("12.00"));

    assertThat(factor).isEqualByComparingTo("2.00000000");
  }

  @Test
  void shouldCalculateConsumptionFromBaseQuantityAndFactor() {
    BigDecimal consumo = policy.calcularConsumo(new BigDecimal("0.2500"), new BigDecimal("2.00000000"));

    assertThat(consumo).isEqualByComparingTo("0.5000");
  }

  @Test
  void shouldRejectInvalidYield() {
    assertThatThrownBy(() -> policy.calcularFactor(BigDecimal.ONE, BigDecimal.ZERO))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("rendimiento");
  }

  @Test
  void shouldCalculateCost() {
    BigDecimal costo = policy.calcularCosto(new BigDecimal("2.5000"), new BigDecimal("1.2000"));

    assertThat(costo).isEqualByComparingTo("3.0000");
  }
}
