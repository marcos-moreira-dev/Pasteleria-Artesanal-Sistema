package com.pasteleria.cartera.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;

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
@Table(name = "cobranza")
public class CobranzaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cobranza_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private ClientEntity cliente;

  @Column(nullable = false, unique = true, length = 60)
  private String codigo;

  @Column(name = "fecha_cobranza", nullable = false)
  private LocalDateTime fechaCobranza;

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
  public ClientEntity getCliente() { return cliente; }
  public void setCliente(ClientEntity cliente) { this.cliente = cliente; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }
  public LocalDateTime getFechaCobranza() { return fechaCobranza; }
  public void setFechaCobranza(LocalDateTime fechaCobranza) { this.fechaCobranza = fechaCobranza; }
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
