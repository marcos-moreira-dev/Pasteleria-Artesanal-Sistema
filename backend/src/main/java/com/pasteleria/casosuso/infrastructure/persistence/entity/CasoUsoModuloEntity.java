package com.pasteleria.casosuso.infrastructure.persistence.entity;

import com.pasteleria.common.persistence.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "caso_uso_modulo")
public class CasoUsoModuloEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "caso_uso_modulo_id")
  private Long id;

  @Column(name = "codigo", nullable = false, unique = true, length = 80)
  private String codigo;

  @Column(name = "nombre", nullable = false, length = 120)
  private String nombre;

  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "grupo", nullable = false, length = 80)
  private String grupo = "OPERACION";

  @Column(name = "orden_visual", nullable = false)
  private Integer ordenVisual = 999;

  @Column(name = "activo", nullable = false)
  private boolean activo = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public Integer getOrdenVisual() {
    return ordenVisual;
  }

  public void setOrdenVisual(Integer ordenVisual) {
    this.ordenVisual = ordenVisual;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }
}
