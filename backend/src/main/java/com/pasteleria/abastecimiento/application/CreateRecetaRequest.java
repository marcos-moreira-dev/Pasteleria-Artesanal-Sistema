package com.pasteleria.abastecimiento.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateRecetaRequest(
    Long productoId,
    @NotBlank @Size(max = 160) String nombre,
    @DecimalMin("0.001") BigDecimal rendimientoBase,
    String observaciones,
    Boolean esActiva,
    @NotNull List<@Valid DetalleRecetaItem> detalles
) {
  public record DetalleRecetaItem(
      @NotNull Long ingredienteId,
      @NotNull @DecimalMin("0.0001") BigDecimal cantidadBase,
      BigDecimal rendimientoPorUnidad,
      Boolean esParaPorcion,
      String observaciones
  ) {
  }
}
