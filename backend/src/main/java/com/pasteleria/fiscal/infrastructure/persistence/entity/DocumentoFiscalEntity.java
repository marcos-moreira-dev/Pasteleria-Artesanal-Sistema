package com.pasteleria.fiscal.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "documento_fiscal")
public class DocumentoFiscalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "documento_fiscal_id")
  private Long id;

  @Column(nullable = false, unique = true, length = 60)
  private String codigo;

  @Column(name = "tipo_comprobante", nullable = false, length = 40)
  private String tipoComprobante;

  @Column(nullable = false, length = 30)
  private String estado;

  @Column(name = "documento_cobrar_id")
  private Long documentoCobrarId;

  @Column(name = "documento_compra_id")
  private Long documentoCompraId;

  @Column(name = "origen_tipo", nullable = false, length = 40)
  private String origenTipo;

  @Column(name = "tercero_tipo", nullable = false, length = 30)
  private String terceroTipo;

  @Column(name = "tercero_id", nullable = false)
  private Long terceroId;

  @Column(name = "tercero_nombre", nullable = false, length = 160)
  private String terceroNombre;

  @Column(name = "fecha_emision", nullable = false)
  private LocalDateTime fechaEmision;

  @Column(nullable = false, length = 3)
  private String establecimiento;

  @Column(name = "punto_emision", nullable = false, length = 3)
  private String puntoEmision;

  @Column(nullable = false, length = 9)
  private String secuencial;

  @Column(name = "numero_comprobante", nullable = false, unique = true, length = 20)
  private String numeroComprobante;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal subtotal;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal impuesto;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal total;

  @Column(name = "clave_acceso", length = 60)
  private String claveAcceso;

  @Column(name = "numero_autorizacion", length = 80)
  private String numeroAutorizacion;

  @Column(name = "fecha_autorizacion")
  private LocalDateTime fechaAutorizacion;

  @Column(length = 20)
  private String ambiente;

  @Column(columnDefinition = "TEXT")
  private String observaciones;

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
  public String getTipoComprobante() { return tipoComprobante; }
  public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public Long getDocumentoCobrarId() { return documentoCobrarId; }
  public void setDocumentoCobrarId(Long documentoCobrarId) { this.documentoCobrarId = documentoCobrarId; }
  public Long getDocumentoCompraId() { return documentoCompraId; }
  public void setDocumentoCompraId(Long documentoCompraId) { this.documentoCompraId = documentoCompraId; }
  public String getOrigenTipo() { return origenTipo; }
  public void setOrigenTipo(String origenTipo) { this.origenTipo = origenTipo; }
  public String getTerceroTipo() { return terceroTipo; }
  public void setTerceroTipo(String terceroTipo) { this.terceroTipo = terceroTipo; }
  public Long getTerceroId() { return terceroId; }
  public void setTerceroId(Long terceroId) { this.terceroId = terceroId; }
  public String getTerceroNombre() { return terceroNombre; }
  public void setTerceroNombre(String terceroNombre) { this.terceroNombre = terceroNombre; }
  public LocalDateTime getFechaEmision() { return fechaEmision; }
  public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
  public String getEstablecimiento() { return establecimiento; }
  public void setEstablecimiento(String establecimiento) { this.establecimiento = establecimiento; }
  public String getPuntoEmision() { return puntoEmision; }
  public void setPuntoEmision(String puntoEmision) { this.puntoEmision = puntoEmision; }
  public String getSecuencial() { return secuencial; }
  public void setSecuencial(String secuencial) { this.secuencial = secuencial; }
  public String getNumeroComprobante() { return numeroComprobante; }
  public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }
  public BigDecimal getSubtotal() { return subtotal; }
  public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
  public BigDecimal getImpuesto() { return impuesto; }
  public void setImpuesto(BigDecimal impuesto) { this.impuesto = impuesto; }
  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }
  public String getClaveAcceso() { return claveAcceso; }
  public void setClaveAcceso(String claveAcceso) { this.claveAcceso = claveAcceso; }
  public String getNumeroAutorizacion() { return numeroAutorizacion; }
  public void setNumeroAutorizacion(String numeroAutorizacion) { this.numeroAutorizacion = numeroAutorizacion; }
  public LocalDateTime getFechaAutorizacion() { return fechaAutorizacion; }
  public void setFechaAutorizacion(LocalDateTime fechaAutorizacion) { this.fechaAutorizacion = fechaAutorizacion; }
  public String getAmbiente() { return ambiente; }
  public void setAmbiente(String ambiente) { this.ambiente = ambiente; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
