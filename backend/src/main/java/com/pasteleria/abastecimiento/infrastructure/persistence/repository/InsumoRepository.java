package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface InsumoRepository extends JpaRepository<InsumoEntity, Long>, InsumoRepositoryPort {

  List<InsumoEntity> findAllByOrderByCreatedAtDesc();

  Page<InsumoEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

  @Query("""
      select i
      from InsumoEntity i
      where lower(i.name) like lower(concat('%', :query, '%'))
         or lower(i.code) like lower(concat('%', :query, '%'))
         or lower(i.description) like lower(concat('%', :query, '%'))
      """)
  Page<InsumoEntity> findBySearchTerm(@Param("query") String query, Pageable pageable);


  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select i from InsumoEntity i where i.id = :id")
  Optional<InsumoEntity> findByIdForUpdate(@Param("id") Long id);

  Optional<InsumoEntity> findByCode(String code);

  List<InsumoEntity> findByActiveTrue();
}