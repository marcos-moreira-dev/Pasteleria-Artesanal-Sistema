package com.pasteleria.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.auth.application.AuthResponse;
import com.pasteleria.auth.application.AuthService;
import com.pasteleria.auth.application.LoginRequest;
import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.pasteleria.common.security.AuthenticatedUserContext;
import com.pasteleria.common.security.SucursalOperable;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldLoginSuccessfully() throws Exception {
    given(authService.login(any(LoginRequest.class)))
        .willReturn(authResponse());

    mockMvc.perform(post("/api/v1/auth/login")
            .header("X-Request-Id", "req-auth-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new LoginRequest("admin", "password"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.accessToken").value("jwt-token"))
        .andExpect(jsonPath("$.data.username").value("admin"))
        .andExpect(jsonPath("$.data.displayName").value("Administrador"))
        .andExpect(jsonPath("$.data.permisos[0]").value("CONTRATOS_API_VER"))
        .andExpect(jsonPath("$.data.sucursalesOperables[0].codigo").value("MATRIZ"))
        .andExpect(jsonPath("$.requestId").value("req-auth-001"));
  }

  @Test
  void shouldReturnCurrentUserContext() throws Exception {
    given(authService.me(eq("admin"))).willReturn(userContext());

    mockMvc.perform(get("/api/v1/auth/me")
            .principal(() -> "admin")
            .header("X-Request-Id", "req-auth-me-001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("admin"))
        .andExpect(jsonPath("$.data.sucursalesOperables[0].codigo").value("MATRIZ"));
  }

  private AuthResponse authResponse() {
    return new AuthResponse(
        "jwt-token",
        "Bearer",
        7200,
        1L,
        "admin",
        "Administrador",
        "ADMIN",
        List.of("ADMIN"),
        List.of("ADMIN"),
        List.of("CONTRATOS_API_VER"),
        List.of("CONTRATOS_API_VER"),
        List.of(new SucursalOperable("MATRIZ", "MATRIZ", "Sucursal principal", true, List.of("PEDIDOS_VER")))
    );
  }

  private AuthenticatedUserContext userContext() {
    return new AuthenticatedUserContext(
        1L,
        "admin",
        "Administrador",
        "ADMIN",
        List.of("ADMIN"),
        List.of("ADMIN"),
        List.of("CONTRATOS_API_VER"),
        List.of("CONTRATOS_API_VER"),
        List.of(new SucursalOperable("MATRIZ", "MATRIZ", "Sucursal principal", true, List.of("PEDIDOS_VER")))
    );
  }
}
