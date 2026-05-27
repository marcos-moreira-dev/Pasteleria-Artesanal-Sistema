package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenCompraDetailDto(
    Long id,
    String codigo,
    String estado,
    Long proveedorId,
    String proveedorNombre,
    String proveedorTelefono,
    String proveedorCorreo,
    LocalDateTime fechaEmision,
    LocalDateTime fechaEntregaEsperada,
    LocalDateTime fechaEntregaReal,
    BigDecimal totalEstimado,
    String observaciones,
    List<OrdenCompraDetalleDto> detalles,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
    public record OrdenCompraDetalleDto(
        Long id,
        String itemTipo,
        Long itemId,
        String itemNombre,
        String itemCodigo,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal,
        Integer cantidadRecibida,
        String observaciones
    ) {}
}
