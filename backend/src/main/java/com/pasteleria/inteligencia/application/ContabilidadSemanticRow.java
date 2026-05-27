package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record ContabilidadSemanticRow(
    Long asientoContableId,
    String codigo,
    String fechaAsiento,
    String estado,
    String descripcion,
    String origenTipo,
    String origenId,
    BigDecimal totalDebe,
    BigDecimal totalHaber,
    String diarioCodigo,
    String diarioNombre,
    long lineas
) {
}
