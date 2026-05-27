package com.pasteleria.common.error;

/**
 * Indica que la operacion solicitada rompe una regla funcional del dominio.
 */
public class BusinessRuleException extends RuntimeException {

  public BusinessRuleException(String message) {
    super(message);
  }
}


