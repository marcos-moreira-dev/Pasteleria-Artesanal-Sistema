package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngredienteRepositoryPort {

  List<IngredienteEntity> findAllByOrderByCreatedAtDesc();

  Page<IngredienteEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<IngredienteEntity> findBySearchTerm(String query, Pageable pageable);

  Optional<IngredienteEntity> findById(Long id);

  Optional<IngredienteEntity> findByIdForUpdate(Long id);

  Optional<IngredienteEntity> findByCode(String code);

  List<IngredienteEntity> findByActiveTrue();

  IngredienteEntity save(IngredienteEntity entity);

  void delete(IngredienteEntity entity);

  long count();

  List<IngredienteEntity> findAll();
}