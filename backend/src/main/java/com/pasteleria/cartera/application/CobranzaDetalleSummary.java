package com.pasteleria.cartera.application;

import java.math.BigDecimal;

public record CobranzaDetalleSummary(
    Long id,
    Long documentoCobrarId,
    String documentoCodigo,
    BigDecimal montoAplicado,
    BigDecimal saldoAnterior,
    BigDecimal saldoPosterior
) {
}
