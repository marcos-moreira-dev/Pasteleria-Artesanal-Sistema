package com.pasteleria.reportes.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import com.pasteleria.reportes.domain.model.ReportJobStatus;
import com.pasteleria.reportes.domain.model.ReportType;
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
import jakarta.persistence.Version;

@Entity
@Table(name = "job_reporte")
public class ReportJobEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "job_reporte_id")
  private Long id;

  @Column(name = "codigo_job", nullable = false, unique = true, length = 80)
  private String jobCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_reporte", nullable = false, length = 60)
  private ReportType reportType;

  @Column(name = "parametros_json", nullable = false, columnDefinition = "jsonb")
  @ColumnTransformer(write = "?::jsonb")
  private String parametersJson = "{}";

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 30)
  private ReportJobStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "solicitado_por_usuario_id")
  private UserEntity requestedByUser;

  @Column(name = "fecha_solicitud", nullable = false, updatable = false)
  private OffsetDateTime requestedAt;

  @Column(name = "fecha_inicio")
  private OffsetDateTime startedAt;

  @Column(name = "fecha_fin")
  private OffsetDateTime finishedAt;

  @Column(name = "intentos", nullable = false)
  private short attempts;

  @Column(name = "mensaje_error")
  private String errorMessage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "archivo_id")
  private FileResourceEntity fileResource;

  @Column(name = "request_id", length = 120)
  private String requestId;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getJobCode() {
    return jobCode;
  }

  public void setJobCode(String jobCode) {
    this.jobCode = jobCode;
  }

  public ReportType getReportType() {
    return reportType;
  }

  public void setReportType(ReportType reportType) {
    this.reportType = reportType;
  }

  public String getParametersJson() {
    return parametersJson;
  }

  public void setParametersJson(String parametersJson) {
    this.parametersJson = parametersJson;
  }

  public ReportJobStatus getStatus() {
    return status;
  }

  public void setStatus(ReportJobStatus status) {
    this.status = status;
  }

  public UserEntity getRequestedByUser() {
    return requestedByUser;
  }

  public void setRequestedByUser(UserEntity requestedByUser) {
    this.requestedByUser = requestedByUser;
  }

  public OffsetDateTime getRequestedAt() {
    return requestedAt;
  }

  public void setRequestedAt(OffsetDateTime requestedAt) {
    this.requestedAt = requestedAt;
  }

  public OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public OffsetDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }

  public short getAttempts() {
    return attempts;
  }

  public void setAttempts(short attempts) {
    this.attempts = attempts;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public FileResourceEntity getFileResource() {
    return fileResource;
  }

  public void setFileResource(FileResourceEntity fileResource) {
    this.fileResource = fileResource;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}



