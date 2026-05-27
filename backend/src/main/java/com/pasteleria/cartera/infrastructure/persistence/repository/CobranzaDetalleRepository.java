package com.pasteleria.cartera.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.cartera.infrastructure.persistence.entity.CobranzaDetalleEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobranzaDetalleRepository extends JpaRepository<CobranzaDetalleEntity, Long> {

  List<CobranzaDetalleEntity> findByCobranzaIdOrderByIdAsc(Long cobranzaId);
}
