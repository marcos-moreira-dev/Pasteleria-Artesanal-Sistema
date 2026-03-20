package com.pasteleria.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Normaliza solicitudes de paginacion para que el backend no acepte tamanos
 * arbitrarios ni indices negativos en los tableros administrativos.
 */
@Component
public class PageRequestFactory {

  private static final int DEFAULT_SIZE = 8;
  private static final int MAX_SIZE = 25;

  public Pageable create(int page, int size, Sort sort) {
    return PageRequest.of(normalizePage(page), normalizeSize(size), sort);
  }

  public int normalizePage(int page) {
    return Math.max(page, 0);
  }

  public int normalizeSize(int size) {
    if (size <= 0) {
      return DEFAULT_SIZE;
    }
    return Math.min(size, MAX_SIZE);
  }
}


