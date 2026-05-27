package com.pasteleria.auth.application;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Endurece la configuración JWT para no aceptar secretos vacíos y evitar que
 * producción use la clave de desarrollo documentada para el entorno local.
 */
@Component
public class JwtConfigurationValidator {

  static final String DEV_FALLBACK_SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public JwtConfigurationValidator(JwtProperties jwtProperties, Environment environment) {
    String secret = jwtProperties.getSecret();
    boolean prodProfile = Arrays.stream(environment.getActiveProfiles()).anyMatch("prod"::equalsIgnoreCase);

    if (!StringUtils.hasText(secret)) {
      throw new IllegalStateException("JWT_SECRET es obligatorio para iniciar el backend.");
    }

    if (prodProfile && DEV_FALLBACK_SECRET.equals(secret)) {
      throw new IllegalStateException("Producción no puede usar el JWT secret de desarrollo.");
    }
  }
}
