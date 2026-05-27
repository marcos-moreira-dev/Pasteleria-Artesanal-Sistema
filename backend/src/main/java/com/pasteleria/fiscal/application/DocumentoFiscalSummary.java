package com.pasteleria.fiscal.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DocumentoFiscalSummary(
    Long id,
    String codigo,
    String tipoComprobante,
    String estado,
    Long documentoCobrarId,
    Long documentoCompraId,
    String origenTipo,
    String terceroTipo,
    Long terceroId,
    String terceroNombre,
    LocalDateTime fechaEmision,
    String establecimiento,
    String puntoEmision,
    String secuencial,
    String numeroComprobante,
    BigDecimal subtotal,
    BigDecimal impuesto,
    BigDecimal total,
    String claveAcceso,
    String numeroAutorizacion,
    LocalDateTime fechaAutorizacion,
    String ambiente,
    String observaciones
) {
}
