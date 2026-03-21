package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity.UmedidaTipo;

public record UmedidaSummary(
    Long id,
    String codigo,
    String nombre,
    String abreviatura,
    UmedidaTipo tipo,
    int decimales,
    boolean activo
) {
}