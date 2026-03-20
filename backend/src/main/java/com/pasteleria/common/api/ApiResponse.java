package com.pasteleria.common.api;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    String errorCode,
    String requestId,
    OffsetDateTime timestamp
) {

  public static <T> ApiResponse<T> ok(String message, T data, String requestId) {
    return new ApiResponse<>(true, message, data, null, requestId, OffsetDateTime.now());
  }

  public static <T> ApiResponse<T> error(String message, String errorCode, String requestId) {
    return new ApiResponse<>(false, message, null, errorCode, requestId, OffsetDateTime.now());
  }
}


