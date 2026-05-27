package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_pagar")
public class DocumentoPagarEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "documento_pagar_id")
  private Long documentoPagarId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "documento_compra_id", nullable = false)
  private DocumentoCompraEntity documentoCompra;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "proveedor_id", nullable = false)
  private ProveedorEntity proveedor;

  @Column(nullable = false, unique = true)
  private String codigo;

  @Column(nullable = false)
  private String estado;

  @Column(name = "fecha_emision", nullable = false)
  private LocalDateTime fechaEmision;

  @Column(name = "fecha_vencimiento")
  private LocalDateTime fechaVencimiento;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal total;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal saldo;

  @Column(columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  public Long getDocumentoPagarId() {
    return documentoPagarId;
  }

  public void setDocumentoPagarId(Long documentoPagarId) {
    this.documentoPagarId = documentoPagarId;
  }

  public DocumentoCompraEntity getDocumentoCompra() {
    return documentoCompra;
  }

  public void setDocumentoCompra(DocumentoCompraEntity documentoCompra) {
    this.documentoCompra = documentoCompra;
  }

  public ProveedorEntity getProveedor() {
    return proveedor;
  }

  public void setProveedor(ProveedorEntity proveedor) {
    this.proveedor = proveedor;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public LocalDateTime getFechaEmision() {
    return fechaEmision;
  }

  public void setFechaEmision(LocalDateTime fechaEmision) {
    this.fechaEmision = fechaEmision;
  }

  public LocalDateTime getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
