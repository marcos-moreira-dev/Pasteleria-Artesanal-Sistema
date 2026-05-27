package com.pasteleria.common.error;

/**
 * Representa fallos de autenticación sin revelar si el usuario existe o no.
 */
public class AuthenticationFailedException extends RuntimeException {

  public AuthenticationFailedException(String message) {
    super(message);
  }
}
