package com.pasteleria.abastecimiento.application;

import java.util.List;
import java.util.Optional;

import com.pasteleria.abastecimiento.application.port.ItemProveedorRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ItemProveedorEntity;
import com.pasteleria.abastecimiento.application.mapper.ItemProveedorDtoMapper;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemProveedorQueryService {

  private final ItemProveedorRepositoryPort repository;
  private final ItemProveedorDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public ItemProveedorQueryService(
      ItemProveedorRepositoryPort repository,
      ItemProveedorDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<ItemProveedorSummary> listByItem(String itemTipo, Long itemId) {
    ItemProveedorEntity.ItemTipo tipo = ItemProveedorEntity.ItemTipo.valueOf(itemTipo);
    return repository.findByItem(tipo, itemId).stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<ItemProveedorSummary> listByItemPage(String itemTipo, Long itemId, int page, int size) {
    ItemProveedorEntity.ItemTipo tipo = ItemProveedorEntity.ItemTipo.valueOf(itemTipo);
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return pageMapper.toPageResponseDto(
        repository.findByItem(tipo, itemId, pageable)
            .map(mapper::toSummary)
    );
  }

  public List<ItemProveedorSummary> listByProveedor(Long proveedorId) {
    return repository.findByProveedorId(proveedorId).stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<ItemProveedorSummary> listByProveedorPage(Long proveedorId, int page, int size) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return pageMapper.toPageResponseDto(
        repository.findByProveedorId(proveedorId, pageable)
            .map(mapper::toSummary)
    );
  }

  public Optional<ItemProveedorSummary> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toSummary);
  }

  public ItemProveedorSummary getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Item-Proveedor no encontrado."));
  }
}