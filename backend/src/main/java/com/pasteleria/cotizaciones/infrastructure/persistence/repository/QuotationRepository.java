package com.pasteleria.cotizaciones.infrastructure.persistence.repository;

import com.pasteleria.cotizaciones.application.port.QuotationRepositoryPort;
import com.pasteleria.cotizaciones.domain.model.QuotationStatus;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<QuotationEntity, Long>, QuotationRepositoryPort {

  List<QuotationEntity> findAllByOrderByCreatedAtDesc();

  Page<QuotationEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Optional<QuotationEntity> findByIdAndStatus(Long id, QuotationStatus status);

  boolean existsByClientId(Long clientId);
}



