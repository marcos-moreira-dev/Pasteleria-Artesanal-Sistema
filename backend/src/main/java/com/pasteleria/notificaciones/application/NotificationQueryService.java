package com.pasteleria.notificaciones.application;

import java.util.List;

import com.pasteleria.common.config.NotificationProperties;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.notificaciones.application.port.NotificationRepositoryPort;
import com.pasteleria.notificaciones.domain.model.NotificationStatus;
import com.pasteleria.notificaciones.application.mapper.NotificationDtoMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NotificationQueryService {

  private static final int MAX_LIMIT = 25;

  private final NotificationRepositoryPort notificationRepository;
  private final AuthenticatedUserService authenticatedUserService;
  private final NotificationDtoMapper notificationDtoMapper;
  private final NotificationProperties notificationProperties;

  public NotificationQueryService(
      NotificationRepositoryPort notificationRepository,
      AuthenticatedUserService authenticatedUserService,
      NotificationDtoMapper notificationDtoMapper,
      NotificationProperties notificationProperties
  ) {
    this.notificationRepository = notificationRepository;
    this.authenticatedUserService = authenticatedUserService;
    this.notificationDtoMapper = notificationDtoMapper;
    this.notificationProperties = notificationProperties;
  }

  public List<NotificationSummary> latestNotifications(Integer limit) {
    int resolvedLimit = normalizeLimit(limit);
    Long userId = authenticatedUserService.currentUser()
        .orElseThrow(() -> new BusinessRuleException("No hay un usuario autenticado para consultar notificaciones."))
        .getId();

    return notificationRepository.findByDestinationUserIdAndStatusNotOrderByCreatedAtDesc(
            userId,
            NotificationStatus.ARCHIVADA,
            PageRequest.of(0, resolvedLimit)
        )
        .stream()
        .map(notificationDtoMapper::toSummary)
        .toList();
  }

  public NotificationCounters counters() {
    Long userId = authenticatedUserService.currentUser()
        .orElseThrow(() -> new BusinessRuleException("No hay un usuario autenticado para consultar notificaciones."))
        .getId();
    return new NotificationCounters(
        notificationRepository.countByDestinationUserIdAndStatus(userId, NotificationStatus.NO_LEIDA)
    );
  }

  private int normalizeLimit(Integer limit) {
    if (limit == null || limit < 1) {
      return notificationProperties.defaultLimit();
    }
    return Math.min(limit, MAX_LIMIT);
  }
}


