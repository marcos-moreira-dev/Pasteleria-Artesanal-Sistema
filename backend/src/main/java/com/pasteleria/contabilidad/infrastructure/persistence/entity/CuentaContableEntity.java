package com.pasteleria.contabilidad.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "cuenta_contable")
public class CuentaContableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cuenta_contable_id")
  private Long id;

  @Column(nullable = false, unique = true, length = 40)
  private String codigo;

  @Column(nullable = false, length = 160)
  private String nombre;

  @Column(name = "tipo_cuenta", nullable = false, length = 30)
  private String tipoCuenta;

  @Column(nullable = false, length = 20)
  private String naturaleza;

  @Column(nullable = false)
  private Integer nivel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cuenta_padre_id")
  private CuentaContableEntity cuentaPadre;

  @Column(nullable = false)
  private Boolean imputable;

  @Column(nullable = false)
  private Boolean activa;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public String getTipoCuenta() { return tipoCuenta; }
  public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
  public String getNaturaleza() { return naturaleza; }
  public void setNaturaleza(String naturaleza) { this.naturaleza = naturaleza; }
  public Integer getNivel() { return nivel; }
  public void setNivel(Integer nivel) { this.nivel = nivel; }
  public CuentaContableEntity getCuentaPadre() { return cuentaPadre; }
  public void setCuentaPadre(CuentaContableEntity cuentaPadre) { this.cuentaPadre = cuentaPadre; }
  public Boolean getImputable() { return imputable; }
  public void setImputable(Boolean imputable) { this.imputable = imputable; }
  public Boolean getActiva() { return activa; }
  public void setActiva(Boolean activa) { this.activa = activa; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
