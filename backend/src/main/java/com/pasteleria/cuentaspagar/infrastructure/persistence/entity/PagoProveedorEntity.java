package com.pasteleria.cuentaspagar.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;

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
@Table(name = "pago_proveedor")
public class PagoProveedorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pago_proveedor_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "proveedor_id", nullable = false)
  private ProveedorEntity proveedor;

  @Column(nullable = false, unique = true, length = 60)
  private String codigo;

  @Column(name = "fecha_pago", nullable = false)
  private LocalDateTime fechaPago;

  @Column(name = "monto_total", precision = 12, scale = 2, nullable = false)
  private BigDecimal montoTotal;

  @Column(name = "medio_pago", length = 40)
  private String medioPago;

  @Column(name = "referencia_pago", length = 120)
  private String referenciaPago;

  @Column(nullable = false, length = 30)
  private String estado;

  @Column(columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public ProveedorEntity getProveedor() { return proveedor; }
  public void setProveedor(ProveedorEntity proveedor) { this.proveedor = proveedor; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }
  public LocalDateTime getFechaPago() { return fechaPago; }
  public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
  public BigDecimal getMontoTotal() { return montoTotal; }
  public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
  public String getMedioPago() { return medioPago; }
  public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
  public String getReferenciaPago() { return referenciaPago; }
  public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
