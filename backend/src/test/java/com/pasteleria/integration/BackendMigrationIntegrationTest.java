package com.pasteleria.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.pasteleria.support.AbstractPostgresIntegrationTest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

/**
 * Protege la línea Flyway compacta V1/V2 y las vistas repeatable.
 */
class BackendMigrationIntegrationTest extends AbstractPostgresIntegrationTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldApplyFlywayMigrationsAndExposeCriticalStructures() {
    assertThat(tableExists("public", "usuario_sistema")).isTrue();
    assertThat(tableExists("public", "producto")).isTrue();
    assertThat(tableExists("public", "caso_uso_operativo")).isTrue();
    assertThat(tableExists("public", "archivo_recurso")).isTrue();
    assertThat(tableExists("public", "consumo_material_produccion")).isTrue();
    assertThat(tableExists("public", "lote_produccion")).isTrue();
    assertThat(tableExists("public", "entrada_producto_terminado")).isTrue();
    assertThat(tableExists("public", "documento_compra")).isTrue();
    assertThat(tableExists("public", "documento_pagar")).isTrue();
    assertThat(tableExists("public", "tercero")).isTrue();
    assertThat(tableExists("public", "cliente_perfil")).isTrue();
    assertThat(tableExists("public", "proveedor_perfil")).isTrue();
    assertThat(tableExists("public", "cuenta_contable")).isTrue();
    assertThat(tableExists("public", "asiento_contable")).isTrue();
    assertThat(tableExists("public", "documento_fiscal")).isTrue();
    assertThat(tableExists("public", "auditoria_evento")).isTrue();
    assertThat(schemaExists("core")).isTrue();
    assertThat(schemaExists("inteligencia")).isTrue();
    assertThat(schemaExists("auditoria")).isTrue();
    assertThat(tableExists("core", "legacy_objeto_mapeo")).isTrue();
    assertThat(tableExists("contabilidad", "cuenta_contable")).isTrue();
    assertThat(tableExists("contabilidad", "asiento_contable")).isTrue();
    assertThat(tableExists("fiscal", "documento_fiscal")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_stock_actual")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_dashboard_erp")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_cartera_documentos")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_cuentas_pagar_documentos")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_caja_movimientos")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_contabilidad_asientos")).isTrue();
    assertThat(viewExists("inteligencia", "vw_semantic_fiscal_documentos")).isTrue();
  }

  @Test
  void shouldLoadMinimumCanonicalSeeds() {
    Integer users = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.usuario_sistema", Integer.class);
    Integer activeProducts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.producto WHERE activo = true", Integer.class);
    Integer activeUseCases = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.caso_uso_operativo WHERE activo = true", Integer.class);
    Integer activeAccounts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.cuenta_contable WHERE activa = true", Integer.class);

    assertThat(users).isNotNull().isGreaterThanOrEqualTo(3);
    assertThat(activeProducts).isNotNull().isGreaterThan(0);
    assertThat(activeUseCases).isNotNull().isGreaterThan(0);
    assertThat(activeAccounts).isNotNull().isGreaterThan(0);
  }

  @Test
  void shouldPassSqlValidationScripts() throws Exception {
    for (String script : List.of(
        "db/validation/01_smoke_structure.sql",
        "db/validation/02_smoke_seeds.sql",
        "db/validation/03_validate_v1_invariants.sql",
        "db/validation/04_validate_inventory_movements.sql",
        "db/validation/05_validate_cash_register.sql",
        "db/validation/06_validate_order_production_state.sql",
        "db/validation/07_validate_production_materials.sql",
        "db/validation/08_validate_purchase_documents.sql",
        "db/validation/09_validate_third_parties.sql",
        "db/validation/10_validate_receivables_payables.sql",
        "db/validation/11_validate_accounting.sql",
        "db/validation/12_validate_erp_bridges.sql",
        "db/validation/13_validate_fiscal_documents.sql",
        "db/validation/14_validate_intelligence_reporting.sql",
        "db/validation/15_validate_presentation_sit.sql",
        "db/validation/16_validate_audit_support_evidence.sql"
    )) {
      String sql = StreamUtils.copyToString(new ClassPathResource(script).getInputStream(), StandardCharsets.UTF_8);
      jdbcTemplate.execute(stripPsqlMetaCommands(sql));
    }
  }

  private String stripPsqlMetaCommands(String sql) {
    StringBuilder jdbcSql = new StringBuilder();
    for (String line : sql.split("\\R")) {
      if (!line.stripLeading().startsWith("\\")) {
        jdbcSql.append(line).append(System.lineSeparator());
      }
    }
    return jdbcSql.toString();
  }

  private boolean tableExists(String schema, String table) {
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
        "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = ? AND table_name = ?)",
        Boolean.class,
        schema,
        table
    ));
  }

  private boolean schemaExists(String schema) {
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
        "SELECT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = ?)",
        Boolean.class,
        schema
    ));
  }

  private boolean viewExists(String schema, String view) {
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
        "SELECT EXISTS (SELECT 1 FROM information_schema.views WHERE table_schema = ? AND table_name = ?)",
        Boolean.class,
        schema,
        view
    ));
  }
}
