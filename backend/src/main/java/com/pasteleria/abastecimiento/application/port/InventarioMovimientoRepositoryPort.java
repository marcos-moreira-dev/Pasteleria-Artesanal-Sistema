package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventarioMovimientoRepositoryPort {

  List<InventarioMovimientoEntity> findByItemOrderByFechaMovimientoDesc(String itemTipo, Long itemId);

  Page<InventarioMovimientoEntity> findByItemOrderByFechaMovimientoDesc(String itemTipo, Long itemId, Pageable pageable);

  List<InventarioMovimientoEntity> findByFilters(
      String itemTipo,
      Long itemId,
      String tipoMovimiento,
      OffsetDateTime fechaDesde,
      OffsetDateTime fechaHasta
  );

  List<InventarioMovimientoEntity> findByReferencia(String referenciaTipo, String referenciaId);

  Page<InventarioMovimientoEntity> findByReferencia(String referenciaTipo, String referenciaId, Pageable pageable);

  Optional<InventarioMovimientoEntity> findById(Long id);

  InventarioMovimientoEntity save(InventarioMovimientoEntity movimiento);

  long count();

  List<InventarioMovimientoEntity> findAll();
}
