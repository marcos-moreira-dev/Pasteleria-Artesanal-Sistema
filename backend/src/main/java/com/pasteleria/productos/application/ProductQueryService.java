package com.pasteleria.productos.application;

import java.util.List;

import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.application.mapper.ProductDtoMapper;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Encapsula las consultas administrativas de productos para desacoplar la
 * lectura del flujo de mutaciones.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

  private final ProductRepositoryPort productRepository;
  private final ProductDtoMapper productDtoMapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public ProductQueryService(
      ProductRepositoryPort productRepository,
      ProductDtoMapper productDtoMapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.productRepository = productRepository;
    this.productDtoMapper = productDtoMapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<ProductSummary> listProducts() {
    return productRepository.findAllByOrderByActiveDescPublishedDescNameAsc().stream()
        .map(productDtoMapper::toSummary)
        .toList();
  }

  public PageResponseDto<ProductSummary> listProductsPage(int page, int size) {
    return pageMapper.toPageResponseDto(
        productRepository.findAllByOrderByActiveDescPublishedDescNameAsc(
            pageRequestFactory.create(
                page,
                size,
                Sort.by(
                    Sort.Order.desc("active"),
                    Sort.Order.desc("published"),
                    Sort.Order.asc("name")
                )
            )
        ).map(productDtoMapper::toSummary)
    );
  }
}


