package com.pasteleria.auth.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.pasteleria.common.security.UserAccessPolicy;
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
  private final UserAccessPolicy userAccessPolicy;

  public DatabaseUserDetailsService(UserRepositoryPort userRepository, UserAccessPolicy userAccessPolicy) {
    this.userRepository = userRepository;
    this.userAccessPolicy = userAccessPolicy;
  }

  /**
   * Traduce el rol persistido a la convencion ROLE_* y agrega permisos tecnicos
   * transicionales como authorities para futuras reglas de Spring Security.
   */
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsernameAndActiveTrue(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo."));

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode().name()));
    userAccessPolicy.permissionsFor(user.getRole().getCode()).stream()
        .map(SimpleGrantedAuthority::new)
        .forEach(authorities::add);

    return User.withUsername(user.getUsername())
        .password(user.getPasswordHash())
        .authorities(authorities)
        .disabled(!user.isActive())
        .build();
  }
}
