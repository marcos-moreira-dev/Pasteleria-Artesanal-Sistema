package com.pasteleria.abastecimiento.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.abastecimiento.application.InventarioMovimientoCommandService;
import com.pasteleria.abastecimiento.application.InventarioMovimientoQueryService;
import com.pasteleria.abastecimiento.application.InventarioMovimientoSummary;
import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventarioMovimientoController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventarioMovimientoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private InventarioMovimientoQueryService queryService;

  @MockitoBean
  private InventarioMovimientoCommandService commandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldListMovimientosWithFilters() throws Exception {
    given(queryService.listMovimientos(any(), any(), any(), any(), any()))
        .willReturn(List.of(sampleSummary()));

    mockMvc.perform(get("/api/v1/abastecimiento/inventario")
            .param("tipoMovimiento", "SALIDA_MERMA")
            .param("fechaDesde", "2026-03-01")
            .param("fechaHasta", "2026-03-31"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].tipoMovimiento").value("SALIDA_MERMA"))
        .andExpect(jsonPath("$.data[0].itemNombre").value("Harina 0000"));
  }

  private InventarioMovimientoSummary sampleSummary() {
    return new InventarioMovimientoSummary(
        11L,
        "INGREDIENTE",
        1L,
        "Harina 0000",
        "SALIDA_MERMA",
        new BigDecimal("2.500"),
        new BigDecimal("26.500"),
        new BigDecimal("24.000"),
        "AJUSTE",
        "AJ-100",
        "Merma por vencimiento",
        "Lote descartado",
        OffsetDateTime.parse("2026-03-25T10:15:00-05:00"),
        1L,
        "admin"
    );
  }
}
