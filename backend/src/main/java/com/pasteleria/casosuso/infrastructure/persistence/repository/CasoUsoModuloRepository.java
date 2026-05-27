package com.pasteleria.casosuso.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.casosuso.infrastructure.persistence.entity.CasoUsoModuloEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CasoUsoModuloRepository extends JpaRepository<CasoUsoModuloEntity, Long> {

  List<CasoUsoModuloEntity> findByActivoTrueOrderByOrdenVisualAscCodigoAsc();
}
