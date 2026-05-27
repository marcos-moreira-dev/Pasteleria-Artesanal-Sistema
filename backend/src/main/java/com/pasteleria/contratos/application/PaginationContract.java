package com.pasteleria.contratos.application;

/**
 * Politica publica de paginacion para tablas administrativas.
 */
public record PaginationContract(
    String pageParam,
    String sizeParam,
    int defaultPage,
    int defaultSize,
    int maxSize,
    String responseType
) {
}
