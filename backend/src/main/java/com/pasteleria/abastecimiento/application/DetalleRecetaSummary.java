package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;

public record DetalleRecetaSummary(
    Long id,
    Long recetaId,
    Long ingredienteId,
    String ingredienteNombre,
    String ingredienteUnidad,
    BigDecimal cantidadBase,
    BigDecimal rendimientoPorUnidad,
    Boolean esParaPorcion,
    String observaciones
) {
}
