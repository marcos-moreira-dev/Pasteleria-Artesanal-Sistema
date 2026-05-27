package com.pasteleria.caja.api;

import java.util.List;

import com.pasteleria.caja.application.AbrirCajaRequest;
import com.pasteleria.caja.application.CajaCommandService;
import com.pasteleria.caja.application.CajaEstadoSummary;
import com.pasteleria.caja.application.CajaQueryService;
import com.pasteleria.caja.application.CerrarCajaRequest;
import com.pasteleria.caja.application.MovimientoCajaSummary;
import com.pasteleria.caja.application.RegistrarMovimientoCajaRequest;
import com.pasteleria.caja.application.TurnoCajaSummary;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Caja", description = "Operación de caja: apertura, movimientos, cierre y consulta.")
@RestController
@RequestMapping("/api/v1/caja")
public class CajaController {

  private final CajaQueryService queryService;
  private final CajaCommandService commandService;

  public CajaController(CajaQueryService queryService, CajaCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Consultar estado de caja operativa.")
  @GetMapping("/estado")
  public ResponseEntity<ApiResponse<CajaEstadoSummary>> estado(
      @RequestParam(required = false) String cajaCodigo,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Estado de caja obtenido correctamente.",
        queryService.estado(cajaCodigo),
        request
    ));
  }

  @Operation(summary = "Consultar turno abierto de caja.")
  @GetMapping("/turnos/abierto")
  public ResponseEntity<ApiResponse<TurnoCajaSummary>> turnoAbierto(
      @RequestParam(required = false) String cajaCodigo,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Turno abierto obtenido correctamente.",
        queryService.turnoAbierto(cajaCodigo),
        request
    ));
  }

  @Operation(summary = "Listar movimientos de un turno de caja.")
  @GetMapping("/turnos/{turnoId}/movimientos")
  public ResponseEntity<ApiResponse<List<MovimientoCajaSummary>>> movimientos(
      @PathVariable @NotNull Long turnoId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Movimientos de caja obtenidos correctamente.",
        queryService.movimientos(turnoId),
        request
    ));
  }

  @Operation(summary = "Abrir turno de caja.")
  @PostMapping("/abrir")
  public ResponseEntity<ApiResponse<TurnoCajaSummary>> abrir(
      @Valid @RequestBody(required = false) AbrirCajaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Turno de caja abierto correctamente.",
        commandService.abrir(body, request),
        request
    ));
  }

  @Operation(summary = "Registrar movimiento de caja.")
  @PostMapping("/movimientos")
  public ResponseEntity<ApiResponse<MovimientoCajaSummary>> registrarMovimiento(
      @Valid @RequestBody RegistrarMovimientoCajaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Movimiento de caja registrado correctamente.",
        commandService.registrarMovimiento(body, request),
        request
    ));
  }

  @Operation(summary = "Cerrar turno de caja con arqueo.")
  @PostMapping("/cerrar")
  public ResponseEntity<ApiResponse<TurnoCajaSummary>> cerrar(
      @Valid @RequestBody CerrarCajaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Turno de caja cerrado correctamente.",
        commandService.cerrar(body, request),
        request
    ));
  }
}
