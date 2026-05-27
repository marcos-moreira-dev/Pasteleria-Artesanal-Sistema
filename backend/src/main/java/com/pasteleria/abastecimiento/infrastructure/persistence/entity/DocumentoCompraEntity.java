package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_compra")
public class DocumentoCompraEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "documento_compra_id")
  private Long documentoCompraId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orden_compra_id", nullable = false)
  private OrdenCompraEntity ordenCompra;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "proveedor_id", nullable = false)
  private ProveedorEntity proveedor;

  @Column(name = "numero_documento", nullable = false, unique = true)
  private String numeroDocumento;

  @Column(nullable = false)
  private String estado;

  @Column(name = "fecha_emision", nullable = false)
  private LocalDateTime fechaEmision;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal subtotal;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal impuesto;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal total;

  @Column(columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  public Long getDocumentoCompraId() {
    return documentoCompraId;
  }

  public void setDocumentoCompraId(Long documentoCompraId) {
    this.documentoCompraId = documentoCompraId;
  }

  public OrdenCompraEntity getOrdenCompra() {
    return ordenCompra;
  }

  public void setOrdenCompra(OrdenCompraEntity ordenCompra) {
    this.ordenCompra = ordenCompra;
  }

  public ProveedorEntity getProveedor() {
    return proveedor;
  }

  public void setProveedor(ProveedorEntity proveedor) {
    this.proveedor = proveedor;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
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

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getImpuesto() {
    return impuesto;
  }

  public void setImpuesto(BigDecimal impuesto) {
    this.impuesto = impuesto;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
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
