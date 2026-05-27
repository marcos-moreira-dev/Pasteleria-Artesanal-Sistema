package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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

/**
 * Entidad JPA que representa un Ingrediente en el sistema.
 * 
 * <h2>DESCRIPCIÓN</h2>
 * Un ingrediente es una materia prima comestible utilizada en la elaboración
 * de productos de pastelería. Ejemplos: harina, azúcar, huevos, mantequilla, etc.
 * 
 * <h2>DIFERENCIA CON INSUMO</h2>
 * <ul>
 *   <li><b>Ingrediente</b>: Materia prima que forma parte de la receta (comestible)</li>
 *   <li><b>Insumo</b>: Artículos de empaque y decoración (no comestible)</li>
 * </ul>
 * 
 * Aunque ambas entidades tienen estructura similar, se mantienen separadas
 * por razones semánticas y para facilitar reporting/análisis de costos.
 * 
 * <h2>CAMPOS PRINCIPALES</h2>
 * <ul>
 *   <li><b>code</b>: Código SKU único (HAR-001, AZU-002, etc.)</li>
 *   <li><b>name</b>: Nombre descriptivo del ingrediente</li>
 *   <li><b>stockMinimo</b>: Nivel mínimo de stock para generar alertas</li>
 *   <li><b>stockActual</b>: Cantidad actual en inventario (BigDecimal para precisión)</li>
 *   <li><b>costoReferencial</b>: Precio de referencia para cálculo de costos</li>
 *   <li><b>umedida</b>: Unidad de medida (kg, g, ml, l, unidades)</li>
 * </ul>
 * 
 * <h2>CONTROL DE STOCK</h2>
 * <pre>
 * Estado del stock:
 * - AGOTADO: stockActual = 0
 * - CRÍTICO: stockActual <= stockMinimo * 0.5
 * - BAJO: stockActual <= stockMinimo
 * - NORMAL: stockActual > stockMinimo
 * </pre>
 * 
 * <h2>PRECISIÓN DECIMAL</h2>
 * Se usa {@link BigDecimal} en lugar de double/float para evitar
 * errores de redondeo en cálculos monetarios y de cantidad:
 * <ul>
 *   <li>stockMinimo: precision=12, scale=4 (hasta 4 decimales)</li>
 *   <li>stockActual: precision=12, scale=4</li>
 *   <li>costoReferencial: precision=10, scale=2 (formato monetario)</li>
 * </ul>
 * 
 * <h2>RELACIONES</h2>
 * <ul>
 *   <li>Many-to-One con {@link UmedidaEntity} (unidad de medida)</li>
 *   <li>One-to-Many con {@link DetalleRecetaEntity} (ingredientes de recetas)</li>
 *   <li>One-to-Many con {@link ItemProveedorEntity} (catálogo de proveedores)</li>
 *   <li>Indirecto: Movimientos de inventario via item_tipo/item_id</li>
 * </ul>
 * 
 * <h2>MOVIMIENTOS DE INVENTARIO</h2>
 * Los movimientos se registran en la tabla {@code inventario_movimiento}
 * usando un sistema polimórfico con campos item_tipo e item_id:
 * <ul>
 *   <li>item_tipo = "INGREDIENTE"</li>
 *   <li>item_id = ingrediente_id</li>
 * </ul>
 * 
 * <h2>CÁLCULO DE COSTOS EN RECETAS</h2>
 * <pre>
 * costoReceta = Σ (cantidadIngrediente × costoReferencial)
 * 
 * Ejemplo:
 * - Harina 500g × $1.20 = $600
 * - Azúcar 400g × $1.10 = $440
 * - Total = $1,040
 * </pre>
 * 
 * @see InsumoEntity
 * @see UmedidaEntity
 * @see DetalleRecetaEntity
 * @author Pastelería Development Team
 */
@Entity
@Table(name = "ingrediente")
public class IngredienteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ingrediente_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "umedida_id", nullable = false)
  private UmedidaEntity umedida;

  @Column(name = "codigo", nullable = false, unique = true, length = 40)
  private String code;

  @Column(name = "nombre", nullable = false, length = 120)
  private String name;

  @Column(name = "descripcion", length = 500)
  private String description;

  @Column(name = "stock_minimo", precision = 12, scale = 4)
  private BigDecimal stockMinimo;

  @Column(name = "stock_actual", precision = 12, scale = 4)
  private BigDecimal stockActual = BigDecimal.ZERO;

  @Column(name = "costo_referencial", precision = 10, scale = 2)
  private BigDecimal costoReferencial;

  @Column(name = "activo", nullable = false)
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UmedidaEntity getUmedida() {
    return umedida;
  }

  public void setUmedida(UmedidaEntity umedida) {
    this.umedida = umedida;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getStockMinimo() {
    return stockMinimo;
  }

  public void setStockMinimo(BigDecimal stockMinimo) {
    this.stockMinimo = stockMinimo;
  }

  public BigDecimal getStockActual() {
    return stockActual;
  }

  public void setStockActual(BigDecimal stockActual) {
    this.stockActual = stockActual;
  }

  public BigDecimal getCostoReferencial() {
    return costoReferencial;
  }

  public void setCostoReferencial(BigDecimal costoReferencial) {
    this.costoReferencial = costoReferencial;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}