package com.pasteleria.pedidos.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.common.persistence.AuditableEntity;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationEntity;
import com.pasteleria.pedidos.domain.model.OrderOrigin;
import com.pasteleria.pedidos.domain.model.OrderPriority;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class OrderEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pedido_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private ClientEntity client;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cotizacion_id", unique = true)
  private QuotationEntity quotation;

  @Column(name = "codigo", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "fecha_pedido", nullable = false, updatable = false)
  private OffsetDateTime orderDate;

  @Column(name = "fecha_entrega_estimada", nullable = false)
  private OffsetDateTime estimatedDeliveryAt;

  @Column(name = "fecha_entrega_real")
  private OffsetDateTime actualDeliveryAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_pedido", nullable = false, length = 40)
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "prioridad", nullable = false, length = 20)
  private OrderPriority priority;

  @Enumerated(EnumType.STRING)
  @Column(name = "origen", nullable = false, length = 20)
  private OrderOrigin origin;

  @Column(name = "observaciones")
  private String notes;

  @Column(name = "total_estimado", precision = 10, scale = 2)
  private BigDecimal estimatedTotal;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrderDetailEntity> details = new LinkedHashSet<>();

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private ProductionEntity production;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ClientEntity getClient() {
    return client;
  }

  public void setClient(ClientEntity client) {
    this.client = client;
  }

  public QuotationEntity getQuotation() {
    return quotation;
  }

  public void setQuotation(QuotationEntity quotation) {
    this.quotation = quotation;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public OffsetDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(OffsetDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public OffsetDateTime getEstimatedDeliveryAt() {
    return estimatedDeliveryAt;
  }

  public void setEstimatedDeliveryAt(OffsetDateTime estimatedDeliveryAt) {
    this.estimatedDeliveryAt = estimatedDeliveryAt;
  }

  public OffsetDateTime getActualDeliveryAt() {
    return actualDeliveryAt;
  }

  public void setActualDeliveryAt(OffsetDateTime actualDeliveryAt) {
    this.actualDeliveryAt = actualDeliveryAt;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public OrderPriority getPriority() {
    return priority;
  }

  public void setPriority(OrderPriority priority) {
    this.priority = priority;
  }

  public OrderOrigin getOrigin() {
    return origin;
  }

  public void setOrigin(OrderOrigin origin) {
    this.origin = origin;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public BigDecimal getEstimatedTotal() {
    return estimatedTotal;
  }

  public void setEstimatedTotal(BigDecimal estimatedTotal) {
    this.estimatedTotal = estimatedTotal;
  }

  public Set<OrderDetailEntity> getDetails() {
    return details;
  }

  public void setDetails(Set<OrderDetailEntity> details) {
    this.details = details;
  }

  public ProductionEntity getProduction() {
    return production;
  }

  public void setProduction(ProductionEntity production) {
    this.production = production;
  }
}



