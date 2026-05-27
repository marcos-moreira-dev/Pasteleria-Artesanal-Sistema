package com.pasteleria.caja.infrastructure.persistence.repository;

import java.util.Optional;

import com.pasteleria.caja.infrastructure.persistence.entity.TurnoCajaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface TurnoCajaRepository extends JpaRepository<TurnoCajaEntity, Long> {

  Optional<TurnoCajaEntity> findFirstByCajaCodigoAndEstadoOrderByFechaAperturaDesc(String cajaCodigo, String estado);

  boolean existsByCajaIdAndEstado(Long cajaId, String estado);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select t from TurnoCajaEntity t where t.id = :id")
  Optional<TurnoCajaEntity> findByIdForUpdate(@Param("id") Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      select t
      from TurnoCajaEntity t
      where t.caja.codigo = :cajaCodigo
        and t.estado = :estado
      order by t.fechaApertura desc
      """)
  Optional<TurnoCajaEntity> findFirstByCajaCodigoAndEstadoForUpdate(
      @Param("cajaCodigo") String cajaCodigo,
      @Param("estado") String estado
  );
}
