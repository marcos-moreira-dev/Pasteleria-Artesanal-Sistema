package com.pasteleria.abastecimiento.application;

import java.util.List;

import com.pasteleria.abastecimiento.application.mapper.RecetaDtoMapper;
import com.pasteleria.abastecimiento.application.port.DetalleRecetaRepositoryPort;
import com.pasteleria.abastecimiento.application.port.RecetaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RecetaQueryService {

  private final RecetaRepositoryPort recetaRepository;
  private final DetalleRecetaRepositoryPort detalleRecetaRepository;
  private final RecetaDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public RecetaQueryService(
      RecetaRepositoryPort recetaRepository,
      DetalleRecetaRepositoryPort detalleRecetaRepository,
      RecetaDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.recetaRepository = recetaRepository;
    this.detalleRecetaRepository = detalleRecetaRepository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<RecetaSummary> listRecetas() {
    return recetaRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<RecetaSummary> listRecetasPage(int page, int size, String query) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    String normalizedQuery = query == null ? "" : query.trim().toLowerCase();

    return pageMapper.toPageResponseDto(
        (normalizedQuery.isBlank()
            ? recetaRepository.findAll(pageable)
            : recetaRepository.findBySearchTerm(normalizedQuery, pageable))
            .map(mapper::toSummary)
    );
  }

  public RecetaDetailSummary getRecetaDetail(Long id) {
    RecetaEntity entity = recetaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada."));
    List<DetalleRecetaEntity> detalles = detalleRecetaRepository.findByRecetaId(id);
    return mapper.toDetailSummary(entity, detalles);
  }

  public List<RecetaSummary> getRecetasByProducto(Long productoId) {
    return recetaRepository.findByProductoId(productoId).stream()
        .map(mapper::toSummary)
        .toList();
  }
}
