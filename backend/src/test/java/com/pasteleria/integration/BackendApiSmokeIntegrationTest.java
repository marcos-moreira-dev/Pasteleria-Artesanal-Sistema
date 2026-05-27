package com.pasteleria.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.support.AbstractPostgresIntegrationTest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Smoke API real sobre Spring Boot + PostgreSQL + Flyway.
 *
 * <p>Usa {@link java.net.http.HttpClient} en lugar de TestRestTemplate para mantener
 * compatibilidad con Spring Boot 4 sin depender de paquetes de test web removidos o
 * reubicados.</p>
 */
class BackendApiSmokeIntegrationTest extends AbstractPostgresIntegrationTest {

  @LocalServerPort
  private int port;

  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void shouldExposePublicHealthWithRequestId() throws Exception {
    HttpResponse<String> response = send(
        request("/api/v1/public/health")
            .GET()
            .header("X-Request-Id", "it-health-001")
    );

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.headers().firstValue("X-Request-Id")).contains("it-health-001");
    JsonNode body = objectMapper.readTree(response.body());
    assertThat(body.path("ok").asBoolean()).isTrue();
    assertThat(body.path("data").path("status").asText()).isEqualTo("UP");
  }

  @Test
  void shouldReturnStandardUnauthorizedForProtectedEndpoint() throws Exception {
    HttpResponse<String> response = send(request("/api/v1/casos-uso/hub").GET());

    assertThat(response.statusCode()).isEqualTo(401);
    JsonNode body = objectMapper.readTree(response.body());
    assertThat(body.path("ok").asBoolean()).isFalse();
    assertThat(body.path("success").asBoolean()).isFalse();
    assertThat(body.path("error").path("code").asText()).isNotBlank();
    assertThat(body.path("meta").path("requestId").asText()).isNotBlank();
  }

  @Test
  void shouldLoginAndReadProtectedContractsAndUseCases() throws Exception {
    String token = loginAsAdmin();

    HttpResponse<String> currentUser = send(
        authenticatedRequest("/api/v1/auth/me", token)
            .GET()
            .header("X-Request-Id", "it-contracts-001")
    );

    assertThat(currentUser.statusCode()).isEqualTo(200);
    JsonNode currentUserBody = objectMapper.readTree(currentUser.body());
    assertThat(currentUserBody.path("data").path("permisos").isArray()).isTrue();
    assertThat(currentUserBody.path("data").path("sucursalesOperables").isArray()).isTrue();

    HttpResponse<String> contracts = send(authenticatedRequest("/api/v1/contratos", token).GET());

    assertThat(contracts.statusCode()).isEqualTo(200);
    JsonNode contractsBody = objectMapper.readTree(contracts.body());
    assertThat(contractsBody.path("ok").asBoolean()).isTrue();
    assertThat(contractsBody.path("data").path("endpoints").isArray()).isTrue();
    assertThat(contractsBody.path("data").path("permissions").isArray()).isTrue();

    HttpResponse<String> useCases = send(authenticatedRequest("/api/v1/casos-uso/hub", token).GET());

    assertThat(useCases.statusCode()).isEqualTo(200);
    JsonNode useCasesBody = objectMapper.readTree(useCases.body());
    assertThat(useCasesBody.path("ok").asBoolean()).isTrue();
    assertThat(useCasesBody.path("data").isMissingNode()).isFalse();

    HttpResponse<String> terceros = send(authenticatedRequest("/api/v1/terceros", token).GET());

    assertThat(terceros.statusCode()).isEqualTo(200);
    JsonNode tercerosBody = objectMapper.readTree(terceros.body());
    assertThat(tercerosBody.path("ok").asBoolean()).isTrue();
    assertThat(tercerosBody.path("data").isArray()).isTrue();

    HttpResponse<String> cuentas = send(authenticatedRequest("/api/v1/contabilidad/cuentas", token).GET());

    assertThat(cuentas.statusCode()).isEqualTo(200);
    JsonNode cuentasBody = objectMapper.readTree(cuentas.body());
    assertThat(cuentasBody.path("ok").asBoolean()).isTrue();
    assertThat(cuentasBody.path("data").isArray()).isTrue();
    assertThat(cuentasBody.path("data").size()).isGreaterThan(0);

    HttpResponse<String> dashboard = send(authenticatedRequest("/api/v1/inteligencia/dashboard", token).GET());

    assertThat(dashboard.statusCode()).isEqualTo(200);
    JsonNode dashboardBody = objectMapper.readTree(dashboard.body());
    assertThat(dashboardBody.path("ok").asBoolean()).isTrue();
    assertThat(dashboardBody.path("data").path("saldoCartera").isNumber()).isTrue();

    HttpResponse<String> auditoria = send(authenticatedRequest("/api/v1/auditoria/resumen", token).GET());

    assertThat(auditoria.statusCode()).isEqualTo(200);
    JsonNode auditoriaBody = objectMapper.readTree(auditoria.body());
    assertThat(auditoriaBody.path("ok").asBoolean()).isTrue();
    assertThat(auditoriaBody.path("data").path("totalEvents").isNumber()).isTrue();

    HttpResponse<String> soporte = send(authenticatedRequest("/api/v1/soporte/evidencia", token).GET());

    assertThat(soporte.statusCode()).isEqualTo(200);
    JsonNode soporteBody = objectMapper.readTree(soporte.body());
    assertThat(soporteBody.path("ok").asBoolean()).isTrue();
    assertThat(soporteBody.path("data").path("operationalCounters").isArray()).isTrue();
  }

  private String loginAsAdmin() throws Exception {
    String payload = objectMapper.writeValueAsString(Map.of("username", "admin", "password", "admin12345"));

    HttpResponse<String> response = send(
        request("/api/v1/auth/login")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .header("Content-Type", "application/json")
            .header("X-Request-Id", "it-login-001")
    );

    assertThat(response.statusCode()).isEqualTo(200);
    JsonNode body = objectMapper.readTree(response.body());
    assertThat(body.path("ok").asBoolean()).isTrue();
    assertThat(body.path("data").path("permisos").isArray()).isTrue();
    assertThat(body.path("data").path("sucursalesOperables").isArray()).isTrue();
    return body.path("data").path("accessToken").asText();
  }

  private HttpRequest.Builder authenticatedRequest(String path, String token) {
    return request(path).header("Authorization", "Bearer " + token);
  }

  private HttpRequest.Builder request(String path) {
    return HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
  }

  private HttpResponse<String> send(HttpRequest.Builder request) throws Exception {
    return httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
  }
}
