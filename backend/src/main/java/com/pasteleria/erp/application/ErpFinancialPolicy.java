package com.pasteleria.erp.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Objects;

import com.pasteleria.common.error.BusinessRuleException;

/**
 * Politica financiera comun para las futuras capacidades ERP.
 *
 * <p>Esta clase no guarda datos, no consulta repositorios y no conoce pantallas.
 * Su responsabilidad es centralizar invariantes reutilizables de dinero, saldos,
 * lineas contables y totales fiscales para que caja, cartera, cuentas por pagar,
 * contabilidad y fiscalidad no creen reglas divergentes.</p>
 *
 * <p>No reemplaza las politicas operativas existentes. CajaPolicy,
 * CompraFinancieraPolicy, InventarioMovimientoPolicy y ProductionMaterialPolicy
 * siguen siendo responsables de las reglas propias de su modulo.</p>
 */
public final class ErpFinancialPolicy {

  private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

  private ErpFinancialPolicy() {
  }

  /**
   * Linea minima para validar partida doble sin depender de una entidad JPA.
   */
  public record AccountingLine(BigDecimal debe, BigDecimal haber) {
  }

  public static BigDecimal money(BigDecimal value) {
    if (value == null) {
      return ZERO;
    }
    return value.setScale(2, RoundingMode.HALF_UP);
  }

  public static void exigirMontoPositivo(BigDecimal value, String message) {
    if (money(value).compareTo(ZERO) <= 0) {
      throw new BusinessRuleException(message);
    }
  }

  public static void exigirNoNegativo(BigDecimal value, String message) {
    if (money(value).compareTo(ZERO) < 0) {
      throw new BusinessRuleException(message);
    }
  }

  /**
   * Evita que una cobranza o un pago aplicado supere el saldo pendiente.
   */
  public static void validarAplicacionContraSaldo(BigDecimal valor, BigDecimal saldo, String message) {
    exigirMontoPositivo(valor, "El valor aplicado debe ser mayor a cero.");
    exigirNoNegativo(saldo, "El saldo pendiente no puede ser negativo.");
    if (money(valor).compareTo(money(saldo)) > 0) {
      throw new BusinessRuleException(message);
    }
  }

  /**
   * Valida que una linea contable tenga movimiento solo en debe o solo en haber.
   */
  public static void validarLineaContable(BigDecimal debe, BigDecimal haber) {
    BigDecimal debeNormalizado = money(debe);
    BigDecimal haberNormalizado = money(haber);
    exigirNoNegativo(debeNormalizado, "El debe no puede ser negativo.");
    exigirNoNegativo(haberNormalizado, "El haber no puede ser negativo.");

    boolean tieneDebe = debeNormalizado.compareTo(ZERO) > 0;
    boolean tieneHaber = haberNormalizado.compareTo(ZERO) > 0;
    if (tieneDebe == tieneHaber) {
      throw new BusinessRuleException("Cada linea de asiento debe tener valor solo en debe o solo en haber.");
    }
  }

  /**
   * Valida partida doble: suma del debe igual a suma del haber.
   */
  public static void validarCuadreContable(Collection<AccountingLine> lineas) {
    if (lineas == null || lineas.size() < 2) {
      throw new BusinessRuleException("El asiento debe tener al menos dos lineas.");
    }

    BigDecimal totalDebe = ZERO;
    BigDecimal totalHaber = ZERO;
    for (AccountingLine linea : lineas) {
      if (linea == null) {
        throw new BusinessRuleException("El asiento no puede contener lineas vacias.");
      }
      validarLineaContable(linea.debe(), linea.haber());
      totalDebe = totalDebe.add(money(linea.debe())).setScale(2, RoundingMode.HALF_UP);
      totalHaber = totalHaber.add(money(linea.haber())).setScale(2, RoundingMode.HALF_UP);
    }

    if (totalDebe.compareTo(totalHaber) != 0) {
      throw new BusinessRuleException("El asiento contable no cuadra: debe y haber deben ser iguales.");
    }
  }

  /**
   * Un documento fiscal interno debe nacer desde exactamente un origen.
   */
  public static void validarOrigenFiscal(Object documentoVentaId, Object documentoCompraId) {
    boolean tieneVenta = documentoVentaId != null;
    boolean tieneCompra = documentoCompraId != null;
    if (tieneVenta == tieneCompra) {
      throw new BusinessRuleException(
          "El documento fiscal requiere exactamente un origen: documento de venta o documento de compra.");
    }
  }

  public static void validarTotalesFiscales(BigDecimal subtotal, BigDecimal impuesto, BigDecimal total) {
    BigDecimal subtotalNormalizado = money(subtotal);
    BigDecimal impuestoNormalizado = money(impuesto);
    BigDecimal totalNormalizado = money(total);

    exigirNoNegativo(subtotalNormalizado, "El subtotal fiscal no puede ser negativo.");
    exigirNoNegativo(impuestoNormalizado, "El impuesto fiscal no puede ser negativo.");
    exigirNoNegativo(totalNormalizado, "El total fiscal no puede ser negativo.");

    BigDecimal esperado = subtotalNormalizado.add(impuestoNormalizado).setScale(2, RoundingMode.HALF_UP);
    if (esperado.compareTo(totalNormalizado) != 0) {
      throw new BusinessRuleException("El total fiscal debe ser igual a subtotal mas impuesto.");
    }
  }

  /**
   * Variante simple para obligaciones donde solo interesa saber si queda saldo.
   */
  public static String estadoPorSaldo(BigDecimal saldo) {
    return money(saldo).compareTo(ZERO) == 0 ? "PAGADO" : "PARCIAL";
  }

  /**
   * Variante completa para distinguir pendiente, parcial y pagado.
   */
  public static String estadoPorSaldo(BigDecimal saldo, BigDecimal total) {
    BigDecimal saldoNormalizado = money(saldo);
    BigDecimal totalNormalizado = money(total);

    exigirMontoPositivo(totalNormalizado, "El total de la obligacion debe ser mayor a cero.");
    exigirNoNegativo(saldoNormalizado, "El saldo de la obligacion no puede ser negativo.");

    if (saldoNormalizado.compareTo(totalNormalizado) > 0) {
      throw new BusinessRuleException("El saldo de la obligacion no puede superar el total.");
    }
    if (saldoNormalizado.compareTo(ZERO) == 0) {
      return "PAGADO";
    }
    if (saldoNormalizado.compareTo(totalNormalizado) == 0) {
      return "PENDIENTE";
    }
    return "PARCIAL";
  }

  public static void exigirCuentaImputable(boolean imputable, String codigoCuenta) {
    if (!imputable) {
      throw new BusinessRuleException("La cuenta " + Objects.toString(codigoCuenta, "") + " no es imputable.");
    }
  }
}
