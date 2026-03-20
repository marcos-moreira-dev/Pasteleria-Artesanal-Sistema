package com.pasteleria.pedidos.infrastructure.persistence.repository;

import com.pasteleria.pedidos.application.port.OrderDetailRepositoryPort;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long>, OrderDetailRepositoryPort {

  boolean existsByProductId(Long productId);
}



