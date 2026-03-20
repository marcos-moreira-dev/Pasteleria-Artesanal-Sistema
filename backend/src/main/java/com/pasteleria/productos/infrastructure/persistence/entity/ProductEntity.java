package com.pasteleria.productos.infrastructure.persistence.entity;

import java.math.BigDecimal;

import com.pasteleria.catalogos.infrastructure.persistence.entity.ProductCategoryEntity;
import com.pasteleria.common.persistence.AuditableEntity;

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
@Table(name = "producto")
public class ProductEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "producto_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "categoria_id", nullable = false)
  private ProductCategoryEntity category;

  @Column(name = "codigo", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "slug", nullable = false, unique = true, length = 160)
  private String slug;

  @Column(name = "nombre", nullable = false, length = 150)
  private String name;

  @Column(name = "descripcion")
  private String description;

  @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
  private BigDecimal basePrice;

  @Column(name = "requiere_cotizacion", nullable = false)
  private boolean quotationRequired;

  @Column(name = "activo", nullable = false)
  private boolean active = true;

  @Column(name = "publicado", nullable = false)
  private boolean published = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductCategoryEntity getCategory() {
    return category;
  }

  public void setCategory(ProductCategoryEntity category) {
    this.category = category;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  public boolean isQuotationRequired() {
    return quotationRequired;
  }

  public void setQuotationRequired(boolean quotationRequired) {
    this.quotationRequired = quotationRequired;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }
}



