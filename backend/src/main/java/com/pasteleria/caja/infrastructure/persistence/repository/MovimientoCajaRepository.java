package com.pasteleria.caja.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.caja.infrastructure.persistence.entity.MovimientoCajaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCajaEntity, Long> {

  List<MovimientoCajaEntity> findByTurnoIdOrderByFechaMovimientoDesc(Long turnoId);

  List<MovimientoCajaEntity> findByTurnoIdAndEstadoOrderByFechaMovimientoAsc(Long turnoId, String estado);
}
