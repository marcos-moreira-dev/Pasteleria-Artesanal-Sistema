package com.pasteleria.notificaciones.application;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Set;

import com.pasteleria.notificaciones.application.port.NotificationRepositoryPort;
import com.pasteleria.notificaciones.infrastructure.persistence.entity.NotificationEntity;
import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.domain.model.NotificationStatus;
import com.pasteleria.usuarios.application.port.UserRepositoryPort;
import com.pasteleria.usuarios.domain.model.RoleCode;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Publica notificaciones internas sin acoplar los modulos operativos al detalle
 * de persistencia del buzon administrativo.
 */
@Service
public class NotificationPublishService {

  private final NotificationRepositoryPort notificationRepository;
  private final UserRepositoryPort userRepository;

  public NotificationPublishService(NotificationRepositoryPort notificationRepository, UserRepositoryPort userRepository) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void notifyUser(UserEntity destinationUser, NotificationCreateCommand command) {
    notificationRepository.save(buildNotification(destinationUser, command));
  }

  @Transactional
  public void notifyRoles(Collection<RoleCode> roles, NotificationCreateCommand command) {
    if (roles == null || roles.isEmpty()) {
      return;
    }

    Set<Long> delivered = new java.util.LinkedHashSet<>();
    for (UserEntity user : userRepository.findByRoleCodeInAndActiveTrue(roles)) {
      if (delivered.add(user.getId())) {
        notificationRepository.save(buildNotification(user, command));
      }
    }
  }

  private NotificationEntity buildNotification(UserEntity destinationUser, NotificationCreateCommand command) {
    NotificationEntity notification = new NotificationEntity();
    notification.setDestinationUser(destinationUser);
    notification.setNotificationType(command.type());
    notification.setTitle(command.title());
    notification.setMessage(command.message());
    notification.setModule(command.module());
    notification.setReferenceType(command.referenceType());
    notification.setReferenceId(command.referenceId());
    notification.setPriority(command.priority() == null ? NotificationPriority.MEDIA : command.priority());
    notification.setStatus(NotificationStatus.NO_LEIDA);
    notification.setPayloadJson(command.payloadJson() == null || command.payloadJson().isBlank() ? "{}" : command.payloadJson());
    notification.setCreatedAt(OffsetDateTime.now());
    return notification;
  }
}


