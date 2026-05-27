package com.pasteleria.catalogos.infrastructure.persistence.entity;

import com.pasteleria.common.persistence.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria_producto")
public class ProductCategoryEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "categoria_id")
  private Long id;

  @Column(name = "codigo", nullable = false, unique = true, length = 40)
  private String code;

  @Column(name = "nombre", nullable = false, unique = true, length = 100)
  private String name;

  @Column(name = "descripcion")
  private String description;

  @Column(name = "orden_visual", nullable = false)
  private int visualOrder;

  @Column(name = "activo", nullable = false)
  private boolean active = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

  public int getVisualOrder() {
    return visualOrder;
  }

  public void setVisualOrder(int visualOrder) {
    this.visualOrder = visualOrder;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}



