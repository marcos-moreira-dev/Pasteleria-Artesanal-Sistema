package com.pasteleria.casosuso.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import com.pasteleria.common.persistence.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "caso_uso_operativo")
public class CasoUsoOperativoEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "caso_uso_id")
  private Long id;

  @Column(name = "modulo", nullable = false, length = 80)
  private String modulo;

  @Column(name = "codigo", nullable = false, unique = true, length = 40)
  private String codigo;

  @Column(name = "titulo", nullable = false, length = 180)
  private String titulo;

  @Column(name = "actor_principal", length = 80)
  private String actorPrincipal;

  @Column(name = "objetivo", columnDefinition = "TEXT")
  private String objetivo;

  @Column(name = "punto_inicio", length = 220)
  private String puntoInicio;

  @Column(name = "orden_visual", nullable = false)
  private Integer ordenVisual = 999;

  @Column(name = "estado", nullable = false, length = 40)
  private String estado = "LISTO";

  @Column(name = "version_flujo", nullable = false)
  private Integer versionFlujo = 1;

  @Column(name = "actualizado_en")
  private OffsetDateTime actualizadoEn;

  @Column(name = "activo", nullable = false)
  private boolean activo = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getModulo() {
    return modulo;
  }

  public void setModulo(String modulo) {
    this.modulo = modulo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getActorPrincipal() {
    return actorPrincipal;
  }

  public void setActorPrincipal(String actorPrincipal) {
    this.actorPrincipal = actorPrincipal;
  }

  public String getObjetivo() {
    return objetivo;
  }

  public void setObjetivo(String objetivo) {
    this.objetivo = objetivo;
  }

  public String getPuntoInicio() {
    return puntoInicio;
  }

  public void setPuntoInicio(String puntoInicio) {
    this.puntoInicio = puntoInicio;
  }

  public Integer getOrdenVisual() {
    return ordenVisual;
  }

  public void setOrdenVisual(Integer ordenVisual) {
    this.ordenVisual = ordenVisual;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Integer getVersionFlujo() {
    return versionFlujo;
  }

  public void setVersionFlujo(Integer versionFlujo) {
    this.versionFlujo = versionFlujo;
  }

  public OffsetDateTime getActualizadoEn() {
    return actualizadoEn;
  }

  public void setActualizadoEn(OffsetDateTime actualizadoEn) {
    this.actualizadoEn = actualizadoEn;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }
}
