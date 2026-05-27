package com.pasteleria.notificaciones.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.domain.model.NotificationStatus;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacion")
public class NotificationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notificacion_id")
  private Long id;

  @Column(name = "tipo_notificacion", nullable = false, length = 60)
  private String notificationType;

  @Column(name = "titulo", nullable = false, length = 160)
  private String title;

  @Column(name = "mensaje", nullable = false)
  private String message;

  @Column(name = "modulo", nullable = false, length = 40)
  private String module;

  @Column(name = "referencia_tipo", length = 40)
  private String referenceType;

  @Column(name = "referencia_id", length = 80)
  private String referenceId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_destino_id", nullable = false)
  private UserEntity destinationUser;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 30)
  private NotificationStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "prioridad", nullable = false, length = 20)
  private NotificationPriority priority;

  @Column(name = "payload_json", nullable = false, columnDefinition = "jsonb")
  @ColumnTransformer(write = "?::jsonb")
  private String payloadJson = "{}";

  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "fecha_lectura")
  private OffsetDateTime readAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(String referenceType) {
    this.referenceType = referenceType;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }

  public UserEntity getDestinationUser() {
    return destinationUser;
  }

  public void setDestinationUser(UserEntity destinationUser) {
    this.destinationUser = destinationUser;
  }

  public NotificationStatus getStatus() {
    return status;
  }

  public void setStatus(NotificationStatus status) {
    this.status = status;
  }

  public NotificationPriority getPriority() {
    return priority;
  }

  public void setPriority(NotificationPriority priority) {
    this.priority = priority;
  }

  public String getPayloadJson() {
    return payloadJson;
  }

  public void setPayloadJson(String payloadJson) {
    this.payloadJson = payloadJson;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(OffsetDateTime readAt) {
    this.readAt = readAt;
  }
}



