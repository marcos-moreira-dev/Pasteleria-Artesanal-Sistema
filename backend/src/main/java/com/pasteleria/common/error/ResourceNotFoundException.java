package com.pasteleria.common.error;

/**
 * Excepcion de aplicacion para recursos inexistentes en consultas o mutaciones.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}


