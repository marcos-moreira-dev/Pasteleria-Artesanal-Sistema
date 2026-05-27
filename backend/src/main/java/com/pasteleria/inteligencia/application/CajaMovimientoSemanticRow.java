package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record CajaMovimientoSemanticRow(
    Long movimientoCajaId,
    String fechaMovimiento,
    String tipoMovimiento,
    String naturaleza,
    BigDecimal monto,
    String moneda,
    String estado,
    String referenciaTipo,
    String referenciaId,
    String descripcion,
    Long turnoCajaId,
    String estadoTurno,
    Long cajaId,
    String cajaCodigo,
    String cajaNombre
) {
}
