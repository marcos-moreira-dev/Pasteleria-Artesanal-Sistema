package com.pasteleria.terceros.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consulta transicional de terceros unificados.
 *
 * <p>Durante la migracion V1 -> ERP, clientes y proveedores siguen teniendo sus
 * pantallas propias. Este servicio permite auditar la vista unificada sin obligar
 * todavia a Angular a cambiar sus flujos de clientes/proveedores.</p>
 */
@Service
public class TerceroQueryService {

  private final JdbcTemplate jdbcTemplate;

  public TerceroQueryService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional(readOnly = true)
  public List<TerceroSummary> list(String perfil, String query) {
    String normalizedPerfil = perfil == null || perfil.isBlank() ? null : perfil.trim().toUpperCase();
    String normalizedQuery = query == null || query.isBlank() ? null : "%" + query.trim().toLowerCase() + "%";

    List<Object> args = new ArrayList<>();
    StringBuilder sql = new StringBuilder("""
        select
          t.tercero_id,
          t.nombre_legal,
          t.nombre_comercial,
          t.telefono,
          t.correo,
          t.direccion_principal,
          t.activo,
          t.created_at,
          cp.cliente_id,
          pp.proveedor_id,
          (cp.cliente_perfil_id is not null) as es_cliente,
          (pp.proveedor_perfil_id is not null) as es_proveedor,
          (ep.empleado_perfil_id is not null) as es_empleado
        from tercero t
        left join cliente_perfil cp on cp.tercero_id = t.tercero_id
        left join proveedor_perfil pp on pp.tercero_id = t.tercero_id
        left join empleado_perfil ep on ep.tercero_id = t.tercero_id
        where 1 = 1
        """);

    if (normalizedPerfil != null) {
      if ("CLIENTE".equals(normalizedPerfil)) {
        sql.append(" and cp.cliente_perfil_id is not null");
      } else if ("PROVEEDOR".equals(normalizedPerfil)) {
        sql.append(" and pp.proveedor_perfil_id is not null");
      } else if ("EMPLEADO".equals(normalizedPerfil)) {
        sql.append(" and ep.empleado_perfil_id is not null");
      }
    }

    if (normalizedQuery != null) {
      sql.append("""
           and (
             lower(t.nombre_legal) like ?
             or lower(coalesce(t.nombre_comercial, '')) like ?
             or lower(coalesce(t.correo, '')) like ?
             or lower(coalesce(t.telefono, '')) like ?
           )
          """);
      args.add(normalizedQuery);
      args.add(normalizedQuery);
      args.add(normalizedQuery);
      args.add(normalizedQuery);
    }

    sql.append(" order by t.created_at desc, t.tercero_id desc limit 200");
    return jdbcTemplate.query(sql.toString(), this::mapRow, args.toArray());
  }

  private TerceroSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
    List<TerceroPerfil> perfiles = new ArrayList<>();
    if (rs.getBoolean("es_cliente")) {
      perfiles.add(TerceroPerfil.CLIENTE);
    }
    if (rs.getBoolean("es_proveedor")) {
      perfiles.add(TerceroPerfil.PROVEEDOR);
    }
    if (rs.getBoolean("es_empleado")) {
      perfiles.add(TerceroPerfil.EMPLEADO);
    }
    Long clienteId = rs.getObject("cliente_id", Long.class);
    Long proveedorId = rs.getObject("proveedor_id", Long.class);
    return new TerceroSummary(
        rs.getLong("tercero_id"),
        rs.getString("nombre_legal"),
        rs.getString("nombre_comercial"),
        rs.getString("telefono"),
        rs.getString("correo"),
        rs.getString("direccion_principal"),
        perfiles,
        clienteId,
        proveedorId,
        rs.getBoolean("activo"),
        rs.getObject("created_at", java.time.OffsetDateTime.class)
    );
  }
}
