package com.pasteleria.common.api;

/**
 * Detalle granular de un error de API.
 *
 * <p>Se usa principalmente para validaciones de campos, pero tambien permite
 * adjuntar detalles operativos sin cambiar el contrato principal.</p>
 */
public record ApiErrorDetail(
    String field,
    String message
) {
}
