package com.pasteleria.produccion.application;

import java.util.List;
import java.util.Comparator;

import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.produccion.application.port.ProductionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.produccion.domain.model.ProductionPriority;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.application.mapper.ProductionDtoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductionQueryService {

  private final ProductionRepositoryPort productionRepository;
  private final ProductionDtoMapper productionDtoMapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public ProductionQueryService(
      ProductionRepositoryPort productionRepository,
      ProductionDtoMapper productionDtoMapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.productionRepository = productionRepository;
    this.productionDtoMapper = productionDtoMapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<ProductionSummary> listProductionQueue() {
    return productionRepository.findAll().stream()
        .sorted(buildProductionComparator())
        .map(productionDtoMapper::toSummary)
        .toList();
  }

  public PageResponseDto<ProductionSummary> listProductionQueuePage(int page, int size) {
    List<ProductionSummary> orderedItems = listProductionQueue();
    int normalizedPage = pageRequestFactory.normalizePage(page);
    int normalizedSize = pageRequestFactory.normalizeSize(size);
    int fromIndex = Math.min(normalizedPage * normalizedSize, orderedItems.size());
    int toIndex = Math.min(fromIndex + normalizedSize, orderedItems.size());

    return pageMapper.toPageResponseDto(
        orderedItems.subList(fromIndex, toIndex),
        normalizedPage,
        normalizedSize,
        orderedItems.size(),
        "estado-cola;prioridad;createdAt,asc"
    );
  }

  private Comparator<ProductionEntity> buildProductionComparator() {
    return Comparator
        .comparingInt((ProductionEntity production) -> production.getStatus() == ProductionStatus.FINALIZADO ? 1 : 0)
        .thenComparingInt(this::priorityRank)
        .thenComparingInt(this::stageRank)
        .thenComparing(ProductionEntity::getCreatedAt);
  }

  private int priorityRank(ProductionEntity production) {
    return production.getPriority() == ProductionPriority.URGENTE ? 0 : 1;
  }

  private int stageRank(ProductionEntity production) {
    return switch (production.getStatus()) {
      case PENDIENTE -> 0;
      case PREPARACION -> 1;
      case DECORACION -> 2;
      case EMPAQUE -> 3;
      case FINALIZADO -> 4;
    };
  }
}


