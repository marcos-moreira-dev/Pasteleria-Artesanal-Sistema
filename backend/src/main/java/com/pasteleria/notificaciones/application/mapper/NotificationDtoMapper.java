package com.pasteleria.notificaciones.application.mapper;

import com.pasteleria.notificaciones.infrastructure.persistence.entity.NotificationEntity;
import com.pasteleria.notificaciones.application.NotificationSummary;

import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

  public NotificationSummary toSummary(NotificationEntity notification) {
    return new NotificationSummary(
        notification.getId(),
        notification.getNotificationType(),
        notification.getTitle(),
        notification.getMessage(),
        notification.getModule(),
        notification.getReferenceType(),
        notification.getReferenceId(),
        notification.getStatus(),
        notification.getPriority(),
        notification.getPayloadJson(),
        notification.getCreatedAt(),
        notification.getReadAt()
    );
  }
}


