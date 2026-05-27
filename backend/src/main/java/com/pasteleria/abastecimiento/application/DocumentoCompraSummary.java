package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DocumentoCompraSummary(
    Long id,
    Long ordenCompraId,
    String ordenCompraCodigo,
    Long proveedorId,
    String proveedorNombre,
    String numeroDocumento,
    String estado,
    LocalDateTime fechaEmision,
    BigDecimal subtotal,
    BigDecimal impuesto,
    BigDecimal total,
    String observaciones,
    LocalDateTime createdAt
) {
}
