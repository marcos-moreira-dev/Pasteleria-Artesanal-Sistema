package com.pasteleria.common.text;

/**
 * Utilidades minimas de normalizacion de texto usadas en comandos y formularios.
 */
public final class TextSupport {

  private TextSupport() {
  }

  /**
   * Recorta un texto y devuelve {@code null} cuando llega vacio o solo con espacios.
   */
  public static String trimToNull(String value) {
    if (value == null) {
      return null;
    }
    String normalized = value.trim();
    return normalized.isEmpty() ? null : normalized;
  }
}


