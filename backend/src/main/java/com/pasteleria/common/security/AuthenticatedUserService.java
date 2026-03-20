package com.pasteleria.common.security;

import java.util.Optional;

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

  public AuthenticatedUserService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<UserEntity> currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }
    return userRepository.findByUsernameAndActiveTrue(authentication.getName());
  }

  public String currentUsernameOrNull() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return authentication.getName();
  }
}


