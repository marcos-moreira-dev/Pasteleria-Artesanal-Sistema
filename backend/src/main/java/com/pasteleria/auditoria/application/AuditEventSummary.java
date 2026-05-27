package com.pasteleria.auditoria.application;

import java.time.OffsetDateTime;

/**
 * Resumen legible de un evento de auditoria para soporte y revision administrativa.
 */
public record AuditEventSummary(
    Long id,
    String eventCode,
    String module,
    String entity,
    String entityId,
    String action,
    String actorUsername,
    String actorRole,
    OffsetDateTime eventAt,
    String reason,
    String requestId,
    String ipAddress
) {
}
