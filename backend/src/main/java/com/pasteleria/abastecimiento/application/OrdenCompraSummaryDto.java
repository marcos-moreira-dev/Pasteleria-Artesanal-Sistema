package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenCompraSummaryDto(
    Long id,
    String codigo,
    String estado,
    Long proveedorId,
    String proveedorNombre,
    LocalDateTime fechaEmision,
    LocalDateTime fechaEntregaEsperada,
    BigDecimal totalEstimado,
    LocalDateTime createdAt
) {}
