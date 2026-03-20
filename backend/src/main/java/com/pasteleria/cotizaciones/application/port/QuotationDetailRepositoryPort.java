package com.pasteleria.cotizaciones.application.port;

public interface QuotationDetailRepositoryPort {

  boolean existsByProductId(Long productId);
}
