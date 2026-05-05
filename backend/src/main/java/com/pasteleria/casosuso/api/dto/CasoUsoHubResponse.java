package com.pasteleria.casosuso.api.dto;

import java.util.List;

public record CasoUsoHubResponse(
    Integer totalCasos,
    List<CasoUsoModuloResponse> modulos
) {
}
