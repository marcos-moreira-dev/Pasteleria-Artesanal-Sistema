package com.pasteleria.terceros.application;

import com.pasteleria.common.text.TextSupport;

import java.time.OffsetDateTime;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Sincroniza clientes/proveedores V1 con el tercero unificado transicional.
 *
 * <p>La UX/UI actual de Clientes y Proveedores se conserva. Esta capa crea el
 * puente hacia el modelo ERP de tercero comun sin obligar a cambiar pantallas ni
 * contratos existentes de golpe.</p>
 */
@Service
public class TerceroSynchronizationService {

  private final JdbcTemplate jdbcTemplate;

  public TerceroSynchronizationService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional
  public Long syncCliente(Long clienteId, String nombreCompleto, String telefono, String correo, String observaciones) {
    if (!tableExists("public.tercero")) {
      return null;
    }
    Long terceroId = findLinkedTercero("cliente", "cliente_id", clienteId);
    OffsetDateTime now = OffsetDateTime.now();
    if (terceroId == null) {
      terceroId = insertTercero(nombreCompleto, nombreCompleto, telefono, correo, null, observaciones, now);
      jdbcTemplate.update("update cliente set tercero_id = ?, updated_at = now() where cliente_id = ?", terceroId, clienteId);
    } else {
      updateTercero(terceroId, nombreCompleto, nombreCompleto, telefono, correo, null, observaciones);
    }
    upsertClientePerfil(terceroId, clienteId);
    upsertLegacyMap("cliente", clienteId, terceroId, "CLIENTE");
    return terceroId;
  }

  @Transactional
  public Long syncProveedor(
      Long proveedorId,
      String codigo,
      String nombre,
      String telefono,
      String correo,
      String direccion,
      String observaciones,
      boolean activo
  ) {
    if (!tableExists("public.tercero")) {
      return null;
    }
    Long terceroId = findLinkedTercero("proveedor", "proveedor_id", proveedorId);
    OffsetDateTime now = OffsetDateTime.now();
    if (terceroId == null) {
      terceroId = insertTercero(nombre, nombre, telefono, correo, direccion, observaciones, now);
      jdbcTemplate.update("update proveedor set tercero_id = ?, updated_at = now() where proveedor_id = ?", terceroId, proveedorId);
    } else {
      updateTercero(terceroId, nombre, nombre, telefono, correo, direccion, observaciones);
    }
    jdbcTemplate.update("update tercero set activo = ?, updated_at = now() where tercero_id = ?", activo, terceroId);
    upsertProveedorPerfil(terceroId, proveedorId, codigo);
    upsertLegacyMap("proveedor", proveedorId, terceroId, "PROVEEDOR");
    return terceroId;
  }


  private boolean tableExists(String regclass) {
    Boolean exists = jdbcTemplate.queryForObject("select to_regclass(?) is not null", Boolean.class, regclass);
    return Boolean.TRUE.equals(exists);
  }

  private Long findLinkedTercero(String tableName, String idColumn, Long legacyId) {
    return jdbcTemplate.query("select tercero_id from " + tableName + " where " + idColumn + " = ?", rs -> {
      if (!rs.next()) {
        return null;
      }
      long value = rs.getLong("tercero_id");
      return rs.wasNull() ? null : value;
    }, legacyId);
  }

  private Long insertTercero(
      String nombreLegal,
      String nombreComercial,
      String telefono,
      String correo,
      String direccion,
      String observaciones,
      OffsetDateTime now
  ) {
    return jdbcTemplate.queryForObject("""
        insert into tercero (
          tipo_identificacion,
          nombre_legal,
          nombre_comercial,
          telefono,
          correo,
          direccion_principal,
          observaciones,
          activo,
          created_at,
          updated_at,
          version
        ) values ('NO_ESPECIFICADA', ?, ?, ?, ?, ?, ?, true, ?, ?, 0)
        returning tercero_id
        """, Long.class,
        normalizeRequired(nombreLegal),
        TextSupport.trimToNull(nombreComercial),
        TextSupport.trimToNull(telefono),
        normalizeEmail(correo),
        TextSupport.trimToNull(direccion),
        TextSupport.trimToNull(observaciones),
        now,
        now
    );
  }

  private void updateTercero(
      Long terceroId,
      String nombreLegal,
      String nombreComercial,
      String telefono,
      String correo,
      String direccion,
      String observaciones
  ) {
    jdbcTemplate.update("""
        update tercero
        set nombre_legal = ?,
            nombre_comercial = ?,
            telefono = ?,
            correo = ?,
            direccion_principal = ?,
            observaciones = ?,
            updated_at = now()
        where tercero_id = ?
        """,
        normalizeRequired(nombreLegal),
        TextSupport.trimToNull(nombreComercial),
        TextSupport.trimToNull(telefono),
        normalizeEmail(correo),
        TextSupport.trimToNull(direccion),
        TextSupport.trimToNull(observaciones),
        terceroId
    );
  }

  private void upsertClientePerfil(Long terceroId, Long clienteId) {
    jdbcTemplate.update("""
        insert into cliente_perfil (tercero_id, cliente_id, estado, fecha_alta)
        values (?, ?, 'ACTIVO', now())
        on conflict (cliente_id) do update set tercero_id = excluded.tercero_id, estado = 'ACTIVO'
        """, terceroId, clienteId);
  }

  private void upsertProveedorPerfil(Long terceroId, Long proveedorId, String codigo) {
    jdbcTemplate.update("""
        insert into proveedor_perfil (tercero_id, proveedor_id, codigo_proveedor, estado, fecha_alta)
        values (?, ?, ?, 'ACTIVO', now())
        on conflict (proveedor_id) do update
        set tercero_id = excluded.tercero_id,
            codigo_proveedor = excluded.codigo_proveedor,
            estado = 'ACTIVO'
        """, terceroId, proveedorId, normalizeCode(codigo));
  }

  private void upsertLegacyMap(String origenTabla, Long origenId, Long terceroId, String contexto) {
    if (!tableExists("core.legacy_objeto_mapeo")) {
      return;
    }
    jdbcTemplate.update("""
        insert into core.legacy_objeto_mapeo (origen_tabla, origen_id, destino_schema, destino_tabla, destino_id, contexto)
        values (?, ?, 'terceros', 'tercero', ?, ?)
        on conflict (origen_tabla, origen_id, destino_schema, destino_tabla)
        do update set destino_id = excluded.destino_id, contexto = excluded.contexto
        """, origenTabla, origenId.toString(), terceroId.toString(), contexto);
  }

  private String normalizeRequired(String value) {
    String normalized = TextSupport.trimToNull(value);
    if (normalized == null) {
      return "Sin nombre";
    }
    return normalized;
  }

  private String normalizeEmail(String email) {
    String normalized = TextSupport.trimToNull(email);
    return normalized == null ? null : normalized.toLowerCase();
  }

  private String normalizeCode(String code) {
    String normalized = TextSupport.trimToNull(code);
    return normalized == null ? null : normalized.toUpperCase();
  }
}
