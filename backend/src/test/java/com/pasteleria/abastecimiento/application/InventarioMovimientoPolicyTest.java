package com.pasteleria.abastecimiento.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.pasteleria.abastecimiento.domain.model.ItemTipo;
import com.pasteleria.abastecimiento.domain.model.TipoMovimiento;
import com.pasteleria.common.error.BusinessRuleException;

import org.junit.jupiter.api.Test;

class InventarioMovimientoPolicyTest {

  private final InventarioMovimientoPolicy policy = new InventarioMovimientoPolicy();

  @Test
  void shouldNormalizeItemTypeAndMovementType() {
    assertThat(policy.normalizarItemTipo(" ingrediente ")).isEqualTo(ItemTipo.INGREDIENTE);
    assertThat(policy.normalizarTipoMovimiento(" salida_merma ")).isEqualTo(TipoMovimiento.SALIDA_MERMA);
  }

  @Test
  void shouldRejectInvalidQuantity() {
    assertThatThrownBy(() -> policy.normalizarCantidad(BigDecimal.ZERO))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("cantidad");
  }

  @Test
  void shouldCalculateBalancesWithPositiveQuantities() {
    BigDecimal entrada = policy.calcularSaldoPosterior(
        new BigDecimal("10.000"),
        TipoMovimiento.ENTRADA_AJUSTE,
        new BigDecimal("2.500")
    );
    BigDecimal salida = policy.calcularSaldoPosterior(
        new BigDecimal("10.000"),
        TipoMovimiento.SALIDA_AJUSTE,
        new BigDecimal("2.500")
    );

    assertThat(entrada).isEqualByComparingTo("12.500");
    assertThat(salida).isEqualByComparingTo("7.500");
  }

  @Test
  void shouldRejectNegativeStock() {
    assertThatThrownBy(() -> policy.calcularSaldoPosterior(
        new BigDecimal("1.000"),
        TipoMovimiento.SALIDA_PRODUCCION,
        new BigDecimal("2.000")
    ))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("stock no puede quedar negativo");
  }

  @Test
  void shouldRequireTraceabilityForOutputs() {
    assertThatThrownBy(() -> policy.validarJustificacion(TipoMovimiento.SALIDA_MERMA, null, " "))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("salidas de inventario");
  }

  @Test
  void shouldDefaultReferenceForManualAdjustments() {
    assertThat(policy.referenciaTipoOrDefault(null, TipoMovimiento.SALIDA_AJUSTE)).isEqualTo("AJUSTE_MANUAL");
    assertThat(policy.referenciaIdOrDefault(null, TipoMovimiento.SALIDA_AJUSTE)).isEqualTo("SALIDA_AJUSTE");
  }
}
