package com.pasteleria.common.pagination;

import java.util.List;

/**
 * Contrato comun para exponer resultados paginados sin acoplar la API
 * directamente a Page de Spring Data.
 */
public record PageResponseDto<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    int numberOfElements,
    boolean first,
    boolean last,
    String sort
) {
}


