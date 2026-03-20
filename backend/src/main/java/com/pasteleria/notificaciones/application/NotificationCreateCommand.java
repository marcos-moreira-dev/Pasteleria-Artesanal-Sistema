package com.pasteleria.notificaciones.application;

import com.pasteleria.notificaciones.domain.model.NotificationPriority;

public record NotificationCreateCommand(
    String type,
    String title,
    String message,
    String module,
    String referenceType,
    String referenceId,
    NotificationPriority priority,
    String payloadJson
) {
}


