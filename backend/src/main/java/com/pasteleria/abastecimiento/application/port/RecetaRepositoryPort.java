package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecetaRepositoryPort {

  Optional<RecetaEntity> findById(Long id);

  Optional<RecetaEntity> findByProductoIdAndEsActivaTrue(Long productoId);

  List<RecetaEntity> findByProductoId(Long productoId);

  List<RecetaEntity> findByEsActivaTrue();

  List<RecetaEntity> findAllByOrderByCreatedAtDesc();

  List<RecetaEntity> findBySearchTerm(String term);

  Page<RecetaEntity> findAll(Pageable pageable);

  Page<RecetaEntity> findBySearchTerm(String term, Pageable pageable);

  RecetaEntity save(RecetaEntity receta);

  void delete(RecetaEntity receta);

  boolean existsByProductoIdAndEsActivaTrueAndIdNot(Long productoId, Long excludeId);
}
