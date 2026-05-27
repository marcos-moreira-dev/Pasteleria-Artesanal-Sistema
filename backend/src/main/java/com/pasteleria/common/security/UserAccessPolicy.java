package com.pasteleria.common.security;

import com.pasteleria.usuarios.domain.model.RoleCode;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * Politica transicional de permisos V1 -> ERP.
 *
 * <p>La aplicacion actual persiste un rol simple en rol_usuario. Esta politica lo
 * proyecta hacia permisos tecnicos y una sucursal operable principal para que el
 * backend y Angular empiecen a trabajar con contexto ERP sin romper el login
 * existente. En la V2 definitiva, estos permisos vendran de tablas de seguridad
 * normalizadas.</p>
 */
@Service
public class UserAccessPolicy {

  public static final String DEFAULT_SUCURSAL_ID = "MATRIZ";
  public static final String DEFAULT_SUCURSAL_CODE = "MATRIZ";
  public static final String DEFAULT_SUCURSAL_NAME = "Sucursal principal";

  public AuthenticatedUserContext buildContext(UserEntity user) {
    RoleCode role = user.getRole().getCode();
    List<String> permissions = permissionsFor(role);
    List<String> globalPermissions = globalPermissionsFor(role, permissions);
    List<String> branchPermissions = branchPermissionsFor(role, permissions);

    return new AuthenticatedUserContext(
        user.getId(),
        user.getUsername(),
        displayName(user),
        role.name(),
        rolesGlobalesFor(role),
        List.of(role.name()),
        globalPermissions,
        permissions,
        List.of(new SucursalOperable(
            DEFAULT_SUCURSAL_ID,
            DEFAULT_SUCURSAL_CODE,
            DEFAULT_SUCURSAL_NAME,
            true,
            branchPermissions
        ))
    );
  }

  public List<String> permissionsFor(RoleCode role) {
    if (role == RoleCode.ADMIN) {
      return Permisos.all();
    }

    Set<String> permissions = new LinkedHashSet<>();
    if (role == RoleCode.ATENCION) {
      add(permissions,
          Permisos.CLIENTES_VER,
          Permisos.CLIENTES_GESTIONAR,
          Permisos.TERCEROS_VER,
          Permisos.PRODUCTOS_VER,
          Permisos.COTIZACIONES_VER,
          Permisos.COTIZACIONES_GESTIONAR,
          Permisos.PEDIDOS_VER,
          Permisos.PEDIDOS_GESTIONAR,
          Permisos.CAJA_VER,
          Permisos.CAJA_OPERAR,
          Permisos.REPORTES_VER,
          Permisos.GUIA_OPERATIVA_VER,
          Permisos.ARCHIVOS_DESCARGAR
      );
    } else if (role == RoleCode.PRODUCCION) {
      add(permissions,
          Permisos.PRODUCTOS_VER,
          Permisos.PEDIDOS_VER,
          Permisos.PRODUCCION_VER,
          Permisos.PRODUCCION_OPERAR,
          Permisos.RECETAS_VER,
          Permisos.RECETAS_GESTIONAR,
          Permisos.INVENTARIO_VER,
          Permisos.INVENTARIO_OPERAR,
          Permisos.TERCEROS_VER,
          Permisos.COMPRAS_VER,
          Permisos.REPORTES_VER,
          Permisos.GUIA_OPERATIVA_VER,
          Permisos.ARCHIVOS_DESCARGAR
      );
    }
    return List.copyOf(permissions);
  }

  public boolean isGlobalPermission(String permission) {
    return Set.of(
        Permisos.REPORTES_VER,
        Permisos.REPORTES_SOLICITAR,
        Permisos.GUIA_OPERATIVA_VER,
        Permisos.AUDITORIA_VER,
        Permisos.SOPORTE_VER,
        Permisos.SOPORTE_GESTIONAR,
        Permisos.SEGURIDAD_ADMINISTRAR,
        Permisos.USUARIOS_GESTIONAR,
        Permisos.CONTRATOS_API_VER,
        Permisos.ARCHIVOS_DESCARGAR,
        Permisos.TESORERIA_VER,
        Permisos.CONTABILIDAD_VER,
        Permisos.ASIENTOS_REGISTRAR,
        Permisos.FISCAL_VER,
        Permisos.DOCUMENTOS_FISCALES_EMITIR
    ).contains(permission);
  }

  private List<String> globalPermissionsFor(RoleCode role, List<String> permissions) {
    if (role == RoleCode.ADMIN) {
      return permissions;
    }
    return permissions.stream().filter(this::isGlobalPermission).toList();
  }

  private List<String> branchPermissionsFor(RoleCode role, List<String> permissions) {
    if (role == RoleCode.ADMIN) {
      return permissions;
    }
    return permissions.stream().filter(permission -> !isGlobalPermission(permission)).toList();
  }

  private List<String> rolesGlobalesFor(RoleCode role) {
    if (role == RoleCode.ADMIN) {
      return List.of(RoleCode.ADMIN.name());
    }
    return List.of();
  }

  private String displayName(UserEntity user) {
    String first = user.getFirstName() == null ? "" : user.getFirstName().trim();
    String last = user.getLastName() == null ? "" : user.getLastName().trim();
    String full = (first + " " + last).trim();
    return full.isBlank() ? user.getUsername() : full;
  }

  private void add(Set<String> target, String... permissions) {
    target.addAll(List.of(permissions));
  }
}
