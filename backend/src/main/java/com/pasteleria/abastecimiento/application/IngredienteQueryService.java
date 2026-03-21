package com.pasteleria.abastecimiento.application;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.mapper.IngredienteDtoMapper;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IngredienteQueryService {

  private final IngredienteRepositoryPort repository;
  private final IngredienteDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public IngredienteQueryService(
      IngredienteRepositoryPort repository,
      IngredienteDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<IngredienteSummary> listIngredientes() {
    return repository.findAllByOrderByCreatedAtDesc().stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<IngredienteSummary> listIngredientesPage(int page, int size, String query) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

    return pageMapper.toPageResponseDto(
        (normalizedQuery.isBlank()
            ? repository.findAllByOrderByCreatedAtDesc(pageable)
            : repository.findBySearchTerm(normalizedQuery, pageable))
            .map(mapper::toSummary)
    );
  }

  public Optional<IngredienteSummary> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toSummary);
  }

  public IngredienteSummary getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado."));
  }

  public List<IngredienteSummary> listActive() {
    return repository.findByActiveTrue().stream()
        .map(mapper::toSummary)
        .toList();
  }
}