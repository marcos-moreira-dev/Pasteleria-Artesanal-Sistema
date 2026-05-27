package com.pasteleria.clientes.api;

import java.util.List;

import com.pasteleria.clientes.application.ClientCommandService;
import com.pasteleria.clientes.application.ClientQueryService;
import com.pasteleria.clientes.application.ClientSummary;
import com.pasteleria.clientes.application.CreateClientRequest;
import com.pasteleria.clientes.application.UpdateClientRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador administrativo del módulo de clientes.
 */
@Validated
@Tag(name = "Clientes", description = "Gestión comercial y mantenimiento de fichas de clientes.")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClientController {

  private final ClientQueryService clientQueryService;
  private final ClientCommandService clientCommandService;

  public ClientController(ClientQueryService clientQueryService, ClientCommandService clientCommandService) {
    this.clientQueryService = clientQueryService;
    this.clientCommandService = clientCommandService;
  }

  @Operation(summary = "Listar clientes registrados.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ClientSummary>>> listClients(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Clientes obtenidos correctamente.",
        clientQueryService.listClients(),
        request
    ));
  }

  @Operation(summary = "Listar clientes en formato paginado para tablas administrativas.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ClientSummary>>> listClientsPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pagina de clientes obtenida correctamente.",
        clientQueryService.listClientsPage(page, size, query),
        request
    ));
  }

  @Operation(summary = "Registrar cliente.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<ClientSummary>> createClient(
      @Valid @RequestBody CreateClientRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Cliente registrado correctamente.",
        clientCommandService.createClient(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar ficha de cliente.")
  @PutMapping("/{clientId}")
  public ResponseEntity<ApiResponse<ClientSummary>> updateClient(
      @PathVariable Long clientId,
      @Valid @RequestBody UpdateClientRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Cliente actualizado correctamente.",
        clientCommandService.updateClient(clientId, body, request),
        request
    ));
  }

  @Operation(summary = "Eliminar cliente sin trazabilidad comercial asociada.")
  @DeleteMapping("/{clientId}")
  public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long clientId, HttpServletRequest request) {
    clientCommandService.deleteClient(clientId, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Cliente eliminado correctamente.",
        null,
        request
    ));
  }
}


