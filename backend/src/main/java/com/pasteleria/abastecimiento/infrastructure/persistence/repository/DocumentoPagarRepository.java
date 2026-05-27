package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoPagarEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

@Repository
public interface DocumentoPagarRepository extends JpaRepository<DocumentoPagarEntity, Long> {

  Optional<DocumentoPagarEntity> findByDocumentoCompraDocumentoCompraId(Long documentoCompraId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select d from DocumentoPagarEntity d where d.documentoPagarId = :id")
  Optional<DocumentoPagarEntity> findByIdForUpdate(@Param("id") Long id);

  List<DocumentoPagarEntity> findByEstadoOrderByCreatedAtDesc(String estado);

  List<DocumentoPagarEntity> findTop50ByOrderByCreatedAtDesc();
}
