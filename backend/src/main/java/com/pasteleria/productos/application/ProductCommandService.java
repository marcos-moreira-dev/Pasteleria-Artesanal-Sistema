package com.pasteleria.productos.application;

import java.time.OffsetDateTime;

import com.pasteleria.catalogos.application.port.ProductCategoryRepositoryPort;
import com.pasteleria.catalogos.infrastructure.persistence.entity.ProductCategoryEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.cotizaciones.application.port.QuotationDetailRepositoryPort;
import com.pasteleria.pedidos.application.port.OrderDetailRepositoryPort;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import com.pasteleria.productos.application.mapper.ProductDtoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Gestiona altas, cambios y retiros controlados del catálogo administrativo.
 */
@Service
public class ProductCommandService {

  private final ProductRepositoryPort productRepository;
  private final ProductCategoryRepositoryPort productCategoryRepository;
  private final OrderDetailRepositoryPort orderDetailRepository;
  private final QuotationDetailRepositoryPort quotationDetailRepository;
  private final AuditTrailService auditTrailService;
  private final ProductDtoMapper productDtoMapper;

  public ProductCommandService(
      ProductRepositoryPort productRepository,
      ProductCategoryRepositoryPort productCategoryRepository,
      OrderDetailRepositoryPort orderDetailRepository,
      QuotationDetailRepositoryPort quotationDetailRepository,
      AuditTrailService auditTrailService,
      ProductDtoMapper productDtoMapper
  ) {
    this.productRepository = productRepository;
    this.productCategoryRepository = productCategoryRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.quotationDetailRepository = quotationDetailRepository;
    this.auditTrailService = auditTrailService;
    this.productDtoMapper = productDtoMapper;
  }

  @Transactional
  public ProductSummary createProduct(CreateProductRequest request, HttpServletRequest httpRequest) {
    ProductEntity product = new ProductEntity();
    ProductCategoryEntity category = resolveCategory(request.categoryId());
    validateCodeUniqueness(request.code(), null);
    productDtoMapper.applyCreateRequest(product, category, request);

    OffsetDateTime now = OffsetDateTime.now();
    product.setCreatedAt(now);
    product.setUpdatedAt(now);

    ProductEntity saved = productRepository.save(product);
    ProductSummary summary = productDtoMapper.toSummary(saved);
    auditTrailService.recordChange(
        "PRODUCTO_CREADO",
        "PRODUCTOS",
        "producto",
        saved.getId().toString(),
        "CREAR_PRODUCTO",
        null,
        summary,
        "Alta del producto en el catalogo administrativo.",
        httpRequest
    );
    return summary;
  }

  @Transactional
  public ProductSummary updateProduct(Long productId, UpdateProductRequest request, HttpServletRequest httpRequest) {
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("El producto indicado no existe."));

    ProductCategoryEntity category = resolveCategory(request.categoryId());
    validateCodeUniqueness(request.code(), product.getId());
    ProductSummary previous = productDtoMapper.toSummary(product);
    productDtoMapper.applyUpdateRequest(product, category, request);
    product.setUpdatedAt(OffsetDateTime.now());

    ProductSummary current = productDtoMapper.toSummary(product);
    auditTrailService.recordChange(
        "PRODUCTO_ACTUALIZADO",
        "PRODUCTOS",
        "producto",
        product.getId().toString(),
        "ACTUALIZAR_PRODUCTO",
        previous,
        current,
        "Ajuste administrativo del catalogo.",
        httpRequest
    );
    return current;
  }

  @Transactional
  public void deleteProduct(Long productId, HttpServletRequest httpRequest) {
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("El producto indicado no existe."));

    if (orderDetailRepository.existsByProductId(productId) || quotationDetailRepository.existsByProductId(productId)) {
      throw new BusinessRuleException(
          "No se puede eliminar el producto porque ya tiene pedidos o cotizaciones asociados."
      );
    }

    auditTrailService.recordChange(
        "PRODUCTO_ELIMINADO",
        "PRODUCTOS",
        "producto",
        product.getId().toString(),
        "ELIMINAR_PRODUCTO",
        productDtoMapper.toSummary(product),
        null,
        "Retiro administrativo del catalogo.",
        httpRequest
    );
    productRepository.delete(product);
  }

  private ProductCategoryEntity resolveCategory(Long categoryId) {
    return productCategoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("La categoria indicada no existe."));
  }

  private void validateCodeUniqueness(String code, Long productId) {
    String normalizedCode = code.trim().toUpperCase(java.util.Locale.ROOT);
    if (productRepository.existsByCodeIgnoreCaseAndIdNot(normalizedCode, productId == null ? -1L : productId)) {
      throw new BusinessRuleException("Ya existe otro producto con el mismo codigo.");
    }
  }
}


