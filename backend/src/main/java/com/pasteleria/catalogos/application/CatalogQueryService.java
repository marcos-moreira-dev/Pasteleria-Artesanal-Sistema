package com.pasteleria.catalogos.application;

import java.util.List;

import com.pasteleria.catalogos.application.port.ProductCategoryRepositoryPort;
import com.pasteleria.catalogos.application.mapper.ProductCategoryDtoMapper;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.application.mapper.ProductDtoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CatalogQueryService {

  private final ProductCategoryRepositoryPort productCategoryRepository;
  private final ProductRepositoryPort productRepository;
  private final ProductCategoryDtoMapper productCategoryDtoMapper;
  private final ProductDtoMapper productDtoMapper;

  public CatalogQueryService(
      ProductCategoryRepositoryPort productCategoryRepository,
      ProductRepositoryPort productRepository,
      ProductCategoryDtoMapper productCategoryDtoMapper,
      ProductDtoMapper productDtoMapper
  ) {
    this.productCategoryRepository = productCategoryRepository;
    this.productRepository = productRepository;
    this.productCategoryDtoMapper = productCategoryDtoMapper;
    this.productDtoMapper = productDtoMapper;
  }

  public List<ProductCategorySummary> listActiveCategories() {
    return productCategoryRepository.findByActiveTrueOrderByVisualOrderAscNameAsc().stream()
        .map(productCategoryDtoMapper::toSummary)
        .toList();
  }

  public List<ProductSummary> listPublishedProducts() {
    return productRepository.findByPublishedTrueAndActiveTrueOrderByNameAsc().stream()
        .map(productDtoMapper::toSummary)
        .toList();
  }
}


