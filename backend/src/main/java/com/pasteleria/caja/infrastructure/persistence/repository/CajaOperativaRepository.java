package com.pasteleria.caja.infrastructure.persistence.repository;

import java.util.Optional;

import com.pasteleria.caja.infrastructure.persistence.entity.CajaOperativaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface CajaOperativaRepository extends JpaRepository<CajaOperativaEntity, Long> {

  Optional<CajaOperativaEntity> findByCodigoAndActivaTrue(String codigo);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select c from CajaOperativaEntity c where c.codigo = :codigo and c.activa = true")
  Optional<CajaOperativaEntity> findByCodigoAndActivaTrueForUpdate(@Param("codigo") String codigo);
}
