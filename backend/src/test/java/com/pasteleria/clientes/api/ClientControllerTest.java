package com.pasteleria.clientes.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.clientes.application.ClientCommandService;
import com.pasteleria.clientes.application.ClientQueryService;
import com.pasteleria.clientes.application.ClientSummary;
import com.pasteleria.clientes.application.CreateClientRequest;
import com.pasteleria.clientes.application.UpdateClientRequest;
import com.pasteleria.common.pagination.PageResponseDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private ClientQueryService clientQueryService;

  @MockitoBean
  private ClientCommandService clientCommandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldListClients() throws Exception {
    given(clientQueryService.listClients())
        .willReturn(List.of(
            new ClientSummary(1L, "Maria Lopez", "0990000001", "maria@example.com", null, OffsetDateTime.now())
        ));

    mockMvc.perform(get("/api/v1/clientes").header("X-Request-Id", "req-client-001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].fullName").value("Maria Lopez"));
  }

  @Test
  void shouldListClientsPage() throws Exception {
    given(clientQueryService.listClientsPage(0, 8, ""))
        .willReturn(new PageResponseDto<>(
            List.of(new ClientSummary(1L, "Maria Lopez", "0990000001", "maria@example.com", null, OffsetDateTime.now())),
            0,
            8,
            1,
            1,
            1,
            true,
            true,
            "createdAt,desc"
        ));

    mockMvc.perform(get("/api/v1/clientes/paginado?page=0&size=8").header("X-Request-Id", "req-client-001b"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.totalElements").value(1))
        .andExpect(jsonPath("$.data.content[0].fullName").value("Maria Lopez"));
  }

  @Test
  void shouldCreateClient() throws Exception {
    given(clientCommandService.createClient(any(CreateClientRequest.class), any()))
        .willReturn(new ClientSummary(10L, "Cliente Nuevo", "0999999999", "nuevo@example.com", null, OffsetDateTime.now()));

    mockMvc.perform(post("/api/v1/clientes")
            .header("X-Request-Id", "req-client-002")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateClientRequest("Cliente Nuevo", "0999999999", "nuevo@example.com", null)
            )))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(10))
        .andExpect(jsonPath("$.data.fullName").value("Cliente Nuevo"));
  }

  @Test
  void shouldDeleteClient() throws Exception {
    willDoNothing().given(clientCommandService).deleteClient(eq(1L), any());

    mockMvc.perform(delete("/api/v1/clientes/1").header("X-Request-Id", "req-client-003"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void shouldUpdateClient() throws Exception {
    given(clientCommandService.updateClient(eq(1L), any(UpdateClientRequest.class), any()))
        .willReturn(new ClientSummary(1L, "Maria Lopez", "0990000001", "maria@example.com", "Actualizada", OffsetDateTime.now()));

    mockMvc.perform(put("/api/v1/clientes/1")
            .header("X-Request-Id", "req-client-004")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new UpdateClientRequest("Maria Lopez", "0990000001", "maria@example.com", "Actualizada")
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.notes").value("Actualizada"));
  }
}


