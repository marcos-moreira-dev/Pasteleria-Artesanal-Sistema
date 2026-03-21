package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.RecetaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecetaRepository extends JpaRepository<RecetaEntity, Long>, RecetaRepositoryPort {

  Optional<RecetaEntity> findByProductoIdAndEsActivaTrue(Long productoId);

  List<RecetaEntity> findByProductoId(Long productoId);

  List<RecetaEntity> findByEsActivaTrue();

  List<RecetaEntity> findAllByOrderByCreatedAtDesc();

  @Query("SELECT r FROM RecetaEntity r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :term, '%')) ORDER BY r.createdAt DESC")
  List<RecetaEntity> findBySearchTerm(@Param("term") String term);

  @Query("SELECT r FROM RecetaEntity r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :term, '%')) ORDER BY r.createdAt DESC")
  Page<RecetaEntity> findBySearchTerm(@Param("term") String term, Pageable pageable);

  boolean existsByProductoIdAndEsActivaTrueAndIdNot(Long productoId, Long excludeId);
}
