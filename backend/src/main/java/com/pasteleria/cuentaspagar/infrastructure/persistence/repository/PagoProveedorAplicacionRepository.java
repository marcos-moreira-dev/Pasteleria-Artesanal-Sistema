package com.pasteleria.cuentaspagar.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.cuentaspagar.infrastructure.persistence.entity.PagoProveedorAplicacionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoProveedorAplicacionRepository extends JpaRepository<PagoProveedorAplicacionEntity, Long> {

  List<PagoProveedorAplicacionEntity> findByPagoProveedorIdOrderByIdAsc(Long pagoProveedorId);
}
