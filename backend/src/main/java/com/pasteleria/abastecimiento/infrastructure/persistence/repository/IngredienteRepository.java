package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface IngredienteRepository extends JpaRepository<IngredienteEntity, Long>, IngredienteRepositoryPort {

  List<IngredienteEntity> findAllByOrderByCreatedAtDesc();

  Page<IngredienteEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  @Query("""
      select i
      from IngredienteEntity i
      where lower(i.name) like lower(CONCAT('%', :query, '%'))
         or lower(i.code) like lower(CONCAT('%', :query, '%'))
         or lower(i.description) like lower(CONCAT('%', :query, '%'))
      """)
  Page<IngredienteEntity> findBySearchTerm(@Param("query") String query, Pageable pageable);


  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select i from IngredienteEntity i where i.id = :id")
  Optional<IngredienteEntity> findByIdForUpdate(@Param("id") Long id);

  Optional<IngredienteEntity> findByCode(String code);

  List<IngredienteEntity> findByActiveTrue();
}