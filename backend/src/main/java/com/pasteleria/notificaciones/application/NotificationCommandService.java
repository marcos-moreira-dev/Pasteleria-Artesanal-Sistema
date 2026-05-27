package com.pasteleria.notificaciones.application;

import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.notificaciones.infrastructure.persistence.entity.NotificationEntity;
import com.pasteleria.notificaciones.application.port.NotificationRepositoryPort;
import com.pasteleria.notificaciones.domain.model.NotificationStatus;
import com.pasteleria.notificaciones.application.mapper.NotificationDtoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationCommandService {

  private final NotificationRepositoryPort notificationRepository;
  private final AuthenticatedUserService authenticatedUserService;
  private final NotificationDtoMapper notificationDtoMapper;

  public NotificationCommandService(
      NotificationRepositoryPort notificationRepository,
      AuthenticatedUserService authenticatedUserService,
      NotificationDtoMapper notificationDtoMapper
  ) {
    this.notificationRepository = notificationRepository;
    this.authenticatedUserService = authenticatedUserService;
    this.notificationDtoMapper = notificationDtoMapper;
  }

  @Transactional
  public NotificationSummary markAsRead(Long notificationId) {
    NotificationEntity notification = findOwnedNotification(notificationId);
    if (notification.getStatus() == NotificationStatus.NO_LEIDA) {
      notification.setStatus(NotificationStatus.LEIDA);
      notification.setReadAt(OffsetDateTime.now());
    }
    return notificationDtoMapper.toSummary(notification);
  }

  @Transactional
  public NotificationSummary archive(Long notificationId) {
    NotificationEntity notification = findOwnedNotification(notificationId);
    archiveNotification(notification);
    return notificationDtoMapper.toSummary(notification);
  }

  @Transactional
  public NotificationActionResult archiveSelection(NotificationSelectionRequest request) {
    Long userId = currentUserId();
    List<NotificationEntity> notifications = notificationRepository.findByIdInAndDestinationUserId(
        request.notificationIds(),
        userId
    );

    notifications.forEach(this::archiveNotification);
    return new NotificationActionResult(notifications.size());
  }

  @Transactional
  public NotificationActionResult archiveReadNotifications() {
    return new NotificationActionResult(notificationRepository.archiveReadNotificationsByDestinationUserId(currentUserId()));
  }

  private void archiveNotification(NotificationEntity notification) {
    if (notification.getStatus() == NotificationStatus.NO_LEIDA) {
      notification.setReadAt(OffsetDateTime.now());
    }
    notification.setStatus(NotificationStatus.ARCHIVADA);
  }

  private NotificationEntity findOwnedNotification(Long notificationId) {
    return notificationRepository.findById(notificationId)
        .filter(notification -> notification.getDestinationUser().getId().equals(currentUserId()))
        .orElseThrow(() -> new ResourceNotFoundException("La notificacion indicada no existe para esta sesion."));
  }

  private Long currentUserId() {
    return authenticatedUserService.currentUser()
        .orElseThrow(() -> new ResourceNotFoundException("No se pudo resolver el usuario autenticado."))
        .getId();
  }
}


