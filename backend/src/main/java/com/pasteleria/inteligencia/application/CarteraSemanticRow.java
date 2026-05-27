package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record CarteraSemanticRow(
    Long documentoCobrarId,
    String codigo,
    String estado,
    String fechaEmision,
    String fechaVencimiento,
    BigDecimal total,
    BigDecimal saldo,
    BigDecimal montoCobrado,
    int diasVencido,
    Long clienteId,
    String clienteNombre,
    Long pedidoId,
    String pedidoCodigo
) {
}
