package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
@Table(name = "insumo")
public class InsumoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "insumo_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "umedida_id", nullable = false)
  private UmedidaEntity umedida;

  @Column(name = "codigo", nullable = false, unique = true, length = 40)
  private String code;

  @Column(name = "nombre", nullable = false, length = 120)
  private String name;

  @Column(name = "descripcion", length = 500)
  private String description;

  @Column(name = "stock_minimo", precision = 12, scale = 4)
  private BigDecimal stockMinimo;

  @Column(name = "stock_actual", precision = 12, scale = 4)
  private BigDecimal stockActual = BigDecimal.ZERO;

  @Column(name = "costo_referencial", precision = 10, scale = 2)
  private BigDecimal costoReferencial;

  @Column(name = "activo", nullable = false)
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UmedidaEntity getUmedida() {
    return umedida;
  }

  public void setUmedida(UmedidaEntity umedida) {
    this.umedida = umedida;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getStockMinimo() {
    return stockMinimo;
  }

  public void setStockMinimo(BigDecimal stockMinimo) {
    this.stockMinimo = stockMinimo;
  }

  public BigDecimal getStockActual() {
    return stockActual;
  }

  public void setStockActual(BigDecimal stockActual) {
    this.stockActual = stockActual;
  }

  public BigDecimal getCostoReferencial() {
    return costoReferencial;
  }

  public void setCostoReferencial(BigDecimal costoReferencial) {
    this.costoReferencial = costoReferencial;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}