package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

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
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un Movimiento de Inventario.
 * 
 * <h2>DESCRIPCIÓN</h2>
 * Esta entidad registra de forma inmutable todos los cambios en el inventario.
 * Cada movimiento representa una entrada, salida o ajuste de stock.
 * Es la base del sistema de trazabilidad y auditoría de inventario.
 * 
 * <h2>DISEÑO POLIMÓRFICO</h2>
 * En lugar de usar herencia JPA (que genera complejidad y JOINs costosos),
 * se implementa un sistema polimórfico manual usando:
 * <ul>
 *   <li><b>itemTipo</b>: Tipo de item afectado ("INGREDIENTE" o "INSUMO")</li>
 *   <li><b>itemId</b>: ID del item en su tabla respectiva</li>
 * </ul>
 * 
 * Esto permite:
 * <ul>
 *   <li>Una sola tabla para todos los movimientos</li>
 *   <li>Consultas simples y rápidas</li>
 *   <li>Flexibilidad para agregar nuevos tipos de items</li>
 *   <li>Auditoría unificada</li>
 * </ul>
 * 
 * <h2>TIPOS DE MOVIMIENTO</h2>
 * {@link com.pasteleria.abastecimiento.domain.model.TipoMovimiento}
 * <ul>
 *   <li><b>ENTRADA_COMPRA</b>: Recepción de mercadería de proveedores (cantidad > 0)</li>
 *   <li><b>ENTRADA_AJUSTE</b>: Corrección positiva de inventario (cantidad > 0)</li>
 *   <li><b>SALIDA_PRODUCCION</b>: Consumo para órdenes de producción (cantidad positiva; resta stock)</li>
 *   <li><b>SALIDA_MERMA</b>: Pérdida por vencimiento, daño, etc. (cantidad positiva; resta stock)</li>
 *   <li><b>SALIDA_AJUSTE</b>: Corrección negativa de inventario (cantidad positiva; resta stock)</li>
 * </ul>
 * 
 * <h2>CAMPOS PRINCIPALES</h2>
 * <ul>
 *   <li><b>cantidad</b>: Cantidad física positiva del movimiento; el tipo define si suma o resta stock</li>
 *   <li><b>saldoAnterior/saldoPosterior</b>: Stock antes y después del movimiento (para trazabilidad)</li>
 *   <li><b>referenciaTipo/referenciaId</b>: Origen del movimiento (OC, Producción, Ajuste manual)</li>
 *   <li><b>motivoSalida</b>: Justificación obligatoria para salidas</li>
 *   <li><b>fechaMovimiento</b>: Timestamp exacto del movimiento</li>
 *   <li><b>registradoPor</b>: Usuario que realizó el movimiento</li>
 * </ul>
 * 
 * <h2>INMUTABILIDAD</h2>
 * Los movimientos son inmutables una vez creados. No hay método update.
 * Si hay un error, se debe crear un movimiento compensatorio (ajuste).
 * 
 * <h2>EJEMPLOS DE MOVIMIENTOS</h2>
 * 
 * <h3>Entrada por Compra</h3>
 * <pre>
 * itemTipo: "INGREDIENTE"
 * itemId: 1
 * tipoMovimiento: "ENTRADA_COMPRA"
 * cantidad: 1000.000
 * saldoPosterior: 5000.000
 * referenciaTipo: "ORDEN_COMPRA"
 * referenciaId: "OC-2026-001"
 * </pre>
 * 
 * <h3>Salida por Producción</h3>
 * <pre>
 * itemTipo: "INGREDIENTE"
 * itemId: 1
 * tipoMovimiento: "SALIDA_PRODUCCION"
 * cantidad: 500.000
 * saldoPosterior: 4500.000
 * referenciaTipo: "PRODUCCION"
 * referenciaId: "PROD-001"
 * motivoSalida: "Torta de chocolate mediana"
 * </pre>
 * 
 * <h3>Ajuste por Merma</h3>
 * <pre>
 * itemTipo: "INGREDIENTE"
 * itemId: 6
 * tipoMovimiento: "SALIDA_MERMA"
 * cantidad: 100.000
 * saldoPosterior: 900.000
 * motivoSalida: "Vencimiento del lote VEN-2026-01"
 * observaciones: "Producto con olor rancio"
 * </pre>
 * 
 * <h2>ÍNDICES RECOMENDADOS</h2>
 * <pre>
 * CREATE INDEX idx_movimiento_item ON inventario_movimiento(item_tipo, item_id);
 * CREATE INDEX idx_movimiento_fecha ON inventario_movimiento(fecha_movimiento);
 * CREATE INDEX idx_movimiento_referencia ON inventario_movimiento(referencia_tipo, referencia_id);
 * </pre>
 * 
 * @see TipoMovimiento
 * @see IngredienteEntity
 * @see InsumoEntity
 * @author Pastelería Development Team
 */
@Entity
@Table(name = "inventario_movimiento")
public class InventarioMovimientoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "inventario_movimiento_id")
  private Long id;

  @Column(name = "item_tipo", nullable = false, length = 20)
  private String itemTipo;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @Column(name = "tipo_movimiento", nullable = false, length = 30)
  private String tipoMovimiento;

  @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
  private BigDecimal cantidad;

  @Column(name = "saldo_anterior", precision = 10, scale = 3)
  private BigDecimal saldoAnterior;

  @Column(name = "saldo_posterior", nullable = false, precision = 10, scale = 3)
  private BigDecimal saldoPosterior;

  @Column(name = "referencia_tipo", length = 40)
  private String referenciaTipo;

  @Column(name = "referencia_id", length = 80)
  private String referenciaId;

  @Column(name = "motivo_salida", length = 100)
  private String motivoSalida;

  @Column(name = "observaciones")
  private String observaciones;

  @Column(name = "fecha_movimiento", nullable = false)
  private OffsetDateTime fechaMovimiento;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "registrado_por_user_id")
  private UserEntity registradoPor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getItemTipo() {
    return itemTipo;
  }

  public void setItemTipo(String itemTipo) {
    this.itemTipo = itemTipo;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getTipoMovimiento() {
    return tipoMovimiento;
  }

  public void setTipoMovimiento(String tipoMovimiento) {
    this.tipoMovimiento = tipoMovimiento;
  }

  public BigDecimal getCantidad() {
    return cantidad;
  }

  public void setCantidad(BigDecimal cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getSaldoAnterior() {
    return saldoAnterior;
  }

  public void setSaldoAnterior(BigDecimal saldoAnterior) {
    this.saldoAnterior = saldoAnterior;
  }

  public BigDecimal getSaldoPosterior() {
    return saldoPosterior;
  }

  public void setSaldoPosterior(BigDecimal saldoPosterior) {
    this.saldoPosterior = saldoPosterior;
  }

  public String getReferenciaTipo() {
    return referenciaTipo;
  }

  public void setReferenciaTipo(String referenciaTipo) {
    this.referenciaTipo = referenciaTipo;
  }

  public String getReferenciaId() {
    return referenciaId;
  }

  public void setReferenciaId(String referenciaId) {
    this.referenciaId = referenciaId;
  }

  public String getMotivoSalida() {
    return motivoSalida;
  }

  public void setMotivoSalida(String motivoSalida) {
    this.motivoSalida = motivoSalida;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public OffsetDateTime getFechaMovimiento() {
    return fechaMovimiento;
  }

  public void setFechaMovimiento(OffsetDateTime fechaMovimiento) {
    this.fechaMovimiento = fechaMovimiento;
  }

  public UserEntity getRegistradoPor() {
    return registradoPor;
  }

  public void setRegistradoPor(UserEntity registradoPor) {
    this.registradoPor = registradoPor;
  }
}
