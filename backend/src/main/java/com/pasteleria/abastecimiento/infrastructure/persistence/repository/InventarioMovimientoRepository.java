package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.InventarioMovimientoRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimientoEntity, Long>, InventarioMovimientoRepositoryPort {

  @Query("SELECT m FROM InventarioMovimientoEntity m WHERE m.itemTipo = :itemTipo AND m.itemId = :itemId ORDER BY m.fechaMovimiento DESC")
  List<InventarioMovimientoEntity> findByItemOrderByFechaMovimientoDesc(
      @Param("itemTipo") String itemTipo,
      @Param("itemId") Long itemId
  );

  @Query("SELECT m FROM InventarioMovimientoEntity m WHERE m.itemTipo = :itemTipo AND m.itemId = :itemId")
  Page<InventarioMovimientoEntity> findByItemOrderByFechaMovimientoDesc(
      @Param("itemTipo") String itemTipo,
      @Param("itemId") Long itemId,
      Pageable pageable
  );

  @Query("""
      SELECT m
      FROM InventarioMovimientoEntity m
      WHERE (:itemTipo IS NULL OR m.itemTipo = :itemTipo)
        AND (:itemId IS NULL OR m.itemId = :itemId)
        AND (:tipoMovimiento IS NULL OR m.tipoMovimiento = :tipoMovimiento)
        AND (:fechaDesde IS NULL OR m.fechaMovimiento >= :fechaDesde)
        AND (:fechaHasta IS NULL OR m.fechaMovimiento <= :fechaHasta)
      ORDER BY m.fechaMovimiento DESC
      """)
  List<InventarioMovimientoEntity> findByFilters(
      @Param("itemTipo") String itemTipo,
      @Param("itemId") Long itemId,
      @Param("tipoMovimiento") String tipoMovimiento,
      @Param("fechaDesde") OffsetDateTime fechaDesde,
      @Param("fechaHasta") OffsetDateTime fechaHasta
  );

  List<InventarioMovimientoEntity> findTop8ByOrderByFechaMovimientoDesc();

  long countByTipoMovimientoStartingWithAndFechaMovimientoBetween(
      String tipoMovimientoPrefix,
      OffsetDateTime fechaDesde,
      OffsetDateTime fechaHasta
  );

  @Query("SELECT m FROM InventarioMovimientoEntity m WHERE m.referenciaTipo = :referenciaTipo AND m.referenciaId = :referenciaId ORDER BY m.fechaMovimiento DESC")
  List<InventarioMovimientoEntity> findByReferencia(
      @Param("referenciaTipo") String referenciaTipo,
      @Param("referenciaId") String referenciaId
  );

  @Query("SELECT m FROM InventarioMovimientoEntity m WHERE m.referenciaTipo = :referenciaTipo AND m.referenciaId = :referenciaId")
  Page<InventarioMovimientoEntity> findByReferencia(
      @Param("referenciaTipo") String referenciaTipo,
      @Param("referenciaId") String referenciaId,
      Pageable pageable
  );
}
