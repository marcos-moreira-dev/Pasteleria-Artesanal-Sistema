package com.pasteleria.common.api.util;

import com.pasteleria.common.api.ApiErrorDetail;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.RequestIdSupport;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Unifica la construccion de respuestas API para que los controladores no
 * repitan el armado del contrato comun ni la resolucion del request id.
 */
public final class ResponseFactory {

  private ResponseFactory() {
  }

  public static <T> ApiResponse<T> ok(String message, T data, HttpServletRequest request) {
    return ApiResponse.ok(message, data, RequestIdSupport.resolveOrGenerate(request));
  }

  public static <T> ApiResponse<T> created(String message, T data, HttpServletRequest request) {
    return ApiResponse.ok(message, data, RequestIdSupport.resolveOrGenerate(request));
  }

  public static <T> ApiResponse<T> error(
      String message,
      String errorCode,
      HttpServletRequest request
  ) {
    return ApiResponse.error(message, errorCode, RequestIdSupport.resolveOrGenerate(request));
  }

  public static <T> ApiResponse<T> error(
      String message,
      String errorCode,
      HttpServletRequest request,
      List<ApiErrorDetail> details
  ) {
    return ApiResponse.error(message, errorCode, RequestIdSupport.resolveOrGenerate(request), details);
  }
}
