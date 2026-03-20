package com.pasteleria.notificaciones.infrastructure.persistence.repository;

import com.pasteleria.notificaciones.application.port.NotificationRepositoryPort;
import com.pasteleria.notificaciones.domain.model.NotificationStatus;
import com.pasteleria.notificaciones.infrastructure.persistence.entity.NotificationEntity;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, NotificationRepositoryPort {

  Page<NotificationEntity> findByDestinationUserIdAndStatusNotOrderByCreatedAtDesc(
      Long userId,
      NotificationStatus status,
      Pageable pageable
  );

  long countByDestinationUserIdAndStatus(Long userId, NotificationStatus status);

  List<NotificationEntity> findByIdInAndDestinationUserId(List<Long> notificationIds, Long userId);

  @Modifying
  @Query("""
      update NotificationEntity notification
         set notification.status = com.pasteleria.notificaciones.domain.model.NotificationStatus.ARCHIVADA
       where notification.destinationUser.id = :userId
         and notification.status = com.pasteleria.notificaciones.domain.model.NotificationStatus.LEIDA
      """)
  int archiveReadNotificationsByDestinationUserId(Long userId);

  @Modifying
  @Query("""
      update NotificationEntity notification
         set notification.status = com.pasteleria.notificaciones.domain.model.NotificationStatus.ARCHIVADA
       where notification.status = com.pasteleria.notificaciones.domain.model.NotificationStatus.LEIDA
         and notification.readAt is not null
         and notification.readAt < :cutoff
      """)
  int archiveReadNotificationsOlderThan(OffsetDateTime cutoff);
}



