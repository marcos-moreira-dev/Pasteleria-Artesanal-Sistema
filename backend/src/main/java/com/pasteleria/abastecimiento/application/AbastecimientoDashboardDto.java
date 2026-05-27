package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.util.List;

public record AbastecimientoDashboardDto(
    Integer itemsCriticos,
    Integer itemsBajoMinimo,
    Integer ordenesPendientes,
    Integer recepcionesHoy,
    BigDecimal costoReposicionEstimado,
    List<AlertaDto> alertas,
    List<SugerenciaReposicionDto> sugerenciasReposicion,
    List<MovimientoRecienteDto> movimientosRecientes,
    List<ProveedorConOCDto> proveedoresConOCActivas
) {
  public record AlertaDto(
      String tipo,
      String itemTipo,
      Long itemId,
      String itemNombre,
      String itemCodigo,
      String mensaje,
      BigDecimal cantidadActual,
      BigDecimal cantidadMinima,
      Integer coberturaDias
  ) {}

  public record SugerenciaReposicionDto(
      String itemTipo,
      Long itemId,
      String itemNombre,
      String codigo,
      BigDecimal stockActual,
      BigDecimal stockMinimo,
      BigDecimal cantidadSugerida,
      String proveedorPrincipal,
      BigDecimal precioUnitario
  ) {}

  public record MovimientoRecienteDto(
      Long id,
      String itemTipo,
      Long itemId,
      String itemNombre,
      String tipoMovimiento,
      BigDecimal cantidad,
      BigDecimal saldoPosterior,
      String fechaMovimiento,
      String registradoPorNombre
  ) {}

  public record ProveedorConOCDto(
      Long id,
      String nombre,
      String telefono,
      Integer ordenesActivas,
      String ultimaOrdenFecha
  ) {}
}
