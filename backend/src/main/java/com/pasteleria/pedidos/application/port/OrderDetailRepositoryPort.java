package com.pasteleria.pedidos.application.port;

public interface OrderDetailRepositoryPort {

  boolean existsByProductId(Long productId);
}
