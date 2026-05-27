package com.pasteleria.abastecimiento.application;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.application.mapper.InsumoDtoMapper;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InsumoQueryService {

  private final InsumoRepositoryPort repository;
  private final InsumoDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public InsumoQueryService(
      InsumoRepositoryPort repository,
      InsumoDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<InsumoSummary> listInsumos() {
    return repository.findAllByOrderByCreatedAtDesc().stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<InsumoSummary> listInsumosPage(int page, int size, String query) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

    return pageMapper.toPageResponseDto(
        (normalizedQuery.isBlank()
            ? repository.findAllByOrderByCreatedAtDesc(pageable)
            : repository.findBySearchTerm(normalizedQuery, pageable))
            .map(mapper::toSummary)
    );
  }

  public Optional<InsumoSummary> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toSummary);
  }

  public InsumoSummary getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado."));
  }

  public List<InsumoSummary> listActive() {
    return repository.findByActiveTrue().stream()
        .map(mapper::toSummary)
        .toList();
  }
}