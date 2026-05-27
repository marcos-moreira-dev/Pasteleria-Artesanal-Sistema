package com.pasteleria.soporte.application;

/**
 * Item de checklist operativo para revisar antes de una entrega o soporte.
 */
public record SupportChecklistItem(
    String code,
    String title,
    String status,
    String detail
) {
}
