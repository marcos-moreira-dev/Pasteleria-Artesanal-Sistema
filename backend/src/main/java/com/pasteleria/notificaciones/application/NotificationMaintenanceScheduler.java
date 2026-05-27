package com.pasteleria.notificaciones.application;

import java.time.OffsetDateTime;

import com.pasteleria.common.config.NotificationProperties;
import com.pasteleria.notificaciones.application.port.NotificationRepositoryPort;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mantiene el buzon interno sin crecer indefinidamente archivando lecturas
 * antiguas.
 */
@Component
public class NotificationMaintenanceScheduler {

  private final NotificationRepositoryPort notificationRepository;
  private final NotificationProperties notificationProperties;

  public NotificationMaintenanceScheduler(
      NotificationRepositoryPort notificationRepository,
      NotificationProperties notificationProperties
  ) {
    this.notificationRepository = notificationRepository;
    this.notificationProperties = notificationProperties;
  }

  @Transactional
  @Scheduled(cron = "${app.notification.archive-cron:0 0 3 * * *}")
  public void archiveOldReadNotifications() {
    notificationRepository.archiveReadNotificationsOlderThan(
        OffsetDateTime.now().minusDays(notificationProperties.archiveReadAfterDays())
    );
  }
}


