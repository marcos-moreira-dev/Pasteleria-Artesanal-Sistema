package com.pasteleria.cartera.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "documento_cobrar")
public class DocumentoCobrarEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "documento_cobrar_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private ClientEntity cliente;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pedido_id", unique = true)
  private OrderEntity pedido;

  @Column(nullable = false, unique = true, length = 60)
  private String codigo;

  @Column(nullable = false, length = 30)
  private String estado;

  @Column(name = "fecha_emision", nullable = false)
  private LocalDateTime fechaEmision;

  @Column(name = "fecha_vencimiento")
  private LocalDateTime fechaVencimiento;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal total;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal saldo;

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
  public OrderEntity getPedido() { return pedido; }
  public void setPedido(OrderEntity pedido) { this.pedido = pedido; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public LocalDateTime getFechaEmision() { return fechaEmision; }
  public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
  public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
  public void setFechaVencimiento(LocalDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }
  public BigDecimal getSaldo() { return saldo; }
  public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
