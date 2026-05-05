package com.pasteleria.casosuso.api.dto;

import java.util.List;

public record CasoUsoOperativoResponse(
    Long id,
    String codigo,
    String modulo,
    String titulo,
    String actorPrincipal,
    String objetivo,
    String puntoInicio,
    Integer ordenVisual,
    String estado,
    Integer version,
    boolean activo,
    List<PasoCasoUsoResponse> pasos
) {
}
