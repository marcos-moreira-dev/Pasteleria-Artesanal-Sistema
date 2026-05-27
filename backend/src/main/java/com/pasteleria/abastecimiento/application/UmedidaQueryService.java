package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.application.port.UmedidaRepositoryPort;
import com.pasteleria.abastecimiento.application.mapper.UmedidaDtoMapper;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UmedidaQueryService {

  private final UmedidaRepositoryPort repository;
  private final UmedidaDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public UmedidaQueryService(
      UmedidaRepositoryPort repository,
      UmedidaDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<UmedidaSummary> listUmedidas() {
    return repository.findAllByOrderByCreatedAtDesc().stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<UmedidaSummary> listUmedidasPage(int page, int size, String query) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

    return pageMapper.toPageResponseDto(
        (normalizedQuery.isBlank()
            ? repository.findAllByOrderByCreatedAtDesc(pageable)
            : repository.findBySearchTerm(normalizedQuery, pageable))
            .map(mapper::toSummary)
    );
  }

  public Optional<UmedidaSummary> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toSummary);
  }

  public UmedidaSummary getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada."));
  }

  public List<UmedidaSummary> listActiveUmedidas() {
    return repository.findByActiveTrue().stream()
        .map(mapper::toSummary)
        .toList();
  }
}