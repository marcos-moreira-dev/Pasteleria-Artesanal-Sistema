package com.pasteleria.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.RequestIdSupport;
import com.pasteleria.common.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Responde problemas de seguridad con el mismo contrato JSON que el resto de la API.
 *
 * <p>Sin esta pieza, Spring Security puede devolver HTML o cuerpos vacios en 401/403,
 * dificultando el manejo uniforme desde Angular y la trazabilidad por requestId.</p>
 */
public class SecurityProblemSupport implements AuthenticationEntryPoint, AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public SecurityProblemSupport(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {
    writeError(
        response,
        HttpStatus.UNAUTHORIZED,
        "La sesión no es válida o no fue enviada.",
        ErrorCode.AUTHENTICATION_FAILED,
        RequestIdSupport.resolveOrGenerate(request)
    );
  }

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) throws IOException {
    writeError(
        response,
        HttpStatus.FORBIDDEN,
        "No tienes permisos para realizar esta operación.",
        ErrorCode.ACCESS_DENIED,
        RequestIdSupport.resolveOrGenerate(request)
    );
  }

  private void writeError(
      HttpServletResponse response,
      HttpStatus status,
      String message,
      ErrorCode code,
      String requestId
  ) throws IOException {
    if (response.isCommitted()) {
      return;
    }

    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setHeader(RequestIdSupport.REQUEST_ID_HEADER, requestId);
    objectMapper.writeValue(response.getWriter(), ApiResponse.error(message, code.code(), requestId));
  }
}
