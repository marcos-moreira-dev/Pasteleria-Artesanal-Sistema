package com.pasteleria.contabilidad.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.pasteleria.contabilidad.infrastructure.persistence.entity.TipoDiarioContableEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDiarioContableRepository extends JpaRepository<TipoDiarioContableEntity, Long> {
  List<TipoDiarioContableEntity> findByActivoTrueOrderByCodigoAsc();
  Optional<TipoDiarioContableEntity> findByCodigo(String codigo);
}
