package com.pasteleria.produccion.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Entrada documental de producto terminado generada desde un lote de producción. */
@Entity
@Table(name = "entrada_producto_terminado")
public class FinishedProductEntryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "entrada_producto_terminado_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "lote_produccion_id", nullable = false)
  private ProductionBatchEntity batch;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "produccion_id", nullable = false)
  private ProductionEntity production;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "producto_id", nullable = false)
  private ProductEntity product;

  @Column(name = "cantidad", nullable = false, precision = 12, scale = 4)
  private BigDecimal cantidad;

  @Column(name = "costo_total_estimado", precision = 12, scale = 4)
  private BigDecimal costoTotalEstimado;

  @Column(name = "observaciones", length = 500)
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public ProductionBatchEntity getBatch() { return batch; }
  public void setBatch(ProductionBatchEntity batch) { this.batch = batch; }
  public ProductionEntity getProduction() { return production; }
  public void setProduction(ProductionEntity production) { this.production = production; }
  public ProductEntity getProduct() { return product; }
  public void setProduct(ProductEntity product) { this.product = product; }
  public BigDecimal getCantidad() { return cantidad; }
  public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
  public BigDecimal getCostoTotalEstimado() { return costoTotalEstimado; }
  public void setCostoTotalEstimado(BigDecimal costoTotalEstimado) { this.costoTotalEstimado = costoTotalEstimado; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
