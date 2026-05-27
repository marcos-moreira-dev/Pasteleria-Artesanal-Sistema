package com.pasteleria.cuentaspagar.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagoProveedorSummary(
    Long id,
    Long proveedorId,
    String proveedorNombre,
    String codigo,
    LocalDateTime fechaPago,
    BigDecimal montoTotal,
    String medioPago,
    String referenciaPago,
    String estado,
    String observaciones,
    List<PagoProveedorAplicacionSummary> aplicaciones
) {
}
