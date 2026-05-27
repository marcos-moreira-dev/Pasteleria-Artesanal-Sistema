package com.pasteleria.notificaciones.application;

import java.time.OffsetDateTime;

import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.domain.model.NotificationStatus;

public record NotificationSummary(
    Long id,
    String type,
    String title,
    String message,
    String module,
    String referenceType,
    String referenceId,
    NotificationStatus status,
    NotificationPriority priority,
    String payloadJson,
    OffsetDateTime createdAt,
    OffsetDateTime readAt
) {
}


