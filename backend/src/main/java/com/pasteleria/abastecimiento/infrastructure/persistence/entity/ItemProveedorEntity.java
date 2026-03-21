package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "item_proveedor")
public class ItemProveedorEntity {

  public enum ItemTipo {
    INGREDIENTE, INSUMO
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_proveedor_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "item_tipo", nullable = false, length = 20)
  private ItemTipo itemTipo;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "proveedor_id", nullable = false)
  private ProveedorEntity proveedor;

  @Column(name = "precio_suministro", precision = 10, scale = 2)
  private BigDecimal precioSuministro;

  @Column(name = "es_principal", nullable = false)
  private boolean esPrincipal = false;

  @Column(name = "activo", nullable = false)
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ItemTipo getItemTipo() {
    return itemTipo;
  }

  public void setItemTipo(ItemTipo itemTipo) {
    this.itemTipo = itemTipo;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public ProveedorEntity getProveedor() {
    return proveedor;
  }

  public void setProveedor(ProveedorEntity proveedor) {
    this.proveedor = proveedor;
  }

  public BigDecimal getPrecioSuministro() {
    return precioSuministro;
  }

  public void setPrecioSuministro(BigDecimal precioSuministro) {
    this.precioSuministro = precioSuministro;
  }

  public boolean isEsPrincipal() {
    return esPrincipal;
  }

  public void setEsPrincipal(boolean esPrincipal) {
    this.esPrincipal = esPrincipal;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }
}