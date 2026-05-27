package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record FiscalSemanticRow(
    Long documentoFiscalId,
    String codigo,
    String tipoComprobante,
    String estado,
    String origenTipo,
    String terceroTipo,
    Long terceroId,
    String terceroNombre,
    String fechaEmision,
    String numeroComprobante,
    BigDecimal subtotal,
    BigDecimal impuesto,
    BigDecimal total,
    String ambiente,
    String claveAcceso,
    String numeroAutorizacion,
    String fechaAutorizacion
) {
}
