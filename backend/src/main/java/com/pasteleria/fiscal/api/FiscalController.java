package com.pasteleria.fiscal.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.fiscal.application.AnularDocumentoFiscalRequest;
import com.pasteleria.fiscal.application.DocumentoFiscalSummary;
import com.pasteleria.fiscal.application.FiscalCommandService;
import com.pasteleria.fiscal.application.FiscalQueryService;
import com.pasteleria.fiscal.application.RegistrarDocumentoFiscalRequest;

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
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Fiscalidad", description = "Documentos fiscales internos preparados sin integracion SRI productiva.")
@RestController
@RequestMapping("/api/v1/fiscal")
public class FiscalController {

  private final FiscalQueryService queryService;
  private final FiscalCommandService commandService;

  public FiscalController(FiscalQueryService queryService, FiscalCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar documentos fiscales internos.")
  @GetMapping("/documentos")
  public ResponseEntity<ApiResponse<List<DocumentoFiscalSummary>>> documentos(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documentos fiscales obtenidos correctamente.",
        queryService.documentos(),
        request
    ));
  }

  @Operation(summary = "Consultar documento fiscal interno.")
  @GetMapping("/documentos/{id}")
  public ResponseEntity<ApiResponse<DocumentoFiscalSummary>> documento(@PathVariable Long id, HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documento fiscal obtenido correctamente.",
        queryService.documento(id),
        request
    ));
  }

  @Operation(summary = "Preparar documento fiscal interno.")
  @PostMapping("/documentos")
  public ResponseEntity<ApiResponse<DocumentoFiscalSummary>> preparar(
      @Valid @RequestBody RegistrarDocumentoFiscalRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Documento fiscal preparado correctamente.",
        commandService.preparar(body, request),
        request
    ));
  }

  @Operation(summary = "Marcar documento fiscal como emitido interno.")
  @PostMapping("/documentos/{id}/emitir-interno")
  public ResponseEntity<ApiResponse<DocumentoFiscalSummary>> emitirInterno(@PathVariable Long id, HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documento fiscal emitido internamente.",
        commandService.emitirInterno(id, request),
        request
    ));
  }

  @Operation(summary = "Anular documento fiscal interno.")
  @PostMapping("/documentos/{id}/anular")
  public ResponseEntity<ApiResponse<DocumentoFiscalSummary>> anular(
      @PathVariable Long id,
      @Valid @RequestBody AnularDocumentoFiscalRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Documento fiscal anulado correctamente.",
        commandService.anular(id, body, request),
        request
    ));
  }
}
