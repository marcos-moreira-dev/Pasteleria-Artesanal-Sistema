package com.pasteleria.usuarios.infrastructure.persistence.repository;

import com.pasteleria.usuarios.application.port.UserRepositoryPort;
import com.pasteleria.usuarios.domain.model.RoleCode;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryPort {

  @EntityGraph(attributePaths = "role")
  Optional<UserEntity> findByUsernameAndActiveTrue(String username);

  @EntityGraph(attributePaths = "role")
  List<UserEntity> findByRoleCodeInAndActiveTrue(Collection<RoleCode> roles);
}



