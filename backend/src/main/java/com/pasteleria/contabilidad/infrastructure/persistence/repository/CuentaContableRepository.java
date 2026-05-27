package com.pasteleria.contabilidad.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.pasteleria.contabilidad.infrastructure.persistence.entity.CuentaContableEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaContableRepository extends JpaRepository<CuentaContableEntity, Long> {
  List<CuentaContableEntity> findByActivaTrueOrderByCodigoAsc();
  Optional<CuentaContableEntity> findByCodigo(String codigo);
}
