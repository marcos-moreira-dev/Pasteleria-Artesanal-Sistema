package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consulta vistas semanticas de solo lectura para tableros/reportes ERP.
 *
 * <p>Esta clase no calcula reglas de negocio ni modifica saldos: solamente lee
 * vistas SQL preparadas por la capa de base de datos.</p>
 */
@Service
@Transactional(readOnly = true)
public class InteligenciaQueryService {

  private final JdbcTemplate jdbcTemplate;

  public InteligenciaQueryService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public DashboardErpSummary dashboard() {
    return jdbcTemplate.queryForObject(
        """
        SELECT *
        FROM inteligencia.vw_semantic_dashboard_erp
        """,
        (rs, rowNum) -> new DashboardErpSummary(
            rs.getString("fecha_corte"),
            rs.getLong("pedidos_activos"),
            rs.getLong("producciones_activas"),
            rs.getLong("documentos_cobrar_abiertos"),
            money(rs, "saldo_cartera"),
            rs.getLong("documentos_pagar_abiertos"),
            money(rs, "saldo_cuentas_pagar"),
            money(rs, "saldo_caja"),
            rs.getLong("asientos_registrados"),
            rs.getLong("documentos_fiscales_borrador"),
            rs.getLong("items_stock_bajo")
        )
    );
  }

  public List<CarteraSemanticRow> cartera(int limit) {
    return jdbcTemplate.query(
        """
        SELECT *
        FROM inteligencia.vw_semantic_cartera_documentos
        ORDER BY saldo DESC, fecha_emision DESC, documento_cobrar_id DESC
        LIMIT ?
        """,
        ps -> ps.setInt(1, safeLimit(limit)),
        this::mapCartera
    );
  }

  public List<CuentasPagarSemanticRow> cuentasPagar(int limit) {
    return jdbcTemplate.query(
        """
        SELECT *
        FROM inteligencia.vw_semantic_cuentas_pagar_documentos
        ORDER BY saldo DESC, fecha_emision DESC, documento_pagar_id DESC
        LIMIT ?
        """,
        ps -> ps.setInt(1, safeLimit(limit)),
        this::mapCuentasPagar
    );
  }

  public List<CajaMovimientoSemanticRow> caja(int limit) {
    return jdbcTemplate.query(
        """
        SELECT *
        FROM inteligencia.vw_semantic_caja_movimientos
        ORDER BY fecha_movimiento DESC, movimiento_caja_id DESC
        LIMIT ?
        """,
        ps -> ps.setInt(1, safeLimit(limit)),
        this::mapCaja
    );
  }

  public List<ContabilidadSemanticRow> contabilidad(int limit) {
    return jdbcTemplate.query(
        """
        SELECT *
        FROM inteligencia.vw_semantic_contabilidad_asientos
        ORDER BY fecha_asiento DESC, asiento_contable_id DESC
        LIMIT ?
        """,
        ps -> ps.setInt(1, safeLimit(limit)),
        this::mapContabilidad
    );
  }

  public List<FiscalSemanticRow> fiscal(int limit) {
    return jdbcTemplate.query(
        """
        SELECT *
        FROM inteligencia.vw_semantic_fiscal_documentos
        ORDER BY fecha_emision DESC, documento_fiscal_id DESC
        LIMIT ?
        """,
        ps -> ps.setInt(1, safeLimit(limit)),
        this::mapFiscal
    );
  }

  public List<StockBajoSemanticRow> stockBajo(int limit) {
    return jdbcTemplate.query(
        """
        SELECT *
        FROM inteligencia.vw_semantic_stock_actual
        ORDER BY tipo_item, nombre
        LIMIT ?
        """,
        ps -> ps.setInt(1, safeLimit(limit)),
        this::mapStockBajo
    );
  }

  private CarteraSemanticRow mapCartera(ResultSet rs, int rowNum) throws SQLException {
    return new CarteraSemanticRow(
        rs.getLong("documento_cobrar_id"),
        rs.getString("codigo"),
        rs.getString("estado"),
        rs.getString("fecha_emision"),
        rs.getString("fecha_vencimiento"),
        money(rs, "total"),
        money(rs, "saldo"),
        money(rs, "monto_cobrado"),
        rs.getInt("dias_vencido"),
        rs.getLong("cliente_id"),
        rs.getString("cliente_nombre"),
        nullableLong(rs, "pedido_id"),
        rs.getString("pedido_codigo")
    );
  }

  private CuentasPagarSemanticRow mapCuentasPagar(ResultSet rs, int rowNum) throws SQLException {
    return new CuentasPagarSemanticRow(
        rs.getLong("documento_pagar_id"),
        rs.getString("codigo"),
        rs.getString("estado"),
        rs.getString("fecha_emision"),
        rs.getString("fecha_vencimiento"),
        money(rs, "total"),
        money(rs, "saldo"),
        money(rs, "monto_pagado"),
        rs.getInt("dias_vencido"),
        rs.getLong("proveedor_id"),
        rs.getString("proveedor_nombre"),
        rs.getLong("documento_compra_id"),
        rs.getString("numero_documento_compra")
    );
  }

  private CajaMovimientoSemanticRow mapCaja(ResultSet rs, int rowNum) throws SQLException {
    return new CajaMovimientoSemanticRow(
        rs.getLong("movimiento_caja_id"),
        rs.getString("fecha_movimiento"),
        rs.getString("tipo_movimiento"),
        rs.getString("naturaleza"),
        money(rs, "monto"),
        rs.getString("moneda"),
        rs.getString("estado"),
        rs.getString("referencia_tipo"),
        rs.getString("referencia_id"),
        rs.getString("descripcion"),
        rs.getLong("turno_caja_id"),
        rs.getString("estado_turno"),
        rs.getLong("caja_id"),
        rs.getString("caja_codigo"),
        rs.getString("caja_nombre")
    );
  }

  private ContabilidadSemanticRow mapContabilidad(ResultSet rs, int rowNum) throws SQLException {
    return new ContabilidadSemanticRow(
        rs.getLong("asiento_contable_id"),
        rs.getString("codigo"),
        rs.getString("fecha_asiento"),
        rs.getString("estado"),
        rs.getString("descripcion"),
        rs.getString("origen_tipo"),
        rs.getString("origen_id"),
        money(rs, "total_debe"),
        money(rs, "total_haber"),
        rs.getString("diario_codigo"),
        rs.getString("diario_nombre"),
        rs.getLong("lineas")
    );
  }

  private FiscalSemanticRow mapFiscal(ResultSet rs, int rowNum) throws SQLException {
    return new FiscalSemanticRow(
        rs.getLong("documento_fiscal_id"),
        rs.getString("codigo"),
        rs.getString("tipo_comprobante"),
        rs.getString("estado"),
        rs.getString("origen_tipo"),
        rs.getString("tercero_tipo"),
        rs.getLong("tercero_id"),
        rs.getString("tercero_nombre"),
        rs.getString("fecha_emision"),
        rs.getString("numero_comprobante"),
        money(rs, "subtotal"),
        money(rs, "impuesto"),
        money(rs, "total"),
        rs.getString("ambiente"),
        rs.getString("clave_acceso"),
        rs.getString("numero_autorizacion"),
        rs.getString("fecha_autorizacion")
    );
  }

  private StockBajoSemanticRow mapStockBajo(ResultSet rs, int rowNum) throws SQLException {
    return new StockBajoSemanticRow(
        rs.getString("tipo_item"),
        rs.getLong("item_id"),
        rs.getString("codigo"),
        rs.getString("nombre"),
        money(rs, "stock_actual"),
        money(rs, "stock_minimo"),
        rs.getString("unidad")
    );
  }

  private BigDecimal money(ResultSet rs, String column) throws SQLException {
    BigDecimal value = rs.getBigDecimal(column);
    return value == null ? BigDecimal.ZERO : value;
  }

  private Long nullableLong(ResultSet rs, String column) throws SQLException {
    long value = rs.getLong(column);
    return rs.wasNull() ? null : value;
  }

  private int safeLimit(int requested) {
    return Math.max(1, Math.min(requested, 100));
  }
}
