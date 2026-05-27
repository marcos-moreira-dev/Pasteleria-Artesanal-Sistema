package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsumoRepositoryPort {

  List<InsumoEntity> findAllByOrderByCreatedAtDesc();

  Page<InsumoEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<InsumoEntity> findBySearchTerm(String query, Pageable pageable);

  Optional<InsumoEntity> findById(Long id);

  Optional<InsumoEntity> findByIdForUpdate(Long id);

  Optional<InsumoEntity> findByCode(String code);

  List<InsumoEntity> findByActiveTrue();

  InsumoEntity save(InsumoEntity entity);

  void delete(InsumoEntity entity);

  long count();

  List<InsumoEntity> findAll();
}