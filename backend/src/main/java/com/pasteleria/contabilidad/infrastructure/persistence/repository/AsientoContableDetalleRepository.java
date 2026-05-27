package com.pasteleria.contabilidad.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.contabilidad.infrastructure.persistence.entity.AsientoContableDetalleEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsientoContableDetalleRepository extends JpaRepository<AsientoContableDetalleEntity, Long> {
  List<AsientoContableDetalleEntity> findByAsientoIdOrderByIdAsc(Long asientoId);
}
