package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompraEntity, Long> {

  Page<OrdenCompraEntity> findByEstado(String estado, Pageable pageable);

  Page<OrdenCompraEntity> findByCodigoContainingIgnoreCaseOrProveedorNameContainingIgnoreCase(
      String codigo, String proveedorName, Pageable pageable);

  List<OrdenCompraEntity> findByProveedorId(Long proveedorId);
}
