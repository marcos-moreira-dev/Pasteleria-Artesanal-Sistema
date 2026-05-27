package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import java.math.BigDecimal;

import com.pasteleria.common.persistence.AuditableEntity;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una Receta de producción.
 * 
 * <h2>DESCRIPCIÓN</h2>
 * Una receta es una fórmula que define los ingredientes, cantidades y procesos
 * necesarios para elaborar un producto de pastelería. Incluye el cálculo
 * de costos estimados para la producción.
 * 
 * <h2>COMPONENTES DE UNA RECETA</h2>
 * <ul>
 *   <li><b>Nombre</b>: Identificador descriptivo de la receta</li>
 *   <li><b>Producto</b>: Producto final que se obtiene (opcional)</li>
 *   <li><b>Rendimiento</b>: Cantidad de unidades que produce la receta base</li>
 *   <li><b>Ingredientes</b>: Lista de ingredientes con sus cantidades</li>
 *   <li><b>Costo estimado</b>: Costo calculado de todos los ingredientes</li>
 * </ul>
 * 
 * <h2>RENDIMIENTO BASE</h2>
 * El rendimiento base indica cuántas unidades produce la receta tal cual está definida.
 * <pre>
 * Ejemplo:
 * - Rendimiento base = 12
 * - Significa: Esta receta produce 12 unidades
 * - Si se necesitan 24 unidades, se multiplican las cantidades × 2
 * 
 * Cálculo de cantidad para producción:
 * cantidadReal = cantidadBase × (cantidadDeseada / rendimientoBase)
 * </pre>
 * 
 * <h2>CÁLCULO DE COSTOS</h2>
 * El costo estimado se calcula automáticamente:
 * <pre>
 * costoEstimado = Σ (cantidadIngrediente × costoReferencialIngrediente)
 * 
 * Ejemplo real - Torta de Chocolate:
 * - Harina 500g × $1.20/kg = $0.60
 * - Azúcar 400g × $1.10/kg = $0.44
 * - Huevos 6 × $0.50/u = $3.00
 * - Mantequilla 250g × $4.50/kg = $1.125
 * - ... otros ingredientes ...
 * - Total ≈ $8.50 (ejemplo simplificado)
 * </pre>
 * 
 * <h2>REGLAS DE NEGOCIO</h2>
 * <ul>
 *   <li><b>Receta única activa</b>: Solo una receta activa por producto</li>
 *   <li><b>Historial de recetas</b>: Las recetas antiguas se desactivan, no se eliminan</li>
 *   <li><b>Ingredientes obligatorios</b>: Una receta debe tener al menos un ingrediente</li>
 *   <li><b>Validación de cantidades</b>: Todas las cantidades deben ser positivas</li>
 * </ul>
 * 
 * <h2>ACTIVACIÓN/DESACTIVACIÓN</h2>
 * El campo {@code esActiva} controla si la receta está disponible para producción:
 * <ul>
 *   <li>Activa (true): Puede usarse en órdenes de producción</li>
 *   <li>Inactiva (false): Histórico, no se usa para nuevas producciones</li>
 * </ul>
 * 
 * <h2>RELACIONES</h2>
 * <ul>
 *   <li>Many-to-One con {@link ProductEntity}: Producto que se elabora</li>
 *   <li>One-to-Many con {@link DetalleRecetaEntity}: Ingredientes y cantidades</li>
 *   <li>Many-to-One con {@link UserEntity}: Usuario que creó la receta</li>
 * </ul>
 * 
 * <h2>AUDITORÍA</h2>
 * Extiende {@link AuditableEntity} que proporciona:
 * <ul>
 *   <li>createdAt: Fecha de creación</li>
 *   <li>updatedAt: Fecha de última modificación</li>
 *   <li>createdBy: Usuario creador</li>
 *   <li>lastModifiedBy: Usuario que modificó</li>
 * </ul>
 * 
 * <h2>EJEMPLO DE RECETA</h2>
 * <pre>
 * Receta: "Cupcakes de Vainilla - Base (12 unidades)"
 * Producto: Cupcake de Vainilla
 * Rendimiento: 12
 * EsActiva: true
 * 
 * Detalles:
 * - Harina 0000: 250g
 * - Azúcar blanca: 220g
 * - Huevos: 3 unidades
 * - Mantequilla: 120g
 * - Leche: 120ml
 * - Esencia vainilla: 10ml
 * 
 * Costo estimado: $5.80
 * Costo por unidad: $5.80 / 12 = $0.48
 * </pre>
 * 
 * @see DetalleRecetaEntity
 * @see ProductEntity
 * @see AuditableEntity
 * @author Pastelería Development Team
 */
@Entity
@Table(name = "receta")
public class RecetaEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "receta_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id")
  private ProductEntity producto;

  @Column(name = "nombre", nullable = false, length = 160)
  private String nombre;

  @Column(name = "rendimiento_base", nullable = false, precision = 10, scale = 2)
  private BigDecimal rendimientoBase = BigDecimal.ONE;

  @Column(name = "costo_estimado", precision = 10, scale = 4)
  private BigDecimal costoEstimado;

  @Column(name = "observaciones")
  private String observaciones;

  @Column(name = "es_activa", nullable = false)
  private Boolean esActiva = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by_user_id")
  private UserEntity createdBy;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductEntity getProducto() {
    return producto;
  }

  public void setProducto(ProductEntity producto) {
    this.producto = producto;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public BigDecimal getRendimientoBase() {
    return rendimientoBase;
  }

  public void setRendimientoBase(BigDecimal rendimientoBase) {
    this.rendimientoBase = rendimientoBase;
  }

  public BigDecimal getCostoEstimado() {
    return costoEstimado;
  }

  public void setCostoEstimado(BigDecimal costoEstimado) {
    this.costoEstimado = costoEstimado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Boolean getEsActiva() {
    return esActiva;
  }

  public void setEsActiva(Boolean esActiva) {
    this.esActiva = esActiva;
  }

  public UserEntity getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserEntity createdBy) {
    this.createdBy = createdBy;
  }
}
