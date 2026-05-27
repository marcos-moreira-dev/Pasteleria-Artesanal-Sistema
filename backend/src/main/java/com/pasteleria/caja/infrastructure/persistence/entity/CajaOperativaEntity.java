package com.pasteleria.caja.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "caja_operativa")
public class CajaOperativaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "caja_id")
  private Long id;

  @Column(name = "sucursal_codigo", nullable = false, length = 40)
  private String sucursalCodigo = "MATRIZ";

  @Column(name = "codigo", nullable = false, unique = true, length = 40)
  private String codigo;

  @Column(name = "nombre", nullable = false, length = 120)
  private String nombre;

  @Column(name = "tipo_caja", nullable = false, length = 30)
  private String tipoCaja = "VENTA";

  @Column(name = "moneda", nullable = false, length = 3)
  private String moneda = "USD";

  @Column(name = "saldo_actual", nullable = false, precision = 12, scale = 2)
  private BigDecimal saldoActual = BigDecimal.ZERO;

  @Column(name = "activa", nullable = false)
  private boolean activa = true;

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
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = OffsetDateTime.now();
    version = version == null ? 0L : version + 1;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getSucursalCodigo() { return sucursalCodigo; }
  public void setSucursalCodigo(String sucursalCodigo) { this.sucursalCodigo = sucursalCodigo; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public String getTipoCaja() { return tipoCaja; }
  public void setTipoCaja(String tipoCaja) { this.tipoCaja = tipoCaja; }
  public String getMoneda() { return moneda; }
  public void setMoneda(String moneda) { this.moneda = moneda; }
  public BigDecimal getSaldoActual() { return saldoActual; }
  public void setSaldoActual(BigDecimal saldoActual) { this.saldoActual = saldoActual; }
  public boolean isActiva() { return activa; }
  public void setActiva(boolean activa) { this.activa = activa; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
