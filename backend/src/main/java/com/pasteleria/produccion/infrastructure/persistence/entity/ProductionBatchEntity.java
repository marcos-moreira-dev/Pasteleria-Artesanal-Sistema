package com.pasteleria.produccion.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderDetailEntity;
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

/** Lote documental de producto terminado generado por producción. */
@Entity
@Table(name = "lote_produccion")
public class ProductionBatchEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lote_produccion_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "produccion_id", nullable = false)
  private ProductionEntity production;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pedido_detalle_id", nullable = false)
  private OrderDetailEntity orderDetail;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "producto_id", nullable = false)
  private ProductEntity product;

  @Column(name = "codigo_lote", nullable = false, unique = true, length = 80)
  private String codigoLote;

  @Column(name = "fecha_produccion", nullable = false)
  private OffsetDateTime fechaProduccion = OffsetDateTime.now();

  @Column(name = "cantidad_producida", nullable = false, precision = 12, scale = 4)
  private BigDecimal cantidadProducida;

  @Column(name = "cantidad_disponible", nullable = false, precision = 12, scale = 4)
  private BigDecimal cantidadDisponible;

  @Column(name = "costo_total_estimado", precision = 12, scale = 4)
  private BigDecimal costoTotalEstimado;

  @Column(name = "estado", nullable = false, length = 30)
  private String estado = "DISPONIBLE";

  @Column(name = "observaciones", length = 500)
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public ProductionEntity getProduction() { return production; }
  public void setProduction(ProductionEntity production) { this.production = production; }
  public OrderDetailEntity getOrderDetail() { return orderDetail; }
  public void setOrderDetail(OrderDetailEntity orderDetail) { this.orderDetail = orderDetail; }
  public ProductEntity getProduct() { return product; }
  public void setProduct(ProductEntity product) { this.product = product; }
  public String getCodigoLote() { return codigoLote; }
  public void setCodigoLote(String codigoLote) { this.codigoLote = codigoLote; }
  public OffsetDateTime getFechaProduccion() { return fechaProduccion; }
  public void setFechaProduccion(OffsetDateTime fechaProduccion) { this.fechaProduccion = fechaProduccion; }
  public BigDecimal getCantidadProducida() { return cantidadProducida; }
  public void setCantidadProducida(BigDecimal cantidadProducida) { this.cantidadProducida = cantidadProducida; }
  public BigDecimal getCantidadDisponible() { return cantidadDisponible; }
  public void setCantidadDisponible(BigDecimal cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
  public BigDecimal getCostoTotalEstimado() { return costoTotalEstimado; }
  public void setCostoTotalEstimado(BigDecimal costoTotalEstimado) { this.costoTotalEstimado = costoTotalEstimado; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
