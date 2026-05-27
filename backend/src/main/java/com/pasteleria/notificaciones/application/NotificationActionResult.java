package com.pasteleria.notificaciones.application;

/**
 * Resume acciones masivas del buzon sin exponer logica de persistencia al API.
 */
public record NotificationActionResult(int affectedCount) {
}
