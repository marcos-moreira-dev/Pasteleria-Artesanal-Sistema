package com.pasteleria.common.security;

import java.util.List;

/**
 * Contexto operativo que el frontend necesita para navegar y que el backend usa
 * como base de autorizacion transicional.
 */
public record AuthenticatedUserContext(
    Long userId,
    String username,
    String displayName,
    String role,
    List<String> rolesGlobales,
    List<String> roles,
    List<String> permisosGlobales,
    List<String> permisos,
    List<SucursalOperable> sucursalesOperables
) {
}
