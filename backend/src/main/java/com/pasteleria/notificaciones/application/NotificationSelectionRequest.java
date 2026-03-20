package com.pasteleria.notificaciones.application;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

/**
 * Transporta un conjunto de notificaciones seleccionadas para acciones masivas
 * del buzon interno.
 */
public record NotificationSelectionRequest(
    @NotEmpty(message = "Debe seleccionar al menos una notificacion.")
    List<@Positive(message = "Cada identificador de notificacion debe ser positivo.") Long> notificationIds
) {
}
