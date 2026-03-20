package com.pasteleria.common.api;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Centraliza la lectura del identificador de correlacion expuesto por el cliente o gateway.
 */
public final class RequestIdSupport {

  private RequestIdSupport() {
  }

  public static String resolve(HttpServletRequest request) {
    return request.getHeader("X-Request-Id");
  }
}


