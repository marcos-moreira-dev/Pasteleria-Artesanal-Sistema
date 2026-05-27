package com.pasteleria.common.error;

/**
 * Catalogo central de codigos de error API.
 *
 * <p>Mantiene los codigos legacy actuales para no romper integraciones y deja
 * una unica fuente para nuevas respuestas de error.</p>
 */
public enum ErrorCode {
  VALIDATION_ERROR("VALIDACION_INVALIDA"),
  RESOURCE_NOT_FOUND("RECURSO_NO_ENCONTRADO"),
  AUTHENTICATION_FAILED("AUTENTICACION_INVALIDA"),
  ACCESS_DENIED("ACCESO_DENEGADO"),
  DATA_INTEGRITY_CONFLICT("CONFLICTO_INTEGRIDAD"),
  OPTIMISTIC_LOCK("CONCURRENCIA_OPTIMISTA"),
  INVALID_STATE_TRANSITION("TRANSICION_INVALIDA"),
  BUSINESS_RULE("REGLA_DE_NEGOCIO"),
  STORAGE_ERROR("ERROR_STORAGE"),
  INTERNAL_ERROR("ERROR_INTERNO");

  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }

  public String code() {
    return code;
  }
}
