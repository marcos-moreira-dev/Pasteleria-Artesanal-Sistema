package com.pasteleria.common.security;

import java.util.List;

/**
 * Sucursal que el usuario puede operar desde la aplicacion administrativa.
 *
 * <p>En T09 se usa como proyeccion transicional sobre el modelo V1, donde aun no
 * existe una tabla fisica de sucursales. La V2 ERP migrara este contrato hacia
 * core.sucursal_operativa y asignaciones seguridad.usuario_sucursal.</p>
 */
public record SucursalOperable(
    String id,
    String codigo,
    String nombre,
    boolean principal,
    List<String> permisos
) {
}
