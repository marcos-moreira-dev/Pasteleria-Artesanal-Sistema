package com.pasteleria.common.audit.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auditoria_evento")
public class AuditEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "auditoria_evento_id")
  private Long id;

  @Column(name = "codigo_evento", nullable = false, length = 80)
  private String eventCode;

  @Column(name = "modulo", nullable = false, length = 40)
  private String module;

  @Column(name = "entidad", nullable = false, length = 80)
  private String entity;

  @Column(name = "entidad_id", nullable = false, length = 80)
  private String entityId;

  @Column(name = "accion", nullable = false, length = 80)
  private String action;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "actor_usuario_id")
  private UserEntity actorUser;

  @Column(name = "actor_rol", length = 80)
  private String actorRole;

  @Column(name = "fecha_evento", nullable = false)
  private OffsetDateTime eventAt;

  @Column(name = "motivo")
  private String reason;

  @Column(name = "valor_anterior_json", columnDefinition = "jsonb")
  @ColumnTransformer(write = "?::jsonb")
  private String previousValueJson;

  @Column(name = "valor_nuevo_json", columnDefinition = "jsonb")
  @ColumnTransformer(write = "?::jsonb")
  private String newValueJson;

  @Column(name = "request_id", length = 120)
  private String requestId;

  @Column(name = "ip_origen", length = 64)
  private String ipAddress;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEventCode() {
    return eventCode;
  }

  public void setEventCode(String eventCode) {
    this.eventCode = eventCode;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getEntity() {
    return entity;
  }

  public void setEntity(String entity) {
    this.entity = entity;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public UserEntity getActorUser() {
    return actorUser;
  }

  public void setActorUser(UserEntity actorUser) {
    this.actorUser = actorUser;
  }

  public String getActorRole() {
    return actorRole;
  }

  public void setActorRole(String actorRole) {
    this.actorRole = actorRole;
  }

  public OffsetDateTime getEventAt() {
    return eventAt;
  }

  public void setEventAt(OffsetDateTime eventAt) {
    this.eventAt = eventAt;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getPreviousValueJson() {
    return previousValueJson;
  }

  public void setPreviousValueJson(String previousValueJson) {
    this.previousValueJson = previousValueJson;
  }

  public String getNewValueJson() {
    return newValueJson;
  }

  public void setNewValueJson(String newValueJson) {
    this.newValueJson = newValueJson;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
}



