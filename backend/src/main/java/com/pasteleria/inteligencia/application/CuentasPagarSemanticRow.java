package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record CuentasPagarSemanticRow(
    Long documentoPagarId,
    String codigo,
    String estado,
    String fechaEmision,
    String fechaVencimiento,
    BigDecimal total,
    BigDecimal saldo,
    BigDecimal montoPagado,
    int diasVencido,
    Long proveedorId,
    String proveedorNombre,
    Long documentoCompraId,
    String numeroDocumentoCompra
) {
}
