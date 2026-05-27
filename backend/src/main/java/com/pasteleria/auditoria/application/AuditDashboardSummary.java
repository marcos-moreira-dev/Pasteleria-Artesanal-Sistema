package com.pasteleria.auditoria.application;

import java.util.List;

/**
 * Fotografia corta de auditoria para soporte operativo.
 */
public record AuditDashboardSummary(
    long totalEvents,
    long eventsLast24Hours,
    long distinctModules,
    List<AuditModuleCount> topModules
) {
}
