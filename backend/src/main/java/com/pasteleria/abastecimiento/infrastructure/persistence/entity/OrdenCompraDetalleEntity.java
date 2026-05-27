package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_compra_detalle")
public class OrdenCompraDetalleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orden_compra_detalle_id")
  private Long ordenCompraDetalleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orden_compra_id", nullable = false)
  private OrdenCompraEntity ordenCompra;

  @Column(name = "item_tipo", nullable = false)
  private String itemTipo;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @Column(name = "item_nombre", nullable = false)
  private String itemNombre;

  @Column(name = "item_codigo", nullable = false)
  private String itemCodigo;

  @Column(nullable = false)
  private Integer cantidad;

  @Column(name = "precio_unitario", precision = 12, scale = 4)
  private BigDecimal precioUnitario;

  @Column(precision = 12, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "cantidad_recibida")
  private Integer cantidadRecibida;

  @Column(columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // Getters and Setters
  public Long getOrdenCompraDetalleId() {
    return ordenCompraDetalleId;
  }

  public void setOrdenCompraDetalleId(Long ordenCompraDetalleId) {
    this.ordenCompraDetalleId = ordenCompraDetalleId;
  }

  public OrdenCompraEntity getOrdenCompra() {
    return ordenCompra;
  }

  public void setOrdenCompra(OrdenCompraEntity ordenCompra) {
    this.ordenCompra = ordenCompra;
  }

  public String getItemTipo() {
    return itemTipo;
  }

  public void setItemTipo(String itemTipo) {
    this.itemTipo = itemTipo;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getItemNombre() {
    return itemNombre;
  }

  public void setItemNombre(String itemNombre) {
    this.itemNombre = itemNombre;
  }

  public String getItemCodigo() {
    return itemCodigo;
  }

  public void setItemCodigo(String itemCodigo) {
    this.itemCodigo = itemCodigo;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public Integer getCantidadRecibida() {
    return cantidadRecibida;
  }

  public void setCantidadRecibida(Integer cantidadRecibida) {
    this.cantidadRecibida = cantidadRecibida;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
