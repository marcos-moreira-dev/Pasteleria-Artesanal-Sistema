package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RecibirOrdenCompraRequest(
    @Valid @NotEmpty List<RecibirOrdenCompraItem> items
) {
  public record RecibirOrdenCompraItem(
      @NotNull Long detalleId,
      @NotNull @DecimalMin("0.0001") BigDecimal cantidadRecibida
  ) {
  }
}
