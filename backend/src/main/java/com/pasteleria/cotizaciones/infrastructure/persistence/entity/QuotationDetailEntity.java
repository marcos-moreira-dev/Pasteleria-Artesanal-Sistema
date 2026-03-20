package com.pasteleria.cotizaciones.infrastructure.persistence.entity;

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

@Entity
@Table(name = "cotizacion_detalle")
public class QuotationDetailEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cotizacion_detalle_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cotizacion_id", nullable = false)
  private QuotationEntity quotation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id")
  private ProductEntity product;

  @Column(name = "descripcion_item", nullable = false)
  private String itemDescription;

  @Column(name = "cantidad", nullable = false)
  private int quantity;

  @Column(name = "precio_estimado", nullable = false, precision = 10, scale = 2)
  private BigDecimal estimatedPrice;

  @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "notas")
  private String notes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public QuotationEntity getQuotation() {
    return quotation;
  }

  public void setQuotation(QuotationEntity quotation) {
    this.quotation = quotation;
  }

  public ProductEntity getProduct() {
    return product;
  }

  public void setProduct(ProductEntity product) {
    this.product = product;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getEstimatedPrice() {
    return estimatedPrice;
  }

  public void setEstimatedPrice(BigDecimal estimatedPrice) {
    this.estimatedPrice = estimatedPrice;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }
}



