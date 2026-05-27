package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record DashboardErpSummary(
    String fechaCorte,
    long pedidosActivos,
    long produccionesActivas,
    long documentosCobrarAbiertos,
    BigDecimal saldoCartera,
    long documentosPagarAbiertos,
    BigDecimal saldoCuentasPagar,
    BigDecimal saldoCaja,
    long asientosRegistrados,
    long documentosFiscalesBorrador,
    long itemsStockBajo
) {
}
