package com.pasteleria.common.api;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Centraliza la resolucion del identificador de correlacion para respuestas,
 * auditoria y logs. Si el cliente no envia uno, el backend puede generar un
 * valor temporal para no perder trazabilidad entre capas.
 */
public final class RequestIdSupport {

  public static final String REQUEST_ID_HEADER = "X-Request-Id";
  public static final String REQUEST_ID_ATTRIBUTE = "requestId";

  private RequestIdSupport() {
  }

  public static String resolve(HttpServletRequest request) {
    if (request == null) {
      return null;
    }

    String fromAttribute = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
    if (fromAttribute != null && !fromAttribute.isBlank()) {
      return fromAttribute;
    }

    String fromHeader = request.getHeader(REQUEST_ID_HEADER);
    if (fromHeader != null && !fromHeader.isBlank()) {
      return fromHeader;
    }

    return null;
  }

  public static String resolveOrGenerate(HttpServletRequest request) {
    String requestId = resolve(request);
    return requestId != null ? requestId : UUID.randomUUID().toString();
  }
}


