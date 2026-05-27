package com.pasteleria.caja.application;

import java.math.BigDecimal;
import java.util.Locale;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import org.springframework.stereotype.Component;

/**
 * Reglas operativas de caja. Mantiene fuera del service las decisiones sobre
 * montos, naturalezas, estados y signos de movimientos.
 */
@Component
public class CajaPolicy {

  public static final String DEFAULT_CAJA_CODIGO = "CAJA_MATRIZ";
  public static final String DEFAULT_REFERENCIA_TIPO = "CAJA";
  public static final String DEFAULT_REFERENCIA_ID = "OPERACION_MANUAL";

  public BigDecimal normalizeMoney(BigDecimal value) {
    return ErpFinancialPolicy.money(value);
  }

  public void exigirMontoNoNegativo(BigDecimal value, String field) {
    ErpFinancialPolicy.exigirNoNegativo(value, field + " no puede ser negativo.");
  }

  public void exigirMontoPositivo(BigDecimal value, String field) {
    ErpFinancialPolicy.exigirMontoPositivo(value, field + " debe ser mayor a cero.");
  }

  public TipoMovimientoCaja parseTipo(String value) {
    try {
      return TipoMovimientoCaja.valueOf(normalizeCode(value));
    } catch (IllegalArgumentException exception) {
      throw new BusinessRuleException("Tipo de movimiento de caja no reconocido: " + value);
    }
  }

  public NaturalezaMovimientoCaja parseNaturaleza(String value) {
    try {
      return NaturalezaMovimientoCaja.valueOf(normalizeCode(value));
    } catch (IllegalArgumentException exception) {
      throw new BusinessRuleException("Naturaleza de movimiento de caja no reconocida: " + value);
    }
  }

  public BigDecimal aplicarMovimiento(BigDecimal saldoActual, NaturalezaMovimientoCaja naturaleza, BigDecimal monto) {
    BigDecimal saldo = normalizeMoney(saldoActual);
    BigDecimal amount = normalizeMoney(monto);
    return switch (naturaleza) {
      case ENTRADA -> saldo.add(amount);
      case SALIDA -> {
        BigDecimal next = saldo.subtract(amount);
        if (next.compareTo(BigDecimal.ZERO) < 0) {
          throw new BusinessRuleException("La caja no tiene saldo suficiente para registrar la salida.");
        }
        yield next;
      }
      case AJUSTE -> saldo;
    };
  }

  public BigDecimal calcularEsperado(BigDecimal esperado, NaturalezaMovimientoCaja naturaleza, BigDecimal monto) {
    BigDecimal base = normalizeMoney(esperado);
    BigDecimal amount = normalizeMoney(monto);
    return switch (naturaleza) {
      case ENTRADA -> base.add(amount);
      case SALIDA -> base.subtract(amount);
      case AJUSTE -> base;
    };
  }

  public void exigirTurnoAbierto(String estado) {
    if (!EstadoTurnoCaja.ABIERTO.name().equals(estado)) {
      throw new BusinessRuleException("El turno de caja no esta abierto.");
    }
  }

  public String cajaCodigoOrDefault(String value) {
    if (value == null || value.isBlank()) {
      return DEFAULT_CAJA_CODIGO;
    }
    return value.trim().toUpperCase(Locale.ROOT);
  }

  public String referenciaTipoOrDefault(String value) {
    if (value == null || value.isBlank()) {
      return DEFAULT_REFERENCIA_TIPO;
    }
    return value.trim().toUpperCase(Locale.ROOT);
  }

  public String referenciaIdOrDefault(String value) {
    if (value == null || value.isBlank()) {
      return DEFAULT_REFERENCIA_ID;
    }
    return value.trim();
  }

  public String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }

  private String normalizeCode(String value) {
    if (value == null || value.isBlank()) {
      throw new BusinessRuleException("El codigo operativo de caja es obligatorio.");
    }
    return value.trim().toUpperCase(Locale.ROOT);
  }
}
