package com.pasteleria.common.api;

import java.time.OffsetDateTime;

/**
 * Metadatos transversales de toda respuesta API.
 *
 * <p>El requestId permite correlacionar frontend, logs, auditoria y soporte
 * sin acoplar cada modulo a detalles del filtro HTTP.</p>
 */
public record ApiMeta(
    String requestId,
    OffsetDateTime timestamp
) {
}
