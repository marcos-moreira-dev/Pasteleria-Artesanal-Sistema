package com.pasteleria.erpbridge.application;

/**
 * Resultado pequeño y estable de una operación bridge ERP.
 *
 * <p>Evita exponer demasiado detalle interno y deja claro si la operación
 * creó algo nuevo o si devolvió una consecuencia ya existente por idempotencia.</p>
 */
public record ErpBridgeOperationResult(
    String operacion,
    String origenTipo,
    String origenId,
    String destinoTipo,
    Long destinoId,
    String destinoCodigo,
    boolean creado,
    String mensaje
) {
}
