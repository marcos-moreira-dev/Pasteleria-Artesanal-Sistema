package com.pasteleria.common.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.pasteleria.usuarios.domain.model.RoleCode;
import com.pasteleria.usuarios.infrastructure.persistence.entity.RoleEntity;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.junit.jupiter.api.Test;

class UserAccessPolicyTest {

  private final UserAccessPolicy policy = new UserAccessPolicy();

  @Test
  void adminShouldReceiveAllPermissionsAndDefaultBranch() {
    AuthenticatedUserContext context = policy.buildContext(user(RoleCode.ADMIN));

    assertThat(context.role()).isEqualTo("ADMIN");
    assertThat(context.permisos()).contains(Permisos.SEGURIDAD_ADMINISTRAR, Permisos.CONTRATOS_API_VER);
    assertThat(context.permisos()).containsAll(Permisos.all());
    assertThat(context.sucursalesOperables())
        .singleElement()
        .satisfies(sucursal -> {
          assertThat(sucursal.id()).isEqualTo(UserAccessPolicy.DEFAULT_SUCURSAL_ID);
          assertThat(sucursal.permisos()).contains(Permisos.CAJA_OPERAR, Permisos.PRODUCCION_OPERAR);
        });
  }

  @Test
  void atencionShouldReceiveOperationalSalesAndCashPermissionsButNotProduction() {
    AuthenticatedUserContext context = policy.buildContext(user(RoleCode.ATENCION));

    assertThat(context.permisos()).contains(Permisos.CLIENTES_GESTIONAR, Permisos.PEDIDOS_GESTIONAR, Permisos.CAJA_OPERAR);
    assertThat(context.permisos()).doesNotContain(Permisos.PRODUCCION_OPERAR, Permisos.SEGURIDAD_ADMINISTRAR);
    assertThat(context.sucursalesOperables()).singleElement()
        .satisfies(sucursal -> assertThat(sucursal.permisos()).contains(Permisos.CAJA_OPERAR));
  }

  @Test
  void produccionShouldReceiveProductionAndInventoryPermissionsButNotCashOperation() {
    AuthenticatedUserContext context = policy.buildContext(user(RoleCode.PRODUCCION));

    assertThat(context.permisos()).contains(Permisos.PRODUCCION_OPERAR, Permisos.RECETAS_GESTIONAR, Permisos.INVENTARIO_OPERAR);
    assertThat(context.permisos()).doesNotContain(Permisos.CAJA_OPERAR, Permisos.CLIENTES_GESTIONAR);
  }

  private UserEntity user(RoleCode roleCode) {
    RoleEntity role = new RoleEntity();
    role.setCode(roleCode);
    role.setName(roleCode.name());

    UserEntity user = new UserEntity();
    user.setId(10L);
    user.setUsername(roleCode.name().toLowerCase() + "@pasteleria.local");
    user.setFirstName("Usuario");
    user.setLastName(roleCode.name());
    user.setRole(role);
    user.setActive(true);
    return user;
  }
}
