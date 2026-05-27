package com.pasteleria.soporte.application;

import com.pasteleria.common.config.StorageProperties;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Construye evidencia de soporte sin modificar datos operativos.
 */
@Service
public class SupportEvidenceService {

  private final JdbcTemplate jdbcTemplate;
  private final StorageProperties storageProperties;

  public SupportEvidenceService(JdbcTemplate jdbcTemplate, StorageProperties storageProperties) {
    this.jdbcTemplate = jdbcTemplate;
    this.storageProperties = storageProperties;
  }

  @Transactional(readOnly = true)
  public SupportEvidenceSnapshot snapshot() {
    return new SupportEvidenceSnapshot(
        OffsetDateTime.now(),
        storageProperties.rootPath().toString(),
        Files.exists(storageProperties.rootPath()),
        counters(),
        migrationMarkers(),
        validationScripts(),
        criticalEndpoints()
    );
  }

  public List<SupportChecklistItem> checklist() {
    SupportEvidenceSnapshot snapshot = snapshot();
    return List.of(
        new SupportChecklistItem(
            "BACKEND_TESTS",
            "Ejecutar scripts\\test-backend.bat",
            "REQUERIDO",
            "Debe compilar backend, ejecutar tests unitarios, smoke API y validaciones SQL."
        ),
        new SupportChecklistItem(
            "FRONTEND_ADMIN_BUILD",
            "Ejecutar scripts\\test-admin.bat si se toca Angular",
            "CONDICIONAL",
            "Requerido cuando la tanda modifica frontend-admin-angular."
        ),
        new SupportChecklistItem(
            "STOREFRONT_BUILD",
            "Ejecutar scripts\\test-storefront.bat si se toca storefront",
            "CONDICIONAL",
            "Requerido cuando la tanda modifica frontend-publico-astro."
        ),
        new SupportChecklistItem(
            "STORAGE_LOCAL",
            "Verificar storage local",
            snapshot.storageRootExists() ? "OK" : "REVISAR",
            "Ruta actual: " + snapshot.storageRoot()
        ),
        new SupportChecklistItem(
            "ROADMAP_HONESTO",
            "Actualizar roadmap y documentacion de tanda",
            "REQUERIDO",
            "Cada entrega debe declarar alcance, fuera de alcance y validacion recomendada."
        ),
        new SupportChecklistItem(
            "FISCALIDAD_PRUDENTE",
            "No declarar SRI productivo sin evidencia real",
            "REQUERIDO",
            "Los documentos fiscales actuales son internos/preparados, no comprobantes autorizados."
        )
    );
  }

  private List<EvidenceCounter> counters() {
    return List.of(
        counter("usuarios", "SELECT COUNT(*) FROM usuario_sistema"),
        counter("productos_activos", "SELECT COUNT(*) FROM producto WHERE activo = true"),
        counter("terceros", "SELECT COUNT(*) FROM tercero"),
        counter("documentos_cobrar", "SELECT COUNT(*) FROM documento_cobrar"),
        counter("documentos_pagar", "SELECT COUNT(*) FROM documento_pagar"),
        counter("asientos_contables", "SELECT COUNT(*) FROM asiento_contable"),
        counter("documentos_fiscales", "SELECT COUNT(*) FROM documento_fiscal"),
        counter("eventos_auditoria", "SELECT COUNT(*) FROM auditoria_evento")
    );
  }

  private EvidenceCounter counter(String name, String sql) {
    Long value = jdbcTemplate.queryForObject(sql, Long.class);
    return new EvidenceCounter(name, value == null ? 0L : value);
  }

  private List<String> migrationMarkers() {
    return jdbcTemplate.query("""
        SELECT codigo
        FROM core.erp_migration_marker
        ORDER BY codigo
        """, (rs, rowNum) -> rs.getString("codigo"));
  }

  private List<String> validationScripts() {
    return List.of(
        "01_smoke_structure.sql",
        "02_smoke_seeds.sql",
        "03_validate_v1_invariants.sql",
        "04_validate_inventory_movements.sql",
        "05_validate_cash_register.sql",
        "06_validate_order_production_state.sql",
        "07_validate_production_materials.sql",
        "08_validate_purchase_documents.sql",
        "09_validate_third_parties.sql",
        "10_validate_receivables_payables.sql",
        "11_validate_accounting.sql",
        "12_validate_erp_bridges.sql",
        "13_validate_fiscal_documents.sql",
        "14_validate_intelligence_reporting.sql",
        "15_validate_presentation_sit.sql",
        "16_validate_audit_support_evidence.sql"
    );
  }

  private List<String> criticalEndpoints() {
    return List.of(
        "GET /api/v1/public/health",
        "POST /api/v1/auth/login",
        "GET /api/v1/contratos",
        "GET /api/v1/auditoria/resumen",
        "GET /api/v1/auditoria/eventos",
        "GET /api/v1/soporte/evidencia",
        "GET /api/v1/soporte/checklist",
        "GET /api/v1/inteligencia/dashboard"
    );
  }
}
