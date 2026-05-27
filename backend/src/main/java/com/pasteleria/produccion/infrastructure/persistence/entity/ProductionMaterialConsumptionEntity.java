package com.pasteleria.produccion.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderDetailEntity;

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
 * Consumo real de materiales generado al finalizar una producción.
 *
 * <p>Esta entidad es transicional V1: consume ingredientes actuales y enlaza
 * con inventario_movimiento. La V2 ERP normalizada la migrará a
 * produccion.consumo_material.</p>
 */
@Entity
@Table(name = "consumo_material_produccion")
public class ProductionMaterialConsumptionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "consumo_material_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "produccion_id", nullable = false)
  private ProductionEntity production;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pedido_detalle_id", nullable = false)
  private OrderDetailEntity orderDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receta_id")
  private RecetaEntity receta;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "detalle_receta_id")
  private DetalleRecetaEntity detalleReceta;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ingrediente_id", nullable = false)
  private IngredienteEntity ingrediente;

  @Column(name = "cantidad_teorica", nullable = false, precision = 12, scale = 4)
  private BigDecimal cantidadTeorica;

  @Column(name = "cantidad_consumida", nullable = false, precision = 12, scale = 4)
  private BigDecimal cantidadConsumida;

  @Column(name = "costo_unitario", precision = 12, scale = 4)
  private BigDecimal costoUnitario;

  @Column(name = "costo_total", precision = 12, scale = 4)
  private BigDecimal costoTotal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movimiento_inventario_id")
  private InventarioMovimientoEntity movimientoInventario;

  @Column(name = "observaciones", length = 500)
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public ProductionEntity getProduction() { return production; }
  public void setProduction(ProductionEntity production) { this.production = production; }
  public OrderDetailEntity getOrderDetail() { return orderDetail; }
  public void setOrderDetail(OrderDetailEntity orderDetail) { this.orderDetail = orderDetail; }
  public RecetaEntity getReceta() { return receta; }
  public void setReceta(RecetaEntity receta) { this.receta = receta; }
  public DetalleRecetaEntity getDetalleReceta() { return detalleReceta; }
  public void setDetalleReceta(DetalleRecetaEntity detalleReceta) { this.detalleReceta = detalleReceta; }
  public IngredienteEntity getIngrediente() { return ingrediente; }
  public void setIngrediente(IngredienteEntity ingrediente) { this.ingrediente = ingrediente; }
  public BigDecimal getCantidadTeorica() { return cantidadTeorica; }
  public void setCantidadTeorica(BigDecimal cantidadTeorica) { this.cantidadTeorica = cantidadTeorica; }
  public BigDecimal getCantidadConsumida() { return cantidadConsumida; }
  public void setCantidadConsumida(BigDecimal cantidadConsumida) { this.cantidadConsumida = cantidadConsumida; }
  public BigDecimal getCostoUnitario() { return costoUnitario; }
  public void setCostoUnitario(BigDecimal costoUnitario) { this.costoUnitario = costoUnitario; }
  public BigDecimal getCostoTotal() { return costoTotal; }
  public void setCostoTotal(BigDecimal costoTotal) { this.costoTotal = costoTotal; }
  public InventarioMovimientoEntity getMovimientoInventario() { return movimientoInventario; }
  public void setMovimientoInventario(InventarioMovimientoEntity movimientoInventario) { this.movimientoInventario = movimientoInventario; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
