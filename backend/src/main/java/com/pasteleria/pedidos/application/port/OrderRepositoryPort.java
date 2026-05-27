package com.pasteleria.pedidos.application.port;

import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryPort {

  List<OrderEntity> findAllByOrderByCreatedAtDesc();

  Page<OrderEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  boolean existsByClientId(Long clientId);

  Optional<OrderEntity> findById(Long orderId);

  OrderEntity save(OrderEntity order);

  void delete(OrderEntity order);

  List<OrderEntity> findAll();
}
