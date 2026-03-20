package com.pasteleria.cotizaciones.infrastructure.persistence.repository;

import com.pasteleria.cotizaciones.application.port.QuotationDetailRepositoryPort;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetailEntity, Long>, QuotationDetailRepositoryPort {

  boolean existsByProductId(Long productId);
}



