package com.pasteleria.fiscal.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.pasteleria.fiscal.infrastructure.persistence.entity.DocumentoFiscalEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoFiscalRepository extends JpaRepository<DocumentoFiscalEntity, Long> {

  List<DocumentoFiscalEntity> findTop80ByOrderByFechaEmisionDescIdDesc();

  Optional<DocumentoFiscalEntity> findByDocumentoCobrarId(Long documentoCobrarId);

  Optional<DocumentoFiscalEntity> findByDocumentoCompraId(Long documentoCompraId);

  boolean existsByDocumentoCobrarId(Long documentoCobrarId);

  boolean existsByDocumentoCompraId(Long documentoCompraId);

  boolean existsByCodigo(String codigo);

  boolean existsByNumeroComprobante(String numeroComprobante);
}
