package com.pasteleria.common.api;

import java.util.List;

/**
 * Error normalizado de API.
 */
public record ApiError(
    String code,
    String message,
    List<ApiErrorDetail> details
) {

  public ApiError {
    details = details == null ? List.of() : List.copyOf(details);
  }

  public static ApiError of(String code, String message) {
    return new ApiError(code, message, List.of());
  }

  public static ApiError of(String code, String message, List<ApiErrorDetail> details) {
    return new ApiError(code, message, details);
  }
}
