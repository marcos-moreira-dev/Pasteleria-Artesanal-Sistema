package com.pasteleria.common.api;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Contrato transversal de respuesta API.
 *
 * <p>La forma nueva del contrato es {@code ok/data/error/meta}, inspirada en
 * Cedro. Durante la transicion se conservan campos legacy
 * {@code success/message/errorCode/requestId/timestamp} para no romper de golpe
 * el frontend administrativo ni los tests existentes.</p>
 */
public record ApiResponse<T>(
    boolean ok,
    T data,
    ApiError error,
    ApiMeta meta,

    // Campos legacy temporales. No crear nuevos consumidores sobre estos campos.
    boolean success,
    String message,
    String errorCode,
    String requestId,
    OffsetDateTime timestamp
) {

  public static <T> ApiResponse<T> ok(String message, T data, String requestId) {
    OffsetDateTime timestamp = OffsetDateTime.now();
    return new ApiResponse<>(
        true,
        data,
        null,
        new ApiMeta(requestId, timestamp),
        true,
        message,
        null,
        requestId,
        timestamp
    );
  }

  public static <T> ApiResponse<T> error(String message, String errorCode, String requestId) {
    return error(message, errorCode, requestId, List.of());
  }

  public static <T> ApiResponse<T> error(
      String message,
      String errorCode,
      String requestId,
      List<ApiErrorDetail> details
  ) {
    OffsetDateTime timestamp = OffsetDateTime.now();
    return new ApiResponse<>(
        false,
        null,
        ApiError.of(errorCode, message, details),
        new ApiMeta(requestId, timestamp),
        false,
        message,
        errorCode,
        requestId,
        timestamp
    );
  }
}
