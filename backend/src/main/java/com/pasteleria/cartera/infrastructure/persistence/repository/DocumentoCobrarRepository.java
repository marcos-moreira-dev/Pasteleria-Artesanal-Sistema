package com.pasteleria.cartera.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.pasteleria.cartera.infrastructure.persistence.entity.DocumentoCobrarEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

@Repository
public interface DocumentoCobrarRepository extends JpaRepository<DocumentoCobrarEntity, Long> {

  List<DocumentoCobrarEntity> findTop80ByOrderByCreatedAtDesc();

  List<DocumentoCobrarEntity> findByEstadoOrderByFechaVencimientoAsc(String estado);

  Optional<DocumentoCobrarEntity> findByPedidoId(Long pedidoId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select d from DocumentoCobrarEntity d where d.id = :id")
  Optional<DocumentoCobrarEntity> findByIdForUpdate(@Param("id") Long id);
}
