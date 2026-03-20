package com.pasteleria.common.audit;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.common.audit.application.port.AuditEventRepositoryPort;
import com.pasteleria.common.audit.infrastructure.persistence.entity.AuditEventEntity;
import com.pasteleria.common.security.AuthenticatedUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Registra auditoria de negocio con contexto suficiente para soporte y trazabilidad.
 */
@Service
public class AuditTrailService {

  private final AuditEventRepositoryPort auditEventRepository;
  private final AuthenticatedUserService authenticatedUserService;
  private final ObjectMapper objectMapper;

  public AuditTrailService(
      AuditEventRepositoryPort auditEventRepository,
      AuthenticatedUserService authenticatedUserService,
      ObjectMapper objectMapper
  ) {
    this.auditEventRepository = auditEventRepository;
    this.authenticatedUserService = authenticatedUserService;
    this.objectMapper = objectMapper;
  }

  /**
   * Persiste un evento de auditoria ligado al actor autenticado y al request actual.
   */
  @Transactional
  public void record(
      String eventCode,
      String module,
      String entity,
      String entityId,
      String action,
      String reason,
      HttpServletRequest request
  ) {
    recordChange(eventCode, module, entity, entityId, action, null, null, reason, request);
  }

  /**
   * Registra auditoria con una fotografia de antes y despues cuando la
   * operacion cambia datos sensibles o etapas operativas.
   */
  @Transactional
  public void recordChange(
      String eventCode,
      String module,
      String entity,
      String entityId,
      String action,
      Object previousValue,
      Object newValue,
      String reason,
      HttpServletRequest request
  ) {
    AuditEventEntity event = new AuditEventEntity();
    event.setEventCode(eventCode);
    event.setModule(module);
    event.setEntity(entity);
    event.setEntityId(entityId);
    event.setAction(action);
    event.setReason(reason);
    event.setEventAt(OffsetDateTime.now());
    event.setPreviousValueJson(toJson(previousValue));
    event.setNewValueJson(toJson(newValue));
    // RequestId e IP permiten correlacionar auditoria de negocio con logs tecnicos y soporte.
    event.setRequestId(request != null ? request.getHeader("X-Request-Id") : null);
    event.setIpAddress(request != null ? request.getRemoteAddr() : null);

    authenticatedUserService.currentUser().ifPresent(user -> {
      event.setActorUser(user);
      event.setActorRole(user.getRole().getCode().name());
    });

    auditEventRepository.save(event);
  }

  private String toJson(Object value) {
    if (value == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException("No se pudo serializar el detalle de auditoria.", exception);
    }
  }
}


