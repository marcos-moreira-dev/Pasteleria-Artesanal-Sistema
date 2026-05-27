package com.pasteleria.pedidos.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.pedidos.domain.model.OrderOrigin;
import com.pasteleria.pedidos.domain.model.OrderPriority;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.application.CreateOrderDetailRequest;
import com.pasteleria.pedidos.application.CreateOrderRequest;
import com.pasteleria.pedidos.application.OrderCommandService;
import com.pasteleria.pedidos.application.OrderDetailSummary;
import com.pasteleria.pedidos.application.OrderQueryService;
import com.pasteleria.pedidos.application.OrderSummary;
import com.pasteleria.pedidos.application.UpdateOrderStatusRequest;
import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private OrderQueryService orderQueryService;

  @MockitoBean
  private OrderCommandService orderCommandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldCreateOrder() throws Exception {
    CreateOrderRequest body = new CreateOrderRequest(
        1L,
        null,
        OffsetDateTime.now().plusDays(2),
        OrderPriority.NORMAL,
        OrderOrigin.INTERNO,
        "Pedido de prueba",
        List.of(new CreateOrderDetailRequest(1L, "Torta de chocolate mediana", 1, new BigDecimal("28.50"), null))
    );

    given(orderCommandService.createOrder(any(CreateOrderRequest.class), any(HttpServletRequest.class)))
        .willReturn(sampleOrderSummary(OrderStatus.REGISTRADO, ProductionStatus.PENDIENTE));

    mockMvc.perform(post("/api/v1/pedidos")
            .header("X-Request-Id", "req-order-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.code").value("PED-1001"))
        .andExpect(jsonPath("$.data.productionStatus").value("PENDIENTE"));
  }

  @Test
  void shouldUpdateOrderStatus() throws Exception {
    given(orderCommandService.updateOrderStatus(eq(1001L), any(UpdateOrderStatusRequest.class), any(HttpServletRequest.class)))
        .willReturn(sampleOrderSummary(OrderStatus.LISTO, ProductionStatus.FINALIZADO));

    mockMvc.perform(patch("/api/v1/pedidos/1001/estado")
            .header("X-Request-Id", "req-order-002")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new UpdateOrderStatusRequest(OrderStatus.LISTO, "Produccion completa"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("LISTO"));
  }

  @Test
  void shouldDeleteOrder() throws Exception {
    willDoNothing().given(orderCommandService).deleteOrder(eq(1001L), any(HttpServletRequest.class));

    mockMvc.perform(delete("/api/v1/pedidos/1001").header("X-Request-Id", "req-order-003"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  private OrderSummary sampleOrderSummary(OrderStatus orderStatus, ProductionStatus productionStatus) {
    return new OrderSummary(
        1001L,
        "PED-1001",
        1L,
        "Maria Lopez",
        null,
        orderStatus,
        OrderPriority.NORMAL,
        OrderOrigin.INTERNO,
        OffsetDateTime.now(),
        OffsetDateTime.now().plusDays(2),
        null,
        "Pedido de prueba",
        new BigDecimal("28.50"),
        productionStatus,
        ProductionPriority.NORMAL,
        List.of(new OrderDetailSummary(1L, 1L, "Torta de chocolate mediana", 1, new BigDecimal("28.50"), new BigDecimal("28.50"), null))
    );
  }
}


