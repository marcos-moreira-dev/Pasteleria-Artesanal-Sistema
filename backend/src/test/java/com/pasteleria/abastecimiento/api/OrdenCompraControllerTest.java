package com.pasteleria.abastecimiento.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.abastecimiento.application.CreateOrdenCompraRequest;
import com.pasteleria.abastecimiento.application.OrdenCompraCommandService;
import com.pasteleria.abastecimiento.application.OrdenCompraDetailDto;
import com.pasteleria.abastecimiento.application.OrdenCompraQueryService;
import com.pasteleria.abastecimiento.application.OrdenCompraSummaryDto;
import com.pasteleria.abastecimiento.application.RecibirOrdenCompraRequest;
import com.pasteleria.abastecimiento.application.UpdateOrdenCompraEstadoRequest;
import com.pasteleria.abastecimiento.application.UpdateOrdenCompraRequest;
import com.pasteleria.abastecimiento.domain.model.EstadoOrdenCompra;
import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(OrdenCompraController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrdenCompraControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private OrdenCompraQueryService ordenCompraQueryService;

  @MockitoBean
  private OrdenCompraCommandService ordenCompraCommandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldCreateOrdenCompra() throws Exception {
    given(ordenCompraCommandService.createOrdenCompra(any(CreateOrdenCompraRequest.class), any(HttpServletRequest.class)))
        .willReturn(summary("OC-2026-0901", EstadoOrdenCompra.BORRADOR));

    CreateOrdenCompraRequest body = new CreateOrdenCompraRequest(
        1L,
        "OC-2026-0901",
        "Compra de prueba",
        "2026-03-30",
        List.of(new CreateOrdenCompraRequest.CreateOrdenCompraDetalleItem(
            "INGREDIENTE",
            1L,
            new BigDecimal("12"),
            new BigDecimal("1.50")
        ))
    );

    mockMvc.perform(post("/api/v1/abastecimiento/ordenes-compra")
            .header("X-Request-Id", "req-oc-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.codigo").value("OC-2026-0901"))
        .andExpect(jsonPath("$.data.estado").value("BORRADOR"));
  }

  @Test
  void shouldUpdateOrdenCompra() throws Exception {
    given(ordenCompraCommandService.updateOrdenCompra(eq(9L), any(UpdateOrdenCompraRequest.class), any(HttpServletRequest.class)))
        .willReturn(summary("OC-2026-0901", EstadoOrdenCompra.BORRADOR));

    UpdateOrdenCompraRequest body = new UpdateOrdenCompraRequest(
        2L,
        "OC-2026-0901",
        "Orden ajustada",
        "2026-04-01",
        List.of(new UpdateOrdenCompraRequest.UpdateOrdenCompraDetalleItem(
            "INSUMO",
            3L,
            new BigDecimal("24"),
            new BigDecimal("0.75")
        ))
    );

    mockMvc.perform(put("/api/v1/abastecimiento/ordenes-compra/9")
            .header("X-Request-Id", "req-oc-002")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.codigo").value("OC-2026-0901"));
  }

  @Test
  void shouldUpdateOrdenCompraEstado() throws Exception {
    given(ordenCompraCommandService.updateEstado(eq(9L), any(UpdateOrdenCompraEstadoRequest.class), any(HttpServletRequest.class)))
        .willReturn(summary("OC-2026-0901", EstadoOrdenCompra.ENVIADA));

    UpdateOrdenCompraEstadoRequest body = new UpdateOrdenCompraEstadoRequest(
        EstadoOrdenCompra.ENVIADA,
        "Se envia al proveedor"
    );

    mockMvc.perform(patch("/api/v1/abastecimiento/ordenes-compra/9/estado")
            .header("X-Request-Id", "req-oc-003")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.estado").value("ENVIADA"));
  }

  @Test
  void shouldReceiveOrdenCompra() throws Exception {
    given(ordenCompraCommandService.receiveOrdenCompra(eq(9L), any(RecibirOrdenCompraRequest.class), any(HttpServletRequest.class)))
        .willReturn(detail("OC-2026-0901", EstadoOrdenCompra.RECIBIDA_PARCIAL));

    RecibirOrdenCompraRequest body = new RecibirOrdenCompraRequest(
        List.of(new RecibirOrdenCompraRequest.RecibirOrdenCompraItem(
            14L,
            new BigDecimal("5")
        ))
    );

    mockMvc.perform(post("/api/v1/abastecimiento/ordenes-compra/9/recibir")
            .header("X-Request-Id", "req-oc-004")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.estado").value("RECIBIDA_PARCIAL"))
        .andExpect(jsonPath("$.data.detalles[0].cantidadRecibida").value(5));
  }

  private OrdenCompraSummaryDto summary(String codigo, EstadoOrdenCompra estado) {
    return new OrdenCompraSummaryDto(
        9L,
        codigo,
        estado.name(),
        1L,
        "Proveedor Demo",
        LocalDateTime.of(2026, 3, 25, 9, 0),
        LocalDateTime.of(2026, 3, 30, 0, 0),
        new BigDecimal("18.00"),
        LocalDateTime.of(2026, 3, 25, 8, 30)
    );
  }

  private OrdenCompraDetailDto detail(String codigo, EstadoOrdenCompra estado) {
    return new OrdenCompraDetailDto(
        9L,
        codigo,
        estado.name(),
        1L,
        "Proveedor Demo",
        "0999999999",
        "demo@proveedor.local",
        LocalDateTime.of(2026, 3, 25, 9, 0),
        LocalDateTime.of(2026, 3, 30, 0, 0),
        null,
        new BigDecimal("18.00"),
        "Recepción parcial",
        List.of(new OrdenCompraDetailDto.OrdenCompraDetalleDto(
            14L,
            "INGREDIENTE",
            1L,
            "Harina 0000",
            "HAR-001",
            10,
            new BigDecimal("1.8000"),
            new BigDecimal("18.00"),
            5,
            null
        )),
        LocalDateTime.of(2026, 3, 25, 8, 30),
        LocalDateTime.of(2026, 3, 25, 10, 0)
    );
  }
}
