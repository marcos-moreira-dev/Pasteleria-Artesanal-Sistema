package com.pasteleria.soporte.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.soporte.application.SupportChecklistItem;
import com.pasteleria.soporte.application.SupportEvidenceService;
import com.pasteleria.soporte.application.SupportEvidenceSnapshot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Soporte", description = "Evidencia tecnica y checklist operativo para soporte administrativo.")
@RestController
@RequestMapping("/api/v1/soporte")
public class SoporteController {

  private final SupportEvidenceService evidenceService;

  public SoporteController(SupportEvidenceService evidenceService) {
    this.evidenceService = evidenceService;
  }

  @Operation(summary = "Obtener evidencia tecnica-operativa del sistema.")
  @GetMapping("/evidencia")
  public ResponseEntity<ApiResponse<SupportEvidenceSnapshot>> evidencia(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Evidencia de soporte obtenida correctamente.",
        evidenceService.snapshot(),
        request
    ));
  }

  @Operation(summary = "Obtener checklist de soporte y entrega.")
  @GetMapping("/checklist")
  public ResponseEntity<ApiResponse<List<SupportChecklistItem>>> checklist(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Checklist de soporte obtenido correctamente.",
        evidenceService.checklist(),
        request
    ));
  }
}
