package com.pasteleria.auditoria.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consultas de auditoria para soporte. No registra eventos ni modifica datos.
 */
@Service
public class AuditQueryService {

  private final JdbcTemplate jdbcTemplate;

  public AuditQueryService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional(readOnly = true)
  public List<AuditEventSummary> recent(String module, int limit) {
    int safeLimit = Math.max(1, Math.min(limit, 100));
    if (module == null || module.isBlank()) {
      return jdbcTemplate.query("""
          SELECT ae.auditoria_evento_id,
                 ae.codigo_evento,
                 ae.modulo,
                 ae.entidad,
                 ae.entidad_id,
                 ae.accion,
                 u.nombre_usuario AS actor_username,
                 ae.actor_rol,
                 ae.fecha_evento,
                 ae.motivo,
                 ae.request_id,
                 ae.ip_origen
          FROM auditoria_evento ae
          LEFT JOIN usuario_sistema u ON u.usuario_id = ae.actor_usuario_id
          ORDER BY ae.fecha_evento DESC, ae.auditoria_evento_id DESC
          LIMIT ?
          """, this::mapAuditEvent, safeLimit);
    }

    return jdbcTemplate.query("""
        SELECT ae.auditoria_evento_id,
               ae.codigo_evento,
               ae.modulo,
               ae.entidad,
               ae.entidad_id,
               ae.accion,
               u.nombre_usuario AS actor_username,
               ae.actor_rol,
               ae.fecha_evento,
               ae.motivo,
               ae.request_id,
               ae.ip_origen
        FROM auditoria_evento ae
        LEFT JOIN usuario_sistema u ON u.usuario_id = ae.actor_usuario_id
        WHERE UPPER(ae.modulo) = UPPER(?)
        ORDER BY ae.fecha_evento DESC, ae.auditoria_evento_id DESC
        LIMIT ?
        """, this::mapAuditEvent, module.trim(), safeLimit);
  }

  @Transactional(readOnly = true)
  public AuditDashboardSummary dashboard() {
    Long totalEvents = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auditoria_evento", Long.class);
    Long eventsLast24Hours = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM auditoria_evento WHERE fecha_evento >= NOW() - INTERVAL '24 hours'",
        Long.class
    );
    Long distinctModules = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT modulo) FROM auditoria_evento", Long.class);
    List<AuditModuleCount> topModules = jdbcTemplate.query("""
        SELECT modulo, COUNT(*) AS total
        FROM auditoria_evento
        GROUP BY modulo
        ORDER BY total DESC, modulo ASC
        LIMIT 8
        """, (rs, rowNum) -> new AuditModuleCount(rs.getString("modulo"), rs.getLong("total")));

    return new AuditDashboardSummary(
        zeroIfNull(totalEvents),
        zeroIfNull(eventsLast24Hours),
        zeroIfNull(distinctModules),
        topModules
    );
  }

  private AuditEventSummary mapAuditEvent(ResultSet rs, int rowNum) throws SQLException {
    return new AuditEventSummary(
        rs.getLong("auditoria_evento_id"),
        rs.getString("codigo_evento"),
        rs.getString("modulo"),
        rs.getString("entidad"),
        rs.getString("entidad_id"),
        rs.getString("accion"),
        rs.getString("actor_username"),
        rs.getString("actor_rol"),
        rs.getObject("fecha_evento", OffsetDateTime.class),
        rs.getString("motivo"),
        rs.getString("request_id"),
        rs.getString("ip_origen")
    );
  }

  private long zeroIfNull(Long value) {
    return value == null ? 0L : value;
  }
}
