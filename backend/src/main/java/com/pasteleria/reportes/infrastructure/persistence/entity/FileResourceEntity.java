package com.pasteleria.reportes.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import com.pasteleria.reportes.domain.model.FileResourceStatus;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

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
@Table(name = "archivo_recurso")
public class FileResourceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "archivo_id")
  private Long id;

  @Column(name = "codigo_archivo", nullable = false, unique = true, length = 80)
  private String fileCode;

  @Column(name = "origen_modulo", nullable = false, length = 40)
  private String sourceModule;

  @Column(name = "tipo_archivo", nullable = false, length = 40)
  private String fileType;

  @Column(name = "nombre_original", nullable = false, length = 255)
  private String originalName;

  @Column(name = "nombre_fisico", nullable = false, length = 255)
  private String physicalName;

  @Column(name = "mime_type", nullable = false, length = 120)
  private String mimeType;

  @Column(name = "extension", length = 20)
  private String extension;

  @Column(name = "tamano_bytes", nullable = false)
  private long sizeBytes;

  @Column(name = "checksum", length = 128)
  private String checksum;

  @Column(name = "ruta_relativa", nullable = false, length = 500)
  private String relativePath;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 30)
  private FileResourceStatus status;

  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "fecha_expiracion")
  private OffsetDateTime expirationAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creado_por_usuario_id")
  private UserEntity createdByUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFileCode() {
    return fileCode;
  }

  public void setFileCode(String fileCode) {
    this.fileCode = fileCode;
  }

  public String getSourceModule() {
    return sourceModule;
  }

  public void setSourceModule(String sourceModule) {
    this.sourceModule = sourceModule;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getOriginalName() {
    return originalName;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  public String getPhysicalName() {
    return physicalName;
  }

  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public long getSizeBytes() {
    return sizeBytes;
  }

  public void setSizeBytes(long sizeBytes) {
    this.sizeBytes = sizeBytes;
  }

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public FileResourceStatus getStatus() {
    return status;
  }

  public void setStatus(FileResourceStatus status) {
    this.status = status;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getExpirationAt() {
    return expirationAt;
  }

  public void setExpirationAt(OffsetDateTime expirationAt) {
    this.expirationAt = expirationAt;
  }

  public UserEntity getCreatedByUser() {
    return createdByUser;
  }

  public void setCreatedByUser(UserEntity createdByUser) {
    this.createdByUser = createdByUser;
  }
}



