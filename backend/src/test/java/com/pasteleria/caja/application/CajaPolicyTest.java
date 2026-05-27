package com.pasteleria.caja.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.pasteleria.common.error.BusinessRuleException;

import org.junit.jupiter.api.Test;

class CajaPolicyTest {

  private final CajaPolicy policy = new CajaPolicy();

  @Test
  void shouldAddCashForIncome() {
    BigDecimal result = policy.aplicarMovimiento(new BigDecimal("10.00"), NaturalezaMovimientoCaja.ENTRADA, new BigDecimal("5.25"));

    assertThat(result).isEqualByComparingTo("15.25");
  }

  @Test
  void shouldSubtractCashForOutcome() {
    BigDecimal result = policy.aplicarMovimiento(new BigDecimal("10.00"), NaturalezaMovimientoCaja.SALIDA, new BigDecimal("4.50"));

    assertThat(result).isEqualByComparingTo("5.50");
  }

  @Test
  void shouldRejectNegativeCashBalance() {
    assertThatThrownBy(() -> policy.aplicarMovimiento(new BigDecimal("3.00"), NaturalezaMovimientoCaja.SALIDA, new BigDecimal("4.00")))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("saldo suficiente");
  }

  @Test
  void shouldKeepBalanceForAdjustmentNature() {
    BigDecimal result = policy.aplicarMovimiento(new BigDecimal("10.00"), NaturalezaMovimientoCaja.AJUSTE, new BigDecimal("999.00"));

    assertThat(result).isEqualByComparingTo("10.00");
  }

  @Test
  void shouldParseCodesIgnoringCaseAndSpaces() {
    assertThat(policy.parseTipo(" venta_contado ")).isEqualTo(TipoMovimientoCaja.VENTA_CONTADO);
    assertThat(policy.parseNaturaleza(" entrada ")).isEqualTo(NaturalezaMovimientoCaja.ENTRADA);
  }
}
