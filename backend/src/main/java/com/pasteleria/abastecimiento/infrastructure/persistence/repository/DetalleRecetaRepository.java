package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.DetalleRecetaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleRecetaRepository extends JpaRepository<DetalleRecetaEntity, Long>, DetalleRecetaRepositoryPort {

  List<DetalleRecetaEntity> findByRecetaId(Long recetaId);

  List<DetalleRecetaEntity> findByIngredienteId(Long ingredienteId);

  void deleteByRecetaId(Long recetaId);
}
