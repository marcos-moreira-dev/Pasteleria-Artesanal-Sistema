package com.pasteleria.notificaciones.application.port;

import com.pasteleria.notificaciones.domain.model.NotificationStatus;
import com.pasteleria.notificaciones.infrastructure.persistence.entity.NotificationEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryPort {

  Page<NotificationEntity> findByDestinationUserIdAndStatusNotOrderByCreatedAtDesc(
      Long userId,
      NotificationStatus status,
      Pageable pageable
  );

  long countByDestinationUserIdAndStatus(Long userId, NotificationStatus status);

  int archiveReadNotificationsOlderThan(OffsetDateTime cutoff);

  int archiveReadNotificationsByDestinationUserId(Long userId);

  Optional<NotificationEntity> findById(Long notificationId);

  List<NotificationEntity> findByIdInAndDestinationUserId(List<Long> notificationIds, Long userId);

  NotificationEntity save(NotificationEntity notification);
}
