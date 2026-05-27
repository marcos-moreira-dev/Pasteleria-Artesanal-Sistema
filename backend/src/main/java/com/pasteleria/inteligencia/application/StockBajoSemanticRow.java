package com.pasteleria.inteligencia.application;

import java.math.BigDecimal;

public record StockBajoSemanticRow(
    String tipoItem,
    Long itemId,
    String codigo,
    String nombre,
    BigDecimal stockActual,
    BigDecimal stockMinimo,
    String unidad
) {
}
