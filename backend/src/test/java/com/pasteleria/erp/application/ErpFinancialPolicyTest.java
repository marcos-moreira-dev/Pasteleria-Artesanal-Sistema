package com.pasteleria.erp.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.erp.application.ErpFinancialPolicy.AccountingLine;

import org.junit.jupiter.api.Test;

class ErpFinancialPolicyTest {

  @Test
  void shouldNormalizeMoneyWithTwoDecimals() {
    assertThat(ErpFinancialPolicy.money(null)).isEqualByComparingTo("0.00");
    assertThat(ErpFinancialPolicy.money(new BigDecimal("10.125"))).isEqualByComparingTo("10.13");
  }

  @Test
  void shouldRejectNonPositiveAmounts() {
    assertThatThrownBy(() -> ErpFinancialPolicy.exigirMontoPositivo(BigDecimal.ZERO, "Monto invalido."))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("Monto invalido");
  }

  @Test
  void shouldValidateApplicationAgainstBalance() {
    ErpFinancialPolicy.validarAplicacionContraSaldo(
        new BigDecimal("25.00"),
        new BigDecimal("30.00"),
        "El valor aplicado no puede superar el saldo.");

    assertThatThrownBy(() -> ErpFinancialPolicy.validarAplicacionContraSaldo(
        new BigDecimal("35.00"),
        new BigDecimal("30.00"),
        "El valor aplicado no puede superar el saldo."))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("superar el saldo");
  }

  @Test
  void shouldValidateAccountingLineUsesOnlyDebitOrCredit() {
    ErpFinancialPolicy.validarLineaContable(new BigDecimal("10.00"), BigDecimal.ZERO);
    ErpFinancialPolicy.validarLineaContable(BigDecimal.ZERO, new BigDecimal("10.00"));

    assertThatThrownBy(() -> ErpFinancialPolicy.validarLineaContable(new BigDecimal("10.00"), new BigDecimal("1.00")))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("solo en debe o solo en haber");
  }

  @Test
  void shouldValidateBalancedAccountingEntry() {
    ErpFinancialPolicy.validarCuadreContable(List.of(
        new AccountingLine(new BigDecimal("12.00"), BigDecimal.ZERO),
        new AccountingLine(BigDecimal.ZERO, new BigDecimal("12.00"))));

    assertThatThrownBy(() -> ErpFinancialPolicy.validarCuadreContable(List.of(
        new AccountingLine(new BigDecimal("12.00"), BigDecimal.ZERO),
        new AccountingLine(BigDecimal.ZERO, new BigDecimal("11.99")))))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("no cuadra");
  }

  @Test
  void shouldRequireExactlyOneFiscalOrigin() {
    ErpFinancialPolicy.validarOrigenFiscal(10L, null);
    ErpFinancialPolicy.validarOrigenFiscal(null, 20L);

    assertThatThrownBy(() -> ErpFinancialPolicy.validarOrigenFiscal(null, null))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("exactamente un origen");

    assertThatThrownBy(() -> ErpFinancialPolicy.validarOrigenFiscal(10L, 20L))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("exactamente un origen");
  }

  @Test
  void shouldValidateFiscalTotals() {
    ErpFinancialPolicy.validarTotalesFiscales(
        new BigDecimal("100.00"),
        new BigDecimal("15.00"),
        new BigDecimal("115.00"));

    assertThatThrownBy(() -> ErpFinancialPolicy.validarTotalesFiscales(
        new BigDecimal("100.00"),
        new BigDecimal("15.00"),
        new BigDecimal("114.99")))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("subtotal mas impuesto");
  }

  @Test
  void shouldResolveStatusByBalanceAndTotal() {
    assertThat(ErpFinancialPolicy.estadoPorSaldo(new BigDecimal("100.00"), new BigDecimal("100.00")))
        .isEqualTo("PENDIENTE");
    assertThat(ErpFinancialPolicy.estadoPorSaldo(new BigDecimal("40.00"), new BigDecimal("100.00")))
        .isEqualTo("PARCIAL");
    assertThat(ErpFinancialPolicy.estadoPorSaldo(BigDecimal.ZERO, new BigDecimal("100.00")))
        .isEqualTo("PAGADO");
  }

  @Test
  void shouldRejectNonImputableAccount() {
    assertThatThrownBy(() -> ErpFinancialPolicy.exigirCuentaImputable(false, "1.1.1.01"))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessageContaining("no es imputable");
  }
}
