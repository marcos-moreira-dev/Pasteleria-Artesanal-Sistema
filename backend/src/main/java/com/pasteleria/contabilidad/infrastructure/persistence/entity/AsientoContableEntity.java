package com.pasteleria.contabilidad.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "asiento_contable")
public class AsientoContableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "asiento_contable_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tipo_diario_contable_id", nullable = false)
  private TipoDiarioContableEntity tipoDiario;

  @Column(nullable = false, unique = true, length = 60)
  private String codigo;

  @Column(name = "fecha_asiento", nullable = false)
  private LocalDateTime fechaAsiento;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "origen_tipo", length = 80)
  private String origenTipo;

  @Column(name = "origen_id", length = 80)
  private String origenId;

  @Column(nullable = false, length = 30)
  private String estado;

  @Column(name = "total_debe", precision = 12, scale = 2, nullable = false)
  private BigDecimal totalDebe;

  @Column(name = "total_haber", precision = 12, scale = 2, nullable = false)
  private BigDecimal totalHaber;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public TipoDiarioContableEntity getTipoDiario() { return tipoDiario; }
  public void setTipoDiario(TipoDiarioContableEntity tipoDiario) { this.tipoDiario = tipoDiario; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }
  public LocalDateTime getFechaAsiento() { return fechaAsiento; }
  public void setFechaAsiento(LocalDateTime fechaAsiento) { this.fechaAsiento = fechaAsiento; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public String getOrigenTipo() { return origenTipo; }
  public void setOrigenTipo(String origenTipo) { this.origenTipo = origenTipo; }
  public String getOrigenId() { return origenId; }
  public void setOrigenId(String origenId) { this.origenId = origenId; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public BigDecimal getTotalDebe() { return totalDebe; }
  public void setTotalDebe(BigDecimal totalDebe) { this.totalDebe = totalDebe; }
  public BigDecimal getTotalHaber() { return totalHaber; }
  public void setTotalHaber(BigDecimal totalHaber) { this.totalHaber = totalHaber; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
