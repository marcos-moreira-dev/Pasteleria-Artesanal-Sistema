package com.pasteleria.abastecimiento.application;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public record UpdateRecetaRequest(
    String nombre,
    BigDecimal rendimientoBase,
    String observaciones,
    Boolean esActiva,
    List<DetalleRecetaItem> detalles
) {
  public record DetalleRecetaItem(
      @jakarta.validation.constraints.NotBlank Long ingredienteId,
      @jakarta.validation.constraints.NotNull BigDecimal cantidadBase,
      BigDecimal rendimientoPorUnidad,
      Boolean esParaPorcion,
      String observaciones
  ) {
  }
}
