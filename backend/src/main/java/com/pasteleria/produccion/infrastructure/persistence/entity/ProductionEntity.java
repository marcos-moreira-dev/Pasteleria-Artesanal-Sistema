package com.pasteleria.produccion.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import com.pasteleria.common.persistence.AuditableEntity;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produccion")
public class ProductionEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "produccion_id")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pedido_id", nullable = false, unique = true)
  private OrderEntity order;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_produccion", nullable = false, length = 40)
  private ProductionStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "prioridad_produccion", nullable = false, length = 20)
  private ProductionPriority priority;

  @Column(name = "fecha_inicio")
  private OffsetDateTime startedAt;

  @Column(name = "fecha_finalizacion")
  private OffsetDateTime finishedAt;

  @Column(name = "observaciones_produccion")
  private String productionNotes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderEntity getOrder() {
    return order;
  }

  public void setOrder(OrderEntity order) {
    this.order = order;
  }

  public ProductionStatus getStatus() {
    return status;
  }

  public void setStatus(ProductionStatus status) {
    this.status = status;
  }

  public ProductionPriority getPriority() {
    return priority;
  }

  public void setPriority(ProductionPriority priority) {
    this.priority = priority;
  }

  public OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public OffsetDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }

  public String getProductionNotes() {
    return productionNotes;
  }

  public void setProductionNotes(String productionNotes) {
    this.productionNotes = productionNotes;
  }
}



