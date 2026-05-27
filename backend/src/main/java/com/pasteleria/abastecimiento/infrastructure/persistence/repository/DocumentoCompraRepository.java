package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoCompraEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoCompraRepository extends JpaRepository<DocumentoCompraEntity, Long> {

  Optional<DocumentoCompraEntity> findByOrdenCompraOrdenCompraId(Long ordenCompraId);

  boolean existsByOrdenCompraOrdenCompraId(Long ordenCompraId);

  boolean existsByNumeroDocumentoIgnoreCase(String numeroDocumento);
}
