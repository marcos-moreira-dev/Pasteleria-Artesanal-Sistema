package com.pasteleria.pedidos.infrastructure.persistence.repository;

import com.pasteleria.pedidos.application.port.OrderRepositoryPort;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderRepositoryPort {

  List<OrderEntity> findAllByOrderByCreatedAtDesc();

  Page<OrderEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  boolean existsByClientId(Long clientId);
}



