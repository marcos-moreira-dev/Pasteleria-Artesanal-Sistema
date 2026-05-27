package com.pasteleria.contabilidad.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.pasteleria.contabilidad.infrastructure.persistence.entity.AsientoContableEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsientoContableRepository extends JpaRepository<AsientoContableEntity, Long> {
  List<AsientoContableEntity> findTop80ByOrderByFechaAsientoDescIdDesc();
  Optional<AsientoContableEntity> findByCodigo(String codigo);
  Optional<AsientoContableEntity> findByOrigenTipoAndOrigenId(String origenTipo, String origenId);
  boolean existsByCodigo(String codigo);
  boolean existsByOrigenTipoAndOrigenId(String origenTipo, String origenId);
}
