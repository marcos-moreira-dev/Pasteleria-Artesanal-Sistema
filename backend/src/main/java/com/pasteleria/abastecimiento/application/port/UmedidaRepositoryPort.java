package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UmedidaRepositoryPort {

  List<UmedidaEntity> findAllByOrderByCreatedAtDesc();

  Page<UmedidaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<UmedidaEntity> findBySearchTerm(String query, Pageable pageable);

  Optional<UmedidaEntity> findById(Long id);

  UmedidaEntity save(UmedidaEntity entity);

  void delete(UmedidaEntity entity);

  long count();

  List<UmedidaEntity> findAll();

  Optional<UmedidaEntity> findByCode(String code);

  List<UmedidaEntity> findByActiveTrue();
}