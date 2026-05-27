package com.pasteleria.common.security;

import java.util.Optional;

import com.pasteleria.common.error.AuthenticationFailedException;
import com.pasteleria.usuarios.application.port.UserRepositoryPort;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Centraliza la lectura del usuario autenticado para evitar repetir acceso al
 * contexto de seguridad en cada módulo.
 */
@Service
public class AuthenticatedUserService {

  private final UserRepositoryPort userRepository;
  private final UserAccessPolicy userAccessPolicy;

  public AuthenticatedUserService(UserRepositoryPort userRepository, UserAccessPolicy userAccessPolicy) {
    this.userRepository = userRepository;
    this.userAccessPolicy = userAccessPolicy;
  }

  public Optional<UserEntity> currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }
    return userRepository.findByUsernameAndActiveTrue(authentication.getName());
  }

  public UserEntity currentUserOrThrow() {
    return currentUser()
        .orElseThrow(() -> new AuthenticationFailedException("La sesion no corresponde a un usuario activo."));
  }

  public Optional<AuthenticatedUserContext> currentContext() {
    return currentUser().map(userAccessPolicy::buildContext);
  }

  public AuthenticatedUserContext currentContextOrThrow() {
    return userAccessPolicy.buildContext(currentUserOrThrow());
  }

  public String currentUsernameOrNull() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return authentication.getName();
  }
}
