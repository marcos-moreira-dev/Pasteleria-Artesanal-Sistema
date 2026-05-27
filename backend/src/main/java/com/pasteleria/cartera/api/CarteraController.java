package com.pasteleria.cartera.api;

import java.util.List;

import com.pasteleria.cartera.application.CarteraCommandService;
import com.pasteleria.cartera.application.CarteraQueryService;
import com.pasteleria.cartera.application.CobranzaSummary;
import com.pasteleria.cartera.application.CrearDocumentoCobrarRequest;
import com.pasteleria.cartera.application.DocumentoCobrarSummary;
import com.pasteleria.cartera.application.RegistrarCobranzaRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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
@Tag(name = "Cartera", description = "Documentos por cobrar y cobranzas aplicadas contra saldos.")
@RestController
@RequestMapping("/api/v1/cartera")
public class CarteraController {

  private final CarteraQueryService queryService;
  private final CarteraCommandService commandService;

  public CarteraController(CarteraQueryService queryService, CarteraCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar documentos por cobrar.")
  @GetMapping("/documentos-cobrar")
  public ResponseEntity<ApiResponse<List<DocumentoCobrarSummary>>> documentos(
      @RequestParam(required = false) String estado,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documentos por cobrar obtenidos correctamente.",
        queryService.documentos(estado),
        request
    ));
  }

  @Operation(summary = "Consultar documento por cobrar.")
  @GetMapping("/documentos-cobrar/{id}")
  public ResponseEntity<ApiResponse<DocumentoCobrarSummary>> documento(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documento por cobrar obtenido correctamente.",
        queryService.documento(id),
        request
    ));
  }

  @Operation(summary = "Crear documento por cobrar.")
  @PostMapping("/documentos-cobrar")
  public ResponseEntity<ApiResponse<DocumentoCobrarSummary>> crearDocumento(
      @Valid @RequestBody CrearDocumentoCobrarRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Documento por cobrar creado correctamente.",
        commandService.crearDocumento(body, request),
        request
    ));
  }

  @Operation(summary = "Listar cobranzas.")
  @GetMapping("/cobranzas")
  public ResponseEntity<ApiResponse<List<CobranzaSummary>>> cobranzas(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Cobranzas obtenidas correctamente.",
        queryService.cobranzas(),
        request
    ));
  }

  @Operation(summary = "Registrar cobranza aplicada contra documentos por cobrar.")
  @PostMapping("/cobranzas")
  public ResponseEntity<ApiResponse<CobranzaSummary>> registrarCobranza(
      @Valid @RequestBody RegistrarCobranzaRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Cobranza registrada correctamente.",
        commandService.registrarCobranza(body, request),
        request
    ));
  }
}
