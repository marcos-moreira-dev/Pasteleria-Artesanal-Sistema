package com.pasteleria.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.auth.application.AuthResponse;
import com.pasteleria.auth.application.AuthService;
import com.pasteleria.auth.application.LoginRequest;
import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;

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
        .willReturn(new AuthResponse("jwt-token", "Bearer", 7200, "admin", "ADMIN"));

    mockMvc.perform(post("/api/v1/auth/login")
            .header("X-Request-Id", "req-auth-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new LoginRequest("admin", "password"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.accessToken").value("jwt-token"))
        .andExpect(jsonPath("$.data.username").value("admin"))
        .andExpect(jsonPath("$.requestId").value("req-auth-001"));
  }
}


