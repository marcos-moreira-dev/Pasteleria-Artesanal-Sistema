package com.pasteleria.common.logging;

import com.pasteleria.common.api.RequestIdSupport;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Propaga el requestId a toda la peticion y deja un log tecnico compacto con
 * metodo, ruta, estado HTTP y duracion. No registra payloads ni credenciales.
 */
@Component
public class RequestCorrelationLoggingFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestCorrelationLoggingFilter.class);
  private static final Set<String> QUIET_PREFIXES = Set.of("/assets/", "/swagger-ui");
  private static final Set<String> QUIET_PATHS = Set.of("/actuator/health", "/actuator/info");

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String requestId = RequestIdSupport.resolveOrGenerate(request);
    Instant startedAt = Instant.now();

    request.setAttribute(RequestIdSupport.REQUEST_ID_ATTRIBUTE, requestId);
    response.setHeader(RequestIdSupport.REQUEST_ID_HEADER, requestId);
    MDC.put("requestId", requestId);

    try {
      filterChain.doFilter(request, response);
    } finally {
      try {
        if (!shouldSkip(request)) {
          long durationMs = Duration.between(startedAt, Instant.now()).toMillis();
          LOGGER.info(
              "requestId={} method={} path={} status={} durationMs={}",
              requestId,
              request.getMethod(),
              request.getRequestURI(),
              response.getStatus(),
              durationMs
          );
        }
      } finally {
        MDC.remove("requestId");
      }
    }
  }

  private boolean shouldSkip(HttpServletRequest request) {
    String path = request.getRequestURI();
    if (QUIET_PATHS.contains(path)) {
      return true;
    }
    return QUIET_PREFIXES.stream().anyMatch(path::startsWith);
  }
}
