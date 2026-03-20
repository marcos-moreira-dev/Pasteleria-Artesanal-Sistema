package com.pasteleria.auth.application;

import com.pasteleria.common.error.AuthenticationFailedException;
import com.pasteleria.usuarios.application.port.UserRepositoryPort;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Centraliza la autenticación del backoffice contra usuarios activos del sistema.
 *
 * <p>Devuelve siempre el mismo mensaje de error para usuario inexistente o
 * contraseña inválida, evitando filtrar información útil para enumeración de cuentas.</p>
 */
@Service
public class AuthService {

  private final UserRepositoryPort userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;

  public AuthService(
      UserRepositoryPort userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenService jwtTokenService
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenService = jwtTokenService;
  }

  /**
   * Valida credenciales y emite el JWT del operador autenticado.
   */
  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request) {
    UserEntity user = userRepository.findByUsernameAndActiveTrue(request.username())
        .orElseThrow(() -> new AuthenticationFailedException("Credenciales inválidas."));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new AuthenticationFailedException("Credenciales inválidas.");
    }

    String role = user.getRole().getCode().name();
    return new AuthResponse(
        jwtTokenService.generateToken(user.getUsername(), role),
        "Bearer",
        jwtTokenService.getExpirationSeconds(),
        user.getUsername(),
        role
    );
  }
}
