package com.pasteleria.cartera.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.cartera.infrastructure.persistence.entity.CobranzaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobranzaRepository extends JpaRepository<CobranzaEntity, Long> {

  List<CobranzaEntity> findTop80ByOrderByFechaCobranzaDesc();
}
