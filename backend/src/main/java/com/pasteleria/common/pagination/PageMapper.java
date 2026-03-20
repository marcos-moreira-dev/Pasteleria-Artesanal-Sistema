package com.pasteleria.common.pagination;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Resume resultados paginados en un DTO estable para frontend, reportes y
 * futuros endpoints administrativos.
 */
@Component
public class PageMapper {

  public <T> PageResponseDto<T> toPageResponseDto(Page<T> page) {
    return new PageResponseDto<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.getNumberOfElements(),
        page.isFirst(),
        page.isLast(),
        summarizeSort(page.getSort())
    );
  }

  public <T> PageResponseDto<T> toPageResponseDto(
      java.util.List<T> content,
      int page,
      int size,
      long totalElements,
      String sort
  ) {
    int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
    return new PageResponseDto<>(
        content,
        page,
        size,
        totalElements,
        totalPages,
        content.size(),
        page <= 0,
        totalElements == 0 || page >= Math.max(totalPages - 1, 0),
        sort
    );
  }

  private String summarizeSort(Sort sort) {
    if (sort == null || sort.isUnsorted()) {
      return null;
    }

    return sort.stream()
        .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
        .collect(Collectors.joining(";"));
  }
}


