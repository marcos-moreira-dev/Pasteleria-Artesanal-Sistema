package com.pasteleria.clientes.infrastructure.persistence.repository;

import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<ClientEntity, Long>, ClientRepositoryPort {

  List<ClientEntity> findAllByOrderByCreatedAtDesc();

  Page<ClientEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  @Query("""
      select client
      from ClientEntity client
      where lower(client.fullName) like lower(concat('%', :query, '%'))
         or lower(coalesce(client.email, '')) like lower(concat('%', :query, '%'))
         or lower(coalesce(client.phone, '')) like lower(concat('%', :query, '%'))
      """)
  Page<ClientEntity> findBySearchTerm(@Param("query") String query, Pageable pageable);

  Optional<ClientEntity> findByEmailIgnoreCase(String email);
}



