package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "detalle_receta")
public class DetalleRecetaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "detalle_receta_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "receta_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private RecetaEntity receta;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ingrediente_id", nullable = false)
  private IngredienteEntity ingrediente;

  @Column(name = "cantidad_base", nullable = false, precision = 10, scale = 4)
  private BigDecimal cantidadBase;

  @Column(name = "rendimiento_por_unidad", nullable = false, precision = 10, scale = 4)
  private BigDecimal rendimientoPorUnidad = BigDecimal.ONE;

  @Column(name = "es_para_porcion", nullable = false)
  private Boolean esParaPorcion = true;

  @Column(name = "observaciones")
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RecetaEntity getReceta() {
    return receta;
  }

  public void setReceta(RecetaEntity receta) {
    this.receta = receta;
  }

  public IngredienteEntity getIngrediente() {
    return ingrediente;
  }

  public void setIngrediente(IngredienteEntity ingrediente) {
    this.ingrediente = ingrediente;
  }

  public BigDecimal getCantidadBase() {
    return cantidadBase;
  }

  public void setCantidadBase(BigDecimal cantidadBase) {
    this.cantidadBase = cantidadBase;
  }

  public BigDecimal getRendimientoPorUnidad() {
    return rendimientoPorUnidad;
  }

  public void setRendimientoPorUnidad(BigDecimal rendimientoPorUnidad) {
    this.rendimientoPorUnidad = rendimientoPorUnidad;
  }

  public Boolean getEsParaPorcion() {
    return esParaPorcion;
  }

  public void setEsParaPorcion(Boolean esParaPorcion) {
    this.esParaPorcion = esParaPorcion;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
