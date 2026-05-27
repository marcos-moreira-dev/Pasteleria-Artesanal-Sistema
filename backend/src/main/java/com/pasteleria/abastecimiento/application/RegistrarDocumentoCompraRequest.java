package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record RegistrarDocumentoCompraRequest(
    @Size(max = 60) String numeroDocumento,
    String fechaEmision,
    String fechaVencimiento,
    @DecimalMin("0.00") BigDecimal impuesto,
    @Size(max = 2000) String observaciones
) {
}
