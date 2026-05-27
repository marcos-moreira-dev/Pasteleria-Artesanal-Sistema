package com.pasteleria.abastecimiento.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraDetalleEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraEntity;
import com.pasteleria.common.error.BusinessRuleException;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CompraFinancieraPolicyTest {

  private final CompraFinancieraPolicy policy = new CompraFinancieraPolicy();

  @Test
  void shouldCalculateSubtotalFromReceivedQuantities() {
    OrdenCompraEntity orden = orden("RECIBIDA_PARCIAL");
    orden.getDetalles().add(detalle(10, 4, "1.50"));
    orden.getDetalles().add(detalle(5, 0, "3.00"));

    assertThat(policy.calcularSubtotalRecibido(orden)).isEqualByComparingTo("6.00");
  }

  @Test
  void shouldRejectOrderWithoutReception() {
    OrdenCompraEntity orden = orden("ENVIADA");
    orden.getDetalles().add(detalle(10, 0, "1.50"));

    assertThatThrownBy(() -> policy.exigirOrdenDocumentable(orden))
        .isInstanceOf(BusinessRuleException.class);
  }

  @Test
  void shouldNormalizeDocumentNumber() {
    assertThat(policy.normalizarNumeroDocumento(null, "OC-2026-001")).isEqualTo("DOC-OC-2026-001");
    assertThat(policy.normalizarNumeroDocumento(" fac-001 ", "OC-2026-001")).isEqualTo("FAC-001");
  }

  @Test
  void shouldRejectNegativeTax() {
    assertThatThrownBy(() -> policy.normalizarImpuesto(new BigDecimal("-0.01")))
        .isInstanceOf(BusinessRuleException.class);
  }

  private OrdenCompraEntity orden(String estado) {
    OrdenCompraEntity orden = new OrdenCompraEntity();
    orden.setEstado(estado);
    orden.setCodigo("OC-2026-TEST");
    return orden;
  }

  private OrdenCompraDetalleEntity detalle(int cantidad, int recibida, String precio) {
    OrdenCompraDetalleEntity detalle = new OrdenCompraDetalleEntity();
    detalle.setCantidad(cantidad);
    detalle.setCantidadRecibida(recibida);
    detalle.setPrecioUnitario(new BigDecimal(precio));
    return detalle;
  }
}
