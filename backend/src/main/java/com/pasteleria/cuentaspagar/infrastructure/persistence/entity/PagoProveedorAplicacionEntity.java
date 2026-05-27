package com.pasteleria.cuentaspagar.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoPagarEntity;

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
@Table(name = "pago_proveedor_aplicacion")
public class PagoProveedorAplicacionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pago_proveedor_aplicacion_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pago_proveedor_id", nullable = false)
  private PagoProveedorEntity pagoProveedor;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "documento_pagar_id", nullable = false)
  private DocumentoPagarEntity documentoPagar;

  @Column(name = "monto_aplicado", precision = 12, scale = 2, nullable = false)
  private BigDecimal montoAplicado;

  @Column(name = "saldo_anterior", precision = 12, scale = 2, nullable = false)
  private BigDecimal saldoAnterior;

  @Column(name = "saldo_posterior", precision = 12, scale = 2, nullable = false)
  private BigDecimal saldoPosterior;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public PagoProveedorEntity getPagoProveedor() { return pagoProveedor; }
  public void setPagoProveedor(PagoProveedorEntity pagoProveedor) { this.pagoProveedor = pagoProveedor; }
  public DocumentoPagarEntity getDocumentoPagar() { return documentoPagar; }
  public void setDocumentoPagar(DocumentoPagarEntity documentoPagar) { this.documentoPagar = documentoPagar; }
  public BigDecimal getMontoAplicado() { return montoAplicado; }
  public void setMontoAplicado(BigDecimal montoAplicado) { this.montoAplicado = montoAplicado; }
  public BigDecimal getSaldoAnterior() { return saldoAnterior; }
  public void setSaldoAnterior(BigDecimal saldoAnterior) { this.saldoAnterior = saldoAnterior; }
  public BigDecimal getSaldoPosterior() { return saldoPosterior; }
  public void setSaldoPosterior(BigDecimal saldoPosterior) { this.saldoPosterior = saldoPosterior; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
