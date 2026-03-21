package com.pasteleria.abastecimiento.application;

import java.time.OffsetDateTime;

public record ProveedorSummary(
    Long id,
    String codigo,
    String nombre,
    String telefono,
    String correo,
    String direccion,
    String observaciones,
    boolean activo,
    OffsetDateTime createdAt
) {
}