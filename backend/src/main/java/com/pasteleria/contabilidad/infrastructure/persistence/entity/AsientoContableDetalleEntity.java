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

@Entity
@Table(name = "asiento_contable_detalle")
public class AsientoContableDetalleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "asiento_contable_detalle_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "asiento_contable_id", nullable = false)
  private AsientoContableEntity asiento;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cuenta_contable_id", nullable = false)
  private CuentaContableEntity cuenta;

  @Column(columnDefinition = "TEXT")
  private String descripcion;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal debe;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal haber;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public AsientoContableEntity getAsiento() { return asiento; }
  public void setAsiento(AsientoContableEntity asiento) { this.asiento = asiento; }
  public CuentaContableEntity getCuenta() { return cuenta; }
  public void setCuenta(CuentaContableEntity cuenta) { this.cuenta = cuenta; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public BigDecimal getDebe() { return debe; }
  public void setDebe(BigDecimal debe) { this.debe = debe; }
  public BigDecimal getHaber() { return haber; }
  public void setHaber(BigDecimal haber) { this.haber = haber; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
