package com.pasteleria.contabilidad.application;

public record TipoDiarioSummary(
    Long id,
    String codigo,
    String nombre,
    String descripcion,
    Boolean activo
) {
}
