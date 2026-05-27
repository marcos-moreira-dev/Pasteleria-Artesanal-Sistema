package com.pasteleria.common.security;

import com.pasteleria.common.error.ForbiddenOperationException;

import java.util.List;
import java.util.Arrays;

import org.springframework.stereotype.Service;

/**
 * Punto unico para exigir permisos globales o permisos sobre la sucursal activa.
 *
 * <p>En T09 opera sobre la proyeccion transicional de usuario/rol V1. En T09+
 * sus metodos deben ser usados por servicios de aplicacion criticos antes de
 * mutar caja, inventario, produccion, compras o seguridad.</p>
 */
@Service
public class OperacionAutorizacionService {

  private final AuthenticatedUserService authenticatedUserService;

  public OperacionAutorizacionService(AuthenticatedUserService authenticatedUserService) {
    this.authenticatedUserService = authenticatedUserService;
  }

  public AuthenticatedUserContext contextoActual() {
    return authenticatedUserService.currentContextOrThrow();
  }

  public void exigirPermisoGlobal(String permission) {
    if (!tienePermisoGlobal(permission)) {
      throw new ForbiddenOperationException("No tienes permiso global para realizar esta operacion: " + permission);
    }
  }


  public void exigirAlgunoPermisoGlobal(String... permissions) {
    boolean authorized = Arrays.stream(permissions).anyMatch(this::tienePermisoGlobal);
    if (!authorized) {
      throw new ForbiddenOperationException("No tienes permisos globales suficientes para realizar esta operacion.");
    }
  }

  public void exigirPermisoSucursal(String sucursalId, String permission) {
    exigirSucursalOperable(sucursalId);
    if (!tienePermisoSucursal(sucursalId, permission)) {
      throw new ForbiddenOperationException("No tienes permiso para operar la sucursal " + sucursalId + ": " + permission);
    }
  }

  public void exigirAlgunoPermisoSucursal(String sucursalId, List<String> permissions) {
    exigirSucursalOperable(sucursalId);
    boolean authorized = permissions.stream().anyMatch(permission -> tienePermisoSucursal(sucursalId, permission));
    if (!authorized) {
      throw new ForbiddenOperationException("No tienes permisos suficientes para operar la sucursal " + sucursalId + ".");
    }
  }

  public void exigirSucursalOperable(String sucursalId) {
    if (!esSucursalOperable(sucursalId)) {
      throw new ForbiddenOperationException("La sucursal no esta habilitada para el usuario autenticado: " + sucursalId);
    }
  }

  public boolean tienePermisoGlobal(String permission) {
    AuthenticatedUserContext context = contextoActual();
    return context.permisosGlobales().contains(permission) || context.permisos().contains(permission);
  }

  public boolean tienePermisoSucursal(String sucursalId, String permission) {
    AuthenticatedUserContext context = contextoActual();
    return context.sucursalesOperables().stream()
        .filter(sucursal -> sucursal.id().equals(sucursalId) || sucursal.codigo().equalsIgnoreCase(sucursalId))
        .anyMatch(sucursal -> sucursal.permisos().contains(permission) || context.permisosGlobales().contains(permission));
  }

  public boolean esSucursalOperable(String sucursalId) {
    AuthenticatedUserContext context = contextoActual();
    return context.sucursalesOperables().stream()
        .anyMatch(sucursal -> sucursal.id().equals(sucursalId) || sucursal.codigo().equalsIgnoreCase(sucursalId));
  }

  public List<String> sucursalIdsOperablesConPermiso(String permission) {
    AuthenticatedUserContext context = contextoActual();
    if (context.permisosGlobales().contains(permission)) {
      return context.sucursalesOperables().stream().map(SucursalOperable::id).toList();
    }
    return context.sucursalesOperables().stream()
        .filter(sucursal -> sucursal.permisos().contains(permission))
        .map(SucursalOperable::id)
        .toList();
  }
}
