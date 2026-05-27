package com.pasteleria.casosuso.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.pasteleria.casosuso.infrastructure.persistence.entity.CasoUsoOperativoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CasoUsoOperativoRepository extends JpaRepository<CasoUsoOperativoEntity, Long> {

  List<CasoUsoOperativoEntity> findByActivoTrueOrderByModuloAscOrdenVisualAscCodigoAsc();

  List<CasoUsoOperativoEntity> findByModuloAndActivoTrueOrderByOrdenVisualAscCodigoAsc(String modulo);

  Optional<CasoUsoOperativoEntity> findByCodigoAndActivoTrue(String codigo);
}
