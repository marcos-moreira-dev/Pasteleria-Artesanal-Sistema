package com.pasteleria.abastecimiento.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;

import com.pasteleria.abastecimiento.application.ProveedorCommandService;
import com.pasteleria.abastecimiento.application.ProveedorQueryService;
import com.pasteleria.abastecimiento.application.ProveedorSummary;
import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(ProveedorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProveedorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProveedorQueryService queryService;

  @MockitoBean
  private ProveedorCommandService commandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldToggleProveedorActivo() throws Exception {
    given(commandService.toggleActivo(eq(7L), any(HttpServletRequest.class)))
        .willReturn(new ProveedorSummary(
            7L,
            "PROV-007",
            "Distribuidora Centro",
            "0990000000",
            "ventas@centro.local",
            "Av. Principal 123",
            "Cambio de estado",
            false,
            OffsetDateTime.parse("2026-03-25T09:00:00-05:00")
        ));

    mockMvc.perform(patch("/api/v1/abastecimiento/proveedores/7/toggle-activo"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(7))
        .andExpect(jsonPath("$.data.activo").value(false));
  }
}
