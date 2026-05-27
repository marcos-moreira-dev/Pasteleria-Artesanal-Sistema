package com.pasteleria.cuentaspagar.infrastructure.persistence.repository;

import java.util.List;

import com.pasteleria.cuentaspagar.infrastructure.persistence.entity.PagoProveedorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoProveedorRepository extends JpaRepository<PagoProveedorEntity, Long> {

  List<PagoProveedorEntity> findTop80ByOrderByFechaPagoDesc();
}
