package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ItemProveedorEntity.ItemTipo;

import java.math.BigDecimal;

public record ItemProveedorSummary(
    Long id,
    ItemTipo itemTipo,
    Long itemId,
    String itemNombre,
    Long proveedorId,
    String proveedorNombre,
    BigDecimal precioSuministro,
    boolean esPrincipal,
    boolean activo
) {
}