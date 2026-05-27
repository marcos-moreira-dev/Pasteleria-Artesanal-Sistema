package com.pasteleria.terceros.application;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Vista compacta del tercero unificado para consultas administrativas.
 */
public record TerceroSummary(
    Long id,
    String nombreLegal,
    String nombreComercial,
    String telefono,
    String correo,
    String direccionPrincipal,
    List<TerceroPerfil> perfiles,
    Long clienteId,
    Long proveedorId,
    boolean activo,
    OffsetDateTime creadoEn
) {
}
