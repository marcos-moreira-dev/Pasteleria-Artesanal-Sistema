package com.pasteleria.produccion.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;

import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.application.ProductionCommandService;
import com.pasteleria.produccion.application.ProductionQueryService;
import com.pasteleria.produccion.application.ProductionSummary;
import com.pasteleria.produccion.application.UpdateProductionStatusRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(ProductionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private ProductionQueryService productionQueryService;

  @MockitoBean
  private ProductionCommandService productionCommandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldUpdateProductionStatus() throws Exception {
    given(productionCommandService.updateProductionStatus(eq(1L), any(UpdateProductionStatusRequest.class), any(HttpServletRequest.class)))
        .willReturn(new ProductionSummary(
            1L,
            1001L,
            "PED-1001",
            "Maria Lopez",
            ProductionStatus.PREPARACION,
            ProductionPriority.NORMAL,
            OffsetDateTime.now(),
            null,
            "Produccion iniciada",
            OffsetDateTime.now()
        ));

    mockMvc.perform(patch("/api/v1/produccion/1/estado")
            .header("X-Request-Id", "req-prod-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new UpdateProductionStatusRequest(ProductionStatus.PREPARACION, "Inicio de preparacion"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("PREPARACION"));
  }
}


