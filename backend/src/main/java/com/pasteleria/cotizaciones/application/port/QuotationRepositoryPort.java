package com.pasteleria.cotizaciones.application.port;

import com.pasteleria.cotizaciones.domain.model.QuotationStatus;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuotationRepositoryPort {

  List<QuotationEntity> findAllByOrderByCreatedAtDesc();

  Page<QuotationEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Optional<QuotationEntity> findByIdAndStatus(Long quotationId, QuotationStatus status);

  boolean existsByClientId(Long clientId);

  Optional<QuotationEntity> findById(Long quotationId);

  QuotationEntity save(QuotationEntity quotation);
}
