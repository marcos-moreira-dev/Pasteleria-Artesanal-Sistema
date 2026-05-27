package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProveedorRepositoryPort {

  List<ProveedorEntity> findAllByOrderByCreatedAtDesc();

  Page<ProveedorEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<ProveedorEntity> findBySearchTerm(String query, Pageable pageable);

  Optional<ProveedorEntity> findById(Long id);

  Optional<ProveedorEntity> findByCode(String code);

  List<ProveedorEntity> findByActiveTrue();

  ProveedorEntity save(ProveedorEntity entity);

  void delete(ProveedorEntity entity);

  long count();

  List<ProveedorEntity> findAll();
}