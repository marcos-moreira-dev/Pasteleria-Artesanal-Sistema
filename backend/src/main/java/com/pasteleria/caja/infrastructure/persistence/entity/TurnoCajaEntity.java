package com.pasteleria.caja.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "turno_caja")
public class TurnoCajaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "turno_caja_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "caja_id", nullable = false)
  private CajaOperativaEntity caja;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_apertura_id", nullable = false)
  private UserEntity usuarioApertura;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_cierre_id")
  private UserEntity usuarioCierre;

  @Column(name = "fecha_apertura", nullable = false)
  private OffsetDateTime fechaApertura;

  @Column(name = "fecha_cierre")
  private OffsetDateTime fechaCierre;

  @Column(name = "monto_apertura", nullable = false, precision = 12, scale = 2)
  private BigDecimal montoApertura = BigDecimal.ZERO;

  @Column(name = "monto_cierre_sistema", precision = 12, scale = 2)
  private BigDecimal montoCierreSistema;

  @Column(name = "monto_cierre_declarado", precision = 12, scale = 2)
  private BigDecimal montoCierreDeclarado;

  @Column(name = "diferencia_cierre", precision = 12, scale = 2)
  private BigDecimal diferenciaCierre;

  @Column(name = "estado", nullable = false, length = 20)
  private String estado = "ABIERTO";

  @Column(name = "observaciones_apertura", length = 500)
  private String observacionesApertura;

  @Column(name = "observaciones_cierre", length = 500)
  private String observacionesCierre;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Column(name = "version", nullable = false)
  private Long version = 0L;

  @PrePersist
  void prePersist() {
    OffsetDateTime now = OffsetDateTime.now();
    createdAt = now;
    updatedAt = now;
    if (fechaApertura == null) {
      fechaApertura = now;
    }
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = OffsetDateTime.now();
    version = version == null ? 0L : version + 1;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public CajaOperativaEntity getCaja() { return caja; }
  public void setCaja(CajaOperativaEntity caja) { this.caja = caja; }
  public UserEntity getUsuarioApertura() { return usuarioApertura; }
  public void setUsuarioApertura(UserEntity usuarioApertura) { this.usuarioApertura = usuarioApertura; }
  public UserEntity getUsuarioCierre() { return usuarioCierre; }
  public void setUsuarioCierre(UserEntity usuarioCierre) { this.usuarioCierre = usuarioCierre; }
  public OffsetDateTime getFechaApertura() { return fechaApertura; }
  public void setFechaApertura(OffsetDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
  public OffsetDateTime getFechaCierre() { return fechaCierre; }
  public void setFechaCierre(OffsetDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
  public BigDecimal getMontoApertura() { return montoApertura; }
  public void setMontoApertura(BigDecimal montoApertura) { this.montoApertura = montoApertura; }
  public BigDecimal getMontoCierreSistema() { return montoCierreSistema; }
  public void setMontoCierreSistema(BigDecimal montoCierreSistema) { this.montoCierreSistema = montoCierreSistema; }
  public BigDecimal getMontoCierreDeclarado() { return montoCierreDeclarado; }
  public void setMontoCierreDeclarado(BigDecimal montoCierreDeclarado) { this.montoCierreDeclarado = montoCierreDeclarado; }
  public BigDecimal getDiferenciaCierre() { return diferenciaCierre; }
  public void setDiferenciaCierre(BigDecimal diferenciaCierre) { this.diferenciaCierre = diferenciaCierre; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public String getObservacionesApertura() { return observacionesApertura; }
  public void setObservacionesApertura(String observacionesApertura) { this.observacionesApertura = observacionesApertura; }
  public String getObservacionesCierre() { return observacionesCierre; }
  public void setObservacionesCierre(String observacionesCierre) { this.observacionesCierre = observacionesCierre; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
