package com.pasteleria.common.error;

import com.pasteleria.common.api.ApiErrorDetail;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.RequestIdSupport;
import com.pasteleria.common.storage.FileStorageException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidation(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    List<ApiErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ApiErrorDetail(error.getField(), error.getDefaultMessage()))
        .sorted(Comparator.comparing(ApiErrorDetail::field))
        .toList();

    return buildError(
        HttpStatus.BAD_REQUEST,
        "La solicitud no cumple las validaciones requeridas.",
        ErrorCode.VALIDATION_ERROR,
        request,
        details
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFound(
      ResourceNotFoundException ex,
      HttpServletRequest request
  ) {
    return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), ErrorCode.RESOURCE_NOT_FOUND, request);
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAuthenticationFailed(
      AuthenticationFailedException ex,
      HttpServletRequest request
  ) {
    return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), ErrorCode.AUTHENTICATION_FAILED, request);
  }

  @ExceptionHandler(ForbiddenOperationException.class)
  public ResponseEntity<ApiResponse<Void>> handleForbiddenOperation(
      ForbiddenOperationException ex,
      HttpServletRequest request
  ) {
    return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), ErrorCode.ACCESS_DENIED, request);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleIntegrityViolation(
      DataIntegrityViolationException ex,
      HttpServletRequest request
  ) {
    return buildError(
        HttpStatus.CONFLICT,
        "La operacion viola una restriccion de integridad del sistema.",
        ErrorCode.DATA_INTEGRITY_CONFLICT,
        request
    );
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ApiResponse<Void>> handleOptimisticLock(
      ObjectOptimisticLockingFailureException ex,
      HttpServletRequest request
  ) {
    return buildError(
        HttpStatus.CONFLICT,
        "Otro usuario actualizó el registro antes de que se pudiera guardar este cambio.",
        ErrorCode.OPTIMISTIC_LOCK,
        request
    );
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalState(
      IllegalStateException ex,
      HttpServletRequest request
  ) {
    return buildError(HttpStatus.CONFLICT, ex.getMessage(), ErrorCode.INVALID_STATE_TRANSITION, request);
  }

  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<ApiResponse<Void>> handleBusinessRule(
      BusinessRuleException ex,
      HttpServletRequest request
  ) {
    return buildError(HttpStatus.CONFLICT, ex.getMessage(), ErrorCode.BUSINESS_RULE, request);
  }

  @ExceptionHandler(FileStorageException.class)
  public ResponseEntity<ApiResponse<Void>> handleStorage(
      FileStorageException ex,
      HttpServletRequest request
  ) {
    return buildError(HttpStatus.CONFLICT, ex.getMessage(), ErrorCode.STORAGE_ERROR, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleUnexpected(
      Exception ex,
      HttpServletRequest request
  ) {
    LOGGER.error("Error no controlado para requestId={}", RequestIdSupport.resolve(request), ex);
    return buildError(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Se produjo un error interno no controlado.",
        ErrorCode.INTERNAL_ERROR,
        request
    );
  }

  private ResponseEntity<ApiResponse<Void>> buildError(
      HttpStatus status,
      String message,
      ErrorCode code,
      HttpServletRequest request
  ) {
    return buildError(status, message, code, request, List.of());
  }

  private ResponseEntity<ApiResponse<Void>> buildError(
      HttpStatus status,
      String message,
      ErrorCode code,
      HttpServletRequest request,
      List<ApiErrorDetail> details
  ) {
    String requestId = RequestIdSupport.resolveOrGenerate(request);
    return ResponseEntity.status(status).body(ApiResponse.error(message, code.code(), requestId, details));
  }
}
