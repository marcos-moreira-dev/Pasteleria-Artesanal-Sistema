package com.pasteleria.auth.application;

import com.pasteleria.common.security.SucursalOperable;

import java.util.List;

public record AuthResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
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
