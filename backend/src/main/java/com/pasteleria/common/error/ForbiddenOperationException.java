package com.pasteleria.common.error;

/**
 * Excepcion de aplicacion para operaciones autenticadas pero no autorizadas.
 */
public class ForbiddenOperationException extends RuntimeException {

  public ForbiddenOperationException(String message) {
    super(message);
  }
}
