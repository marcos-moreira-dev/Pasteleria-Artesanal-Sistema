package com.pasteleria.auditoria.application;

/**
 * Conteo de eventos por modulo para tablero de auditoria.
 */
public record AuditModuleCount(
    String module,
    long totalEvents
) {
}
