package com.pasteleria.reportes.api;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import com.pasteleria.reportes.application.CreateReportJobRequest;
import com.pasteleria.reportes.application.LocalReportStorageService;
import com.pasteleria.reportes.application.ReportActionResult;
import com.pasteleria.reportes.application.ReportCommandService;
import com.pasteleria.reportes.application.ReportJobSelectionRequest;
import com.pasteleria.reportes.application.ReportJobSummary;
import com.pasteleria.reportes.application.ReportQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Reportes", description = "Generacion asincrona y descarga de reportes.")
@RestController
@RequestMapping("/api/v1/reportes")
public class ReportController {

  private final ReportQueryService reportQueryService;
  private final ReportCommandService reportCommandService;
  private final LocalReportStorageService localReportStorageService;

  public ReportController(
      ReportQueryService reportQueryService,
      ReportCommandService reportCommandService,
      LocalReportStorageService localReportStorageService
  ) {
    this.reportQueryService = reportQueryService;
    this.reportCommandService = reportCommandService;
    this.localReportStorageService = localReportStorageService;
  }

  @Operation(summary = "Solicitar reporte asincrono.")
  @PostMapping
  public ResponseEntity<ApiResponse<ReportJobSummary>> requestReport(
      @Valid @RequestBody CreateReportJobRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.accepted().body(ResponseFactory.created(
        "Reporte solicitado correctamente.",
        reportCommandService.requestReport(body, request),
        request
    ));
  }

  @Operation(summary = "Listar reportes recientes de la sesion.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ReportJobSummary>>> listJobs(
      @RequestParam(defaultValue = "10") int limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Jobs de reporte obtenidos correctamente.",
        reportQueryService.latestJobs(limit),
        request
    ));
  }

  @Operation(summary = "Listar jobs de reporte de forma paginada.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ReportJobSummary>>> pagedJobs(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "8") @Min(1) @Max(25) int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Jobs de reporte paginados obtenidos correctamente.",
        reportQueryService.pagedJobs(page, size),
        request
    ));
  }

  @Operation(summary = "Descargar archivo generado por un job de reporte.")
  @GetMapping("/{jobId}/descargar")
  public ResponseEntity<InputStreamResource> download(@PathVariable Long jobId) throws IOException {
    FileResourceEntity file = reportQueryService.resolveOwnedFile(jobId);
    var path = localReportStorageService.resolvePath(file);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(file.getMimeType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalName() + "\"")
        .body(new InputStreamResource(Files.newInputStream(path)));
  }

  @Operation(summary = "Eliminar jobs terminales seleccionados por el usuario actual.")
  @DeleteMapping("/seleccionados")
  public ResponseEntity<ApiResponse<ReportActionResult>> deleteSelected(
      @Valid @RequestBody ReportJobSelectionRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Jobs de reporte eliminados correctamente.",
        reportCommandService.deleteSelectedJobs(body),
        request
    ));
  }

  @Operation(summary = "Conservar solo los jobs terminales mas recientes del usuario actual.")
  @DeleteMapping("/antiguos")
  public ResponseEntity<ApiResponse<ReportActionResult>> cleanupOldJobs(
      @RequestParam(defaultValue = "10") @Min(1) @Max(25) int keepLatest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Historial de reportes depurado correctamente.",
        reportCommandService.cleanupTerminalHistory(keepLatest),
        request
    ));
  }
}


