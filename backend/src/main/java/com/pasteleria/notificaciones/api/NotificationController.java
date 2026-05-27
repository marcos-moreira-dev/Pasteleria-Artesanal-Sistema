package com.pasteleria.notificaciones.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.notificaciones.application.NotificationCommandService;
import com.pasteleria.notificaciones.application.NotificationCounters;
import com.pasteleria.notificaciones.application.NotificationQueryService;
import com.pasteleria.notificaciones.application.NotificationActionResult;
import com.pasteleria.notificaciones.application.NotificationSelectionRequest;
import com.pasteleria.notificaciones.application.NotificationSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Notificaciones", description = "Buzon interno del panel administrativo.")
@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificationController {

  private final NotificationQueryService notificationQueryService;
  private final NotificationCommandService notificationCommandService;

  public NotificationController(
      NotificationQueryService notificationQueryService,
      NotificationCommandService notificationCommandService
  ) {
    this.notificationQueryService = notificationQueryService;
    this.notificationCommandService = notificationCommandService;
  }

  @Operation(summary = "Listar notificaciones recientes de la sesion.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<NotificationSummary>>> listNotifications(
      @RequestParam(required = false) @Min(1) @Max(25) Integer limit,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Notificaciones obtenidas correctamente.",
        notificationQueryService.latestNotifications(limit),
        request
    ));
  }

  @Operation(summary = "Obtener contadores del buzon interno.")
  @GetMapping("/resumen")
  public ResponseEntity<ApiResponse<NotificationCounters>> counters(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Resumen de notificaciones obtenido correctamente.",
        notificationQueryService.counters(),
        request
    ));
  }

  @Operation(summary = "Marcar notificacion como leida.")
  @PatchMapping("/{notificationId}/leer")
  public ResponseEntity<ApiResponse<NotificationSummary>> markAsRead(
      @PathVariable Long notificationId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Notificacion marcada como leida.",
        notificationCommandService.markAsRead(notificationId),
        request
    ));
  }

  @Operation(summary = "Archivar notificacion.")
  @PatchMapping("/{notificationId}/archivar")
  public ResponseEntity<ApiResponse<NotificationSummary>> archive(
      @PathVariable Long notificationId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Notificacion archivada correctamente.",
        notificationCommandService.archive(notificationId),
        request
    ));
  }

  @Operation(summary = "Archivar un conjunto de notificaciones elegidas por el usuario.")
  @PatchMapping("/archivar-seleccion")
  public ResponseEntity<ApiResponse<NotificationActionResult>> archiveSelection(
      @Valid @RequestBody NotificationSelectionRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Notificaciones archivadas correctamente.",
        notificationCommandService.archiveSelection(body),
        request
    ));
  }

  @Operation(summary = "Archivar todas las notificaciones leidas del buzon actual.")
  @PatchMapping("/archivar-leidas")
  public ResponseEntity<ApiResponse<NotificationActionResult>> archiveReadNotifications(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Notificaciones leidas archivadas correctamente.",
        notificationCommandService.archiveReadNotifications(),
        request
    ));
  }
}


