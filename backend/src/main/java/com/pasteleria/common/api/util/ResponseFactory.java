package com.pasteleria.common.api.util;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.RequestIdSupport;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Unifica la construccion de respuestas API para que los controladores no
 * repitan el armado del contrato comun ni la resolucion del request id.
 */
public final class ResponseFactory {

  private ResponseFactory() {
  }

  public static <T> ApiResponse<T> ok(String message, T data, HttpServletRequest request) {
    return ApiResponse.ok(message, data, RequestIdSupport.resolve(request));
  }

  public static <T> ApiResponse<T> created(String message, T data, HttpServletRequest request) {
    return ApiResponse.ok(message, data, RequestIdSupport.resolve(request));
  }
}


