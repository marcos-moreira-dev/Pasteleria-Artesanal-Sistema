package com.pasteleria.abastecimiento.infrastructure.persistence.repository;

import com.pasteleria.abastecimiento.application.port.ItemProveedorRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ItemProveedorEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemProveedorRepository extends JpaRepository<ItemProveedorEntity, Long>, ItemProveedorRepositoryPort {

  @Query("select ip from ItemProveedorEntity ip where ip.itemTipo = :itemTipo and ip.itemId = :itemId and ip.active = true")
  List<ItemProveedorEntity> findByItem(@Param("itemTipo") ItemProveedorEntity.ItemTipo itemTipo, @Param("itemId") Long itemId);

  @Query("select ip from ItemProveedorEntity ip where ip.itemTipo = :itemTipo and ip.itemId = :itemId and ip.active = true")
  Page<ItemProveedorEntity> findByItem(@Param("itemTipo") ItemProveedorEntity.ItemTipo itemTipo, @Param("itemId") Long itemId, Pageable pageable);

  @Query("select ip from ItemProveedorEntity ip where ip.proveedor.id = :proveedorId and ip.active = true")
  List<ItemProveedorEntity> findByProveedorId(@Param("proveedorId") Long proveedorId);

  @Query("select ip from ItemProveedorEntity ip where ip.proveedor.id = :proveedorId and ip.active = true")
  Page<ItemProveedorEntity> findByProveedorId(@Param("proveedorId") Long proveedorId, Pageable pageable);

  @Query("select ip from ItemProveedorEntity ip where ip.itemTipo = :itemTipo and ip.itemId = :itemId and ip.esPrincipal = true and ip.active = true")
  Optional<ItemProveedorEntity> findByItemAndEsPrincipalTrue(@Param("itemTipo") ItemProveedorEntity.ItemTipo itemTipo, @Param("itemId") Long itemId);
}