package com.pasteleria.contratos.application;

/**
 * Descripcion operativa de un permiso reconocido por el backend.
 */
public record PermissionContract(
    String code,
    String module,
    String description,
    String scope
) {
}
