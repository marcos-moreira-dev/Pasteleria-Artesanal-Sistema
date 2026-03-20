package com.pasteleria.usuarios.application.port;

import com.pasteleria.usuarios.domain.model.RoleCode;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

  Optional<UserEntity> findByUsernameAndActiveTrue(String username);

  List<UserEntity> findByRoleCodeInAndActiveTrue(Collection<RoleCode> roles);
}
