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
@Table(name = "arqueo_caja")
public class ArqueoCajaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "arqueo_caja_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "turno_caja_id", nullable = false)
  private TurnoCajaEntity turno;

  @Column(name = "fecha_arqueo", nullable = false)
  private OffsetDateTime fechaArqueo;

  @Column(name = "monto_sistema", nullable = false, precision = 12, scale = 2)
  private BigDecimal montoSistema;

  @Column(name = "monto_declarado", nullable = false, precision = 12, scale = 2)
  private BigDecimal montoDeclarado;

  @Column(name = "diferencia", nullable = false, precision = 12, scale = 2)
  private BigDecimal diferencia;

  @Column(name = "estado", nullable = false, length = 20)
  private String estado = "CONFIRMADO";

  @Column(name = "observaciones", length = 500)
  private String observaciones;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creado_por_usuario_id", nullable = false)
  private UserEntity creadoPor;

  @PrePersist
  void prePersist() {
    if (fechaArqueo == null) {
      fechaArqueo = OffsetDateTime.now();
    }
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public TurnoCajaEntity getTurno() { return turno; }
  public void setTurno(TurnoCajaEntity turno) { this.turno = turno; }
  public OffsetDateTime getFechaArqueo() { return fechaArqueo; }
  public void setFechaArqueo(OffsetDateTime fechaArqueo) { this.fechaArqueo = fechaArqueo; }
  public BigDecimal getMontoSistema() { return montoSistema; }
  public void setMontoSistema(BigDecimal montoSistema) { this.montoSistema = montoSistema; }
  public BigDecimal getMontoDeclarado() { return montoDeclarado; }
  public void setMontoDeclarado(BigDecimal montoDeclarado) { this.montoDeclarado = montoDeclarado; }
  public BigDecimal getDiferencia() { return diferencia; }
  public void setDiferencia(BigDecimal diferencia) { this.diferencia = diferencia; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public UserEntity getCreadoPor() { return creadoPor; }
  public void setCreadoPor(UserEntity creadoPor) { this.creadoPor = creadoPor; }
}
