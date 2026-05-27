package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;

import com.pasteleria.abastecimiento.domain.model.ItemTipo;
import com.pasteleria.abastecimiento.domain.model.TipoMovimiento;
import com.pasteleria.common.error.BusinessRuleException;

import org.springframework.stereotype.Component;

/**
 * Política funcional única para movimientos de inventario V1.
 *
 * <p>La V2 ERP migrará inventario hacia item_maestro/stock_almacen; mientras
 * tanto, esta política evita que las reglas queden dispersas entre controllers,
 * servicios de compras, producción y ajustes manuales.</p>
 */
@Component
public class InventarioMovimientoPolicy {

  public ItemTipo normalizarItemTipo(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new BusinessRuleException("Debe indicar el tipo de item del inventario.");
    }
    try {
      return ItemTipo.valueOf(raw.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new BusinessRuleException("Tipo de item de inventario no soportado: " + raw);
    }
  }

  public TipoMovimiento normalizarTipoMovimiento(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new BusinessRuleException("Debe indicar el tipo de movimiento de inventario.");
    }
    try {
      return TipoMovimiento.valueOf(raw.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new BusinessRuleException("Tipo de movimiento de inventario no soportado: " + raw);
    }
  }

  public BigDecimal normalizarCantidad(BigDecimal cantidad) {
    if (cantidad == null) {
      throw new BusinessRuleException("La cantidad del movimiento de inventario es obligatoria.");
    }
    if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("La cantidad del movimiento debe ser positiva. La entrada o salida la define el tipo de movimiento.");
    }
    return cantidad.stripTrailingZeros();
  }

  public BigDecimal calcularSaldoPosterior(BigDecimal saldoAnterior, TipoMovimiento tipoMovimiento, BigDecimal cantidad) {
    BigDecimal saldoBase = saldoAnterior == null ? BigDecimal.ZERO : saldoAnterior;
    BigDecimal saldoPosterior = esEntrada(tipoMovimiento) ? saldoBase.add(cantidad) : saldoBase.subtract(cantidad);
    if (saldoPosterior.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException(
          "El stock no puede quedar negativo. Stock actual: " + saldoBase + ", cantidad solicitada: " + cantidad
      );
    }
    return saldoPosterior;
  }

  public boolean esEntrada(TipoMovimiento tipoMovimiento) {
    return tipoMovimiento.name().startsWith("ENTRADA_");
  }

  public boolean esSalida(TipoMovimiento tipoMovimiento) {
    return tipoMovimiento.name().startsWith("SALIDA_");
  }

  public void validarJustificacion(TipoMovimiento tipoMovimiento, String motivoSalida, String observaciones) {
    if (esSalida(tipoMovimiento) && isBlank(motivoSalida) && isBlank(observaciones)) {
      throw new BusinessRuleException("Las salidas de inventario requieren motivo u observación para trazabilidad.");
    }
  }

  public String referenciaTipoOrDefault(String referenciaTipo, TipoMovimiento tipoMovimiento) {
    if (!isBlank(referenciaTipo)) {
      return referenciaTipo.trim().toUpperCase();
    }
    if (tipoMovimiento == TipoMovimiento.ENTRADA_AJUSTE || tipoMovimiento == TipoMovimiento.SALIDA_AJUSTE) {
      return "AJUSTE_MANUAL";
    }
    if (tipoMovimiento == TipoMovimiento.SALIDA_MERMA) {
      return "MERMA";
    }
    return "MOVIMIENTO_MANUAL";
  }

  public String referenciaIdOrDefault(String referenciaId, TipoMovimiento tipoMovimiento) {
    if (!isBlank(referenciaId)) {
      return referenciaId.trim();
    }
    return tipoMovimiento.name();
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
