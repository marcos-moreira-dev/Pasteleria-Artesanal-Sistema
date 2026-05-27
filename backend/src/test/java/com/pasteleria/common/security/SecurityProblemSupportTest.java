package com.pasteleria.common.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;

class SecurityProblemSupportTest {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
  private final SecurityProblemSupport support = new SecurityProblemSupport(objectMapper);

  @Test
  void shouldWriteUnauthorizedAsStandardApiResponse() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("requestId", "req-sec-401");
    MockHttpServletResponse response = new MockHttpServletResponse();

    support.commence(request, response, new BadCredentialsException("bad"));

    JsonNode body = objectMapper.readTree(response.getContentAsString());
    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(response.getHeader("X-Request-Id")).isEqualTo("req-sec-401");
    assertThat(body.get("ok").asBoolean()).isFalse();
    assertThat(body.get("success").asBoolean()).isFalse();
    assertThat(body.at("/error/code").asText()).isEqualTo("AUTENTICACION_INVALIDA");
    assertThat(body.at("/meta/requestId").asText()).isEqualTo("req-sec-401");
  }

  @Test
  void shouldWriteForbiddenAsStandardApiResponse() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("requestId", "req-sec-403");
    MockHttpServletResponse response = new MockHttpServletResponse();

    support.handle(request, response, new AccessDeniedException("denied"));

    JsonNode body = objectMapper.readTree(response.getContentAsString());
    assertThat(response.getStatus()).isEqualTo(403);
    assertThat(response.getHeader("X-Request-Id")).isEqualTo("req-sec-403");
    assertThat(body.get("ok").asBoolean()).isFalse();
    assertThat(body.get("success").asBoolean()).isFalse();
    assertThat(body.at("/error/code").asText()).isEqualTo("ACCESO_DENEGADO");
    assertThat(body.at("/meta/requestId").asText()).isEqualTo("req-sec-403");
  }
}
