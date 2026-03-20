package com.pasteleria.clientes.application.port;

import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientRepositoryPort {

  List<ClientEntity> findAllByOrderByCreatedAtDesc();

  Page<ClientEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<ClientEntity> findBySearchTerm(String query, Pageable pageable);

  Optional<ClientEntity> findByEmailIgnoreCase(String email);

  Optional<ClientEntity> findById(Long clientId);

  ClientEntity save(ClientEntity client);

  void delete(ClientEntity client);

  long count();

  List<ClientEntity> findAll();
}
