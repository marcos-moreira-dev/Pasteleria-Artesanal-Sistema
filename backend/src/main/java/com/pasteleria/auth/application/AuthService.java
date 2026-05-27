package com.pasteleria.auth.application;

import com.pasteleria.common.error.AuthenticationFailedException;
import com.pasteleria.common.security.AuthenticatedUserContext;
import com.pasteleria.common.security.UserAccessPolicy;
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
  private final UserAccessPolicy userAccessPolicy;

  public AuthService(
      UserRepositoryPort userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenService jwtTokenService,
      UserAccessPolicy userAccessPolicy
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenService = jwtTokenService;
    this.userAccessPolicy = userAccessPolicy;
  }

  /**
   * Valida credenciales y emite el JWT del operador autenticado junto con su
   * contexto operativo transicional.
   */
  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request) {
    UserEntity user = userRepository.findByUsernameAndActiveTrue(request.username())
        .orElseThrow(() -> new AuthenticationFailedException("Credenciales inválidas."));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new AuthenticationFailedException("Credenciales inválidas.");
    }

    return buildResponse(user);
  }

  /**
   * Construye el contexto operativo del usuario ya autenticado para /auth/me.
   */
  @Transactional(readOnly = true)
  public AuthenticatedUserContext me(String username) {
    UserEntity user = userRepository.findByUsernameAndActiveTrue(username)
        .orElseThrow(() -> new AuthenticationFailedException("La sesion ya no corresponde a un usuario activo."));
    return userAccessPolicy.buildContext(user);
  }

  private AuthResponse buildResponse(UserEntity user) {
    AuthenticatedUserContext context = userAccessPolicy.buildContext(user);
    return new AuthResponse(
        jwtTokenService.generateToken(user.getUsername(), context.role()),
        "Bearer",
        jwtTokenService.getExpirationSeconds(),
        context.userId(),
        context.username(),
        context.displayName(),
        context.role(),
        context.rolesGlobales(),
        context.roles(),
        context.permisosGlobales(),
        context.permisos(),
        context.sucursalesOperables()
    );
  }
}
