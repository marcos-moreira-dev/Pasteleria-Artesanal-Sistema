package com.pasteleria.caja.infrastructure.persistence.repository;

import java.util.Optional;

import com.pasteleria.caja.infrastructure.persistence.entity.ArqueoCajaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArqueoCajaRepository extends JpaRepository<ArqueoCajaEntity, Long> {

  Optional<ArqueoCajaEntity> findFirstByTurnoIdAndEstadoOrderByFechaArqueoDesc(Long turnoId, String estado);
}
