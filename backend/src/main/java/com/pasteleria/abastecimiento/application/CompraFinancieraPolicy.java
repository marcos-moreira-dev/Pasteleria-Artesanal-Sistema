package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.domain.model.EstadoOrdenCompra;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraDetalleEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraEntity;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

/**
 * Reglas financieras transicionales de compras.
 *
 * <p>Esta politica no reemplaza la contabilidad ni cuentas por pagar formales
 * que se implementaran en T16-T18. Su responsabilidad en T14 es asegurar que
 * una orden recibida pueda convertirse en documento de compra y obligación
 * pendiente sin duplicaciones ni saldos imposibles.</p>
 */
@Component
public class CompraFinancieraPolicy {

  public void exigirOrdenDocumentable(OrdenCompraEntity orden) {
    EstadoOrdenCompra estado = EstadoOrdenCompra.valueOf(orden.getEstado());
    if (estado != EstadoOrdenCompra.RECIBIDA && estado != EstadoOrdenCompra.RECIBIDA_PARCIAL) {
      throw new BusinessRuleException("Solo se puede registrar documento de compra para órdenes recibidas o parcialmente recibidas.");
    }

    boolean tieneRecepcion = orden.getDetalles().stream()
        .anyMatch(detalle -> defaultInt(detalle.getCantidadRecibida()) > 0);
    if (!tieneRecepcion) {
      throw new BusinessRuleException("La orden debe tener al menos una recepción antes de registrar documento de compra.");
    }
  }

  public BigDecimal calcularSubtotalRecibido(OrdenCompraEntity orden) {
    BigDecimal subtotal = BigDecimal.ZERO;
    for (OrdenCompraDetalleEntity detalle : orden.getDetalles()) {
      int recibida = defaultInt(detalle.getCantidadRecibida());
      if (recibida <= 0) {
        continue;
      }
      BigDecimal precio = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : BigDecimal.ZERO;
      subtotal = subtotal.add(precio.multiply(BigDecimal.valueOf(recibida)));
    }
    return subtotal.setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal normalizarImpuesto(BigDecimal impuesto) {
    ErpFinancialPolicy.exigirNoNegativo(impuesto, "El impuesto del documento de compra no puede ser negativo.");
    return ErpFinancialPolicy.money(impuesto);
  }

  public BigDecimal calcularTotal(BigDecimal subtotal, BigDecimal impuesto) {
    BigDecimal total = ErpFinancialPolicy.money(subtotal)
        .add(ErpFinancialPolicy.money(impuesto))
        .setScale(2, RoundingMode.HALF_UP);
    ErpFinancialPolicy.exigirMontoPositivo(total, "El total del documento de compra debe ser mayor a cero.");
    return total;
  }

  public String normalizarNumeroDocumento(String numeroDocumento, String ordenCodigo) {
    String base = (numeroDocumento == null || numeroDocumento.isBlank())
        ? "DOC-" + ordenCodigo
        : numeroDocumento.trim();
    if (base.length() > 60) {
      throw new BusinessRuleException("El número de documento de compra no debe superar 60 caracteres.");
    }
    return base.toUpperCase();
  }

  public String construirCodigoDocumentoPagar(String ordenCodigo) {
    return ("CXP-" + ordenCodigo).toUpperCase();
  }

  private int defaultInt(Integer value) {
    return value != null ? value : 0;
  }
}
