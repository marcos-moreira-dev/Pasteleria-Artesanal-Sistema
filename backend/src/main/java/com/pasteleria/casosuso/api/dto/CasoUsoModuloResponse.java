package com.pasteleria.casosuso.api.dto;

import java.util.List;

public record CasoUsoModuloResponse(
    String codigo,
    String nombre,
    String descripcion,
    String grupo,
    Integer ordenVisual,
    List<CasoUsoOperativoResponse> casos
) {
}
