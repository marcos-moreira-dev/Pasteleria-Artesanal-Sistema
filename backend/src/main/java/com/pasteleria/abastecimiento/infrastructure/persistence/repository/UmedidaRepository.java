package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.UmedidaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.UmedidaEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UmedidaRepository extends JpaRepository<UmedidaEntity, Long>, UmedidaRepositoryPort {

  List<UmedidaEntity> findAllByOrderByCreatedAtDesc();

  Page<UmedidaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  @Query("""
      SELECT u FROM UmedidaEntity u
      WHERE LOWER(u.code) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%'))
      """)
  Page<UmedidaEntity> findBySearchTerm(@Param("query") String query, Pageable pageable);

  Optional<UmedidaEntity> findByCode(String code);

  List<UmedidaEntity> findByActiveTrue();
}