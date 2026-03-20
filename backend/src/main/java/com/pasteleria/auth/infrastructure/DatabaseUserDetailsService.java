package com.pasteleria.auth.infrastructure;

import java.util.List;

import com.pasteleria.usuarios.application.port.UserRepositoryPort;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adapta los usuarios persistidos a Spring Security para login y resolucion de JWT.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

  private final UserRepositoryPort userRepository;

  public DatabaseUserDetailsService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Traduce el rol persistido a la convencion ROLE_* esperada por Spring Security.
   */
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsernameAndActiveTrue(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo."));

    return User.withUsername(user.getUsername())
        .password(user.getPasswordHash())
        .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode().name())))
        .disabled(!user.isActive())
        .build();
  }
}


