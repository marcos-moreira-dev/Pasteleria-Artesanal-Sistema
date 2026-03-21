package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;

import java.util.List;

public interface DetalleRecetaRepositoryPort {

  List<DetalleRecetaEntity> findByRecetaId(Long recetaId);

  List<DetalleRecetaEntity> findByIngredienteId(Long ingredienteId);

  DetalleRecetaEntity save(DetalleRecetaEntity detalle);

  void deleteByRecetaId(Long recetaId);
}
