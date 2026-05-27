package com.pasteleria.cuentaspagar.application;

import java.math.BigDecimal;

public record PagoProveedorAplicacionSummary(
    Long id,
    Long documentoPagarId,
    String documentoCodigo,
    BigDecimal montoAplicado,
    BigDecimal saldoAnterior,
    BigDecimal saldoPosterior
) {
}
