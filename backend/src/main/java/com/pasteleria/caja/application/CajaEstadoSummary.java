package com.pasteleria.caja.application;

import java.math.BigDecimal;

public record CajaEstadoSummary(
    Long cajaId,
    String cajaCodigo,
    String cajaNombre,
    String sucursalCodigo,
    String moneda,
    BigDecimal saldoActual,
    boolean activa,
    boolean turnoAbierto,
    TurnoCajaSummary turnoActual
) {
}
