package com.pasteleria.casosuso.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.casosuso.infrastructure.persistence.entity.PasoCasoUsoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasoCasoUsoRepository extends JpaRepository<PasoCasoUsoEntity, Long> {

  List<PasoCasoUsoEntity> findByCasoUsoIdOrderByNumeroAsc(Long casoUsoId);

  List<PasoCasoUsoEntity> findByCasoUsoIdInOrderByCasoUsoIdAscNumeroAsc(List<Long> casoUsoIds);
}
