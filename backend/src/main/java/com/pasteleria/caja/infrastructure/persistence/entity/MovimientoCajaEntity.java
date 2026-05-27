package com.pasteleria.caja.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimiento_caja")
public class MovimientoCajaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "movimiento_caja_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "turno_caja_id", nullable = false)
  private TurnoCajaEntity turno;

  @Column(name = "tipo_movimiento", nullable = false, length = 40)
  private String tipoMovimiento;

  @Column(name = "naturaleza", nullable = false, length = 20)
  private String naturaleza;

  @Column(name = "fecha_movimiento", nullable = false)
  private OffsetDateTime fechaMovimiento;

  @Column(name = "monto", nullable = false, precision = 12, scale = 2)
  private BigDecimal monto;

  @Column(name = "moneda", nullable = false, length = 3)
  private String moneda = "USD";

  @Column(name = "referencia_tipo", length = 80)
  private String referenciaTipo;

  @Column(name = "referencia_id", length = 120)
  private String referenciaId;

  @Column(name = "descripcion", length = 500)
  private String descripcion;

  @Column(name = "estado", nullable = false, length = 20)
  private String estado = "REGISTRADO";

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creado_por_usuario_id", nullable = false)
  private UserEntity creadoPor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "anulado_por_usuario_id")
  private UserEntity anuladoPor;

  @Column(name = "anulado_en")
  private OffsetDateTime anuladoEn;

  @Column(name = "motivo_anulacion", length = 500)
  private String motivoAnulacion;

  @PrePersist
  void prePersist() {
    if (fechaMovimiento == null) {
      fechaMovimiento = OffsetDateTime.now();
    }
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public TurnoCajaEntity getTurno() { return turno; }
  public void setTurno(TurnoCajaEntity turno) { this.turno = turno; }
  public String getTipoMovimiento() { return tipoMovimiento; }
  public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
  public String getNaturaleza() { return naturaleza; }
  public void setNaturaleza(String naturaleza) { this.naturaleza = naturaleza; }
  public OffsetDateTime getFechaMovimiento() { return fechaMovimiento; }
  public void setFechaMovimiento(OffsetDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
  public BigDecimal getMonto() { return monto; }
  public void setMonto(BigDecimal monto) { this.monto = monto; }
  public String getMoneda() { return moneda; }
  public void setMoneda(String moneda) { this.moneda = moneda; }
  public String getReferenciaTipo() { return referenciaTipo; }
  public void setReferenciaTipo(String referenciaTipo) { this.referenciaTipo = referenciaTipo; }
  public String getReferenciaId() { return referenciaId; }
  public void setReferenciaId(String referenciaId) { this.referenciaId = referenciaId; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public UserEntity getCreadoPor() { return creadoPor; }
  public void setCreadoPor(UserEntity creadoPor) { this.creadoPor = creadoPor; }
  public UserEntity getAnuladoPor() { return anuladoPor; }
  public void setAnuladoPor(UserEntity anuladoPor) { this.anuladoPor = anuladoPor; }
  public OffsetDateTime getAnuladoEn() { return anuladoEn; }
  public void setAnuladoEn(OffsetDateTime anuladoEn) { this.anuladoEn = anuladoEn; }
  public String getMotivoAnulacion() { return motivoAnulacion; }
  public void setMotivoAnulacion(String motivoAnulacion) { this.motivoAnulacion = motivoAnulacion; }
}
