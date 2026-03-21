package com.pasteleria.abastecimiento.application;

import java.util.List;
import java.util.Optional;

import com.pasteleria.abastecimiento.application.mapper.InventarioMovimientoDtoMapper;
import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InventarioMovimientoRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventarioMovimientoQueryService {

  private static final String TIPO_INGREDIENTE = "INGREDIENTE";

  private final InventarioMovimientoRepositoryPort movimientoRepository;
  private final IngredienteRepositoryPort ingredienteRepository;
  private final InsumoRepositoryPort insumoRepository;
  private final InventarioMovimientoDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public InventarioMovimientoQueryService(
      InventarioMovimientoRepositoryPort movimientoRepository,
      IngredienteRepositoryPort ingredienteRepository,
      InsumoRepositoryPort insumoRepository,
      InventarioMovimientoDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.movimientoRepository = movimientoRepository;
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<InventarioMovimientoSummary> listByItem(String itemTipo, Long itemId) {
    List<InventarioMovimientoEntity> movimientos = movimientoRepository.findByItemOrderByFechaMovimientoDesc(itemTipo, itemId);
    return movimientos.stream()
        .map(m -> {
          String itemNombre = getItemNombre(m.getItemTipo(), m.getItemId());
          return mapper.toSummary(m, itemNombre);
        })
        .toList();
  }

  public PageResponseDto<InventarioMovimientoSummary> listByItemPage(String itemTipo, Long itemId, int page, int size) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "fechaMovimiento"));
    return pageMapper.toPageResponseDto(
        movimientoRepository.findByItemOrderByFechaMovimientoDesc(itemTipo, itemId, pageable)
            .map(m -> {
              String itemNombre = getItemNombre(m.getItemTipo(), m.getItemId());
              return mapper.toSummary(m, itemNombre);
            })
    );
  }

  public List<InventarioMovimientoSummary> listByReferencia(String referenciaTipo, String referenciaId) {
    List<InventarioMovimientoEntity> movimientos = movimientoRepository.findByReferencia(referenciaTipo, referenciaId);
    return movimientos.stream()
        .map(m -> {
          String itemNombre = getItemNombre(m.getItemTipo(), m.getItemId());
          return mapper.toSummary(m, itemNombre);
        })
        .toList();
  }

  public PageResponseDto<InventarioMovimientoSummary> listByReferenciaPage(String referenciaTipo, String referenciaId, int page, int size) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "fechaMovimiento"));
    return pageMapper.toPageResponseDto(
        movimientoRepository.findByReferencia(referenciaTipo, referenciaId, pageable)
            .map(m -> {
              String itemNombre = getItemNombre(m.getItemTipo(), m.getItemId());
              return mapper.toSummary(m, itemNombre);
            })
    );
  }

  public Optional<InventarioMovimientoSummary> findById(Long id) {
    return movimientoRepository.findById(id)
        .map(m -> {
          String itemNombre = getItemNombre(m.getItemTipo(), m.getItemId());
          return mapper.toSummary(m, itemNombre);
        });
  }

  public InventarioMovimientoSummary getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Movimiento de inventario no encontrado."));
  }

  private String getItemNombre(String itemTipo, Long itemId) {
    if (TIPO_INGREDIENTE.equals(itemTipo)) {
      return ingredienteRepository.findById(itemId)
          .map(IngredienteEntity::getName)
          .orElse("Desconocido");
    } else {
      return insumoRepository.findById(itemId)
          .map(InsumoEntity::getName)
          .orElse("Desconocido");
    }
  }
}
