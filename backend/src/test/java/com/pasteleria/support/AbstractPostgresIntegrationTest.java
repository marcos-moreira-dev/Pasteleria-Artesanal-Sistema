package com.pasteleria.support;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base de pruebas de integración con PostgreSQL real.
 *
 * <p>La intención es proteger la línea V1/V2 de Flyway contra diferencias entre SQL,
 * JPA y contratos API. Si Docker no está disponible, Testcontainers marcará estas
 * pruebas como omitidas en lugar de convertir una laptop sin Docker en falso negativo.</p>
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPostgresIntegrationTest {

  @Container
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
      .withDatabaseName("pasteleria_it")
      .withUsername("postgres")
      .withPassword("postgres");

  @DynamicPropertySource
  static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
    ensurePostgresStarted();

    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.flyway.enabled", () -> "true");
    registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    registry.add("spring.jpa.show-sql", () -> "false");
    registry.add("app.jwt.secret", () -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    registry.add("app.report.worker-enabled", () -> "false");
    registry.add("app.storage.root", () -> "target/test-storage");
    registry.add("app.storage.report-path", () -> "target/test-storage/reportes");
  }
  /**
   * Spring Boot resuelve las propiedades dinámicas antes de que algunos callbacks
   * de JUnit/Testcontainers hayan arrancado el contenedor. Al iniciar aquí de
   * forma idempotente evitamos pedir el puerto mapeado antes de tiempo.
   */
  private static synchronized void ensurePostgresStarted() {
    if (!POSTGRES.isRunning()) {
      POSTGRES.start();
    }
  }

}
