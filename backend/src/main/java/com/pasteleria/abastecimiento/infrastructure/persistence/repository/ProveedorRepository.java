package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.ProveedorRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long>, ProveedorRepositoryPort {

  List<ProveedorEntity> findAllByOrderByCreatedAtDesc();

  Page<ProveedorEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  @Query("""
      select p
      from ProveedorEntity p
      where lower(p.name) like lower(concat('%', :query, '%'))
         or lower(p.code) like lower(concat('%', :query, '%'))
         or lower(p.email) like lower(concat('%', :query, '%'))
      """)
  Page<ProveedorEntity> findBySearchTerm(@Param("query") String query, Pageable pageable);

  Optional<ProveedorEntity> findByCode(String code);

  List<ProveedorEntity> findByActiveTrue();
}