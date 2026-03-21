package com.pasteleria.abastecimiento.application.port;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ItemProveedorEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemProveedorRepositoryPort {

  List<ItemProveedorEntity> findAll();

  Optional<ItemProveedorEntity> findById(Long id);

  List<ItemProveedorEntity> findByItem(ItemProveedorEntity.ItemTipo itemTipo, Long itemId);

  Page<ItemProveedorEntity> findByItem(ItemProveedorEntity.ItemTipo itemTipo, Long itemId, Pageable pageable);

  List<ItemProveedorEntity> findByProveedorId(Long proveedorId);

  Page<ItemProveedorEntity> findByProveedorId(Long proveedorId, Pageable pageable);

  ItemProveedorEntity save(ItemProveedorEntity entity);

  void delete(ItemProveedorEntity entity);

  void deleteById(Long id);

  long count();

  Optional<ItemProveedorEntity> findByItemAndEsPrincipalTrue(ItemProveedorEntity.ItemTipo itemTipo, Long itemId);
}