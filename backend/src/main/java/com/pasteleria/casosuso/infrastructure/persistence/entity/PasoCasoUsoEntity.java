package com.pasteleria.casosuso.infrastructure.persistence.entity;

import com.pasteleria.common.persistence.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paso_caso_uso")
public class PasoCasoUsoEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "paso_caso_uso_id")
  private Long id;

  @Column(name = "caso_uso_id", nullable = false)
  private Long casoUsoId;

  @Column(name = "numero", nullable = false)
  private Integer numero;

  @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
  private String descripcion;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCasoUsoId() {
    return casoUsoId;
  }

  public void setCasoUsoId(Long casoUsoId) {
    this.casoUsoId = casoUsoId;
  }

  public Integer getNumero() {
    return numero;
  }

  public void setNumero(Integer numero) {
    this.numero = numero;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
}
