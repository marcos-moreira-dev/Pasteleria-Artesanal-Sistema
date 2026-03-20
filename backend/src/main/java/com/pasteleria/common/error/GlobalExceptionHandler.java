package com.pasteleria.common.error;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.badRequest().body(
        new ApiErrorResponse(
            false,
            "La solicitud no cumple las validaciones requeridas.",
            "VALIDACION_INVALIDA",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
      ResourceNotFoundException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ApiErrorResponse(
            false,
            ex.getMessage(),
            "RECURSO_NO_ENCONTRADO",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ApiErrorResponse> handleAuthenticationFailed(
      AuthenticationFailedException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        new ApiErrorResponse(
            false,
            ex.getMessage(),
            "AUTENTICACION_INVALIDA",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleIntegrityViolation(
      DataIntegrityViolationException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ApiErrorResponse(
            false,
            "La operacion viola una restriccion de integridad del sistema.",
            "CONFLICTO_INTEGRIDAD",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ApiErrorResponse> handleOptimisticLock(
      ObjectOptimisticLockingFailureException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ApiErrorResponse(
            false,
            "Otro usuario actualizó el registro antes de que se pudiera guardar este cambio.",
            "CONCURRENCIA_OPTIMISTA",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalState(
      IllegalStateException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ApiErrorResponse(
            false,
            ex.getMessage(),
            "TRANSICION_INVALIDA",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<ApiErrorResponse> handleBusinessRule(
      BusinessRuleException ex,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ApiErrorResponse(
            false,
            ex.getMessage(),
            "REGLA_DE_NEGOCIO",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnexpected(
      Exception ex,
      HttpServletRequest request
  ) {
    LOGGER.error("Error no controlado para requestId={}", request.getHeader("X-Request-Id"), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ApiErrorResponse(
            false,
            "Se produjo un error interno no controlado.",
            "ERROR_INTERNO",
            request.getHeader("X-Request-Id"),
            OffsetDateTime.now()
        )
    );
  }
}


