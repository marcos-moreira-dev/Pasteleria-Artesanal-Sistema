package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_compra")
public class OrdenCompraEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orden_compra_id")
  private Long ordenCompraId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "proveedor_id", nullable = false)
  private ProveedorEntity proveedor;

  @Column(nullable = false, unique = true)
  private String codigo;

  @Column(nullable = false)
  private String estado;

  @Column(name = "fecha_emision")
  private LocalDateTime fechaEmision;

  @Column(name = "fecha_entrega_esperada")
  private LocalDateTime fechaEntregaEsperada;

  @Column(name = "fecha_entrega_real")
  private LocalDateTime fechaEntregaReal;

  @Column(name = "total_estimado", precision = 12, scale = 2)
  private BigDecimal totalEstimado;

  @Column(columnDefinition = "TEXT")
  private String observaciones;

  private Boolean activo;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<OrdenCompraDetalleEntity> detalles = new ArrayList<>();

  // Getters and Setters
  public Long getOrdenCompraId() {
    return ordenCompraId;
  }

  public void setOrdenCompraId(Long ordenCompraId) {
    this.ordenCompraId = ordenCompraId;
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

  public LocalDateTime getFechaEntregaEsperada() {
    return fechaEntregaEsperada;
  }

  public void setFechaEntregaEsperada(LocalDateTime fechaEntregaEsperada) {
    this.fechaEntregaEsperada = fechaEntregaEsperada;
  }

  public LocalDateTime getFechaEntregaReal() {
    return fechaEntregaReal;
  }

  public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) {
    this.fechaEntregaReal = fechaEntregaReal;
  }

  public BigDecimal getTotalEstimado() {
    return totalEstimado;
  }

  public void setTotalEstimado(BigDecimal totalEstimado) {
    this.totalEstimado = totalEstimado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
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

  public List<OrdenCompraDetalleEntity> getDetalles() {
    return detalles;
  }

  public void setDetalles(List<OrdenCompraDetalleEntity> detalles) {
    this.detalles = detalles;
  }
}
