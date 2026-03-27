package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrdenCompraRequest(
    @NotNull Long proveedorId,
    @NotBlank @Size(max = 40) String codigo,
    @Size(max = 2000) String observaciones,
    String fechaEntregaEstimada,
    @Valid @NotEmpty List<CreateOrdenCompraDetalleItem> detalles
) {
  public record CreateOrdenCompraDetalleItem(
      @NotBlank String itemTipo,
      @NotNull Long itemId,
      @NotNull @DecimalMin("0.0001") BigDecimal cantidad,
      @NotNull @DecimalMin("0.00") BigDecimal precioUnitario
  ) {
  }
}
