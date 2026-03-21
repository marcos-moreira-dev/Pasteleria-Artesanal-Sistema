package com.pasteleria.abastecimiento.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * Entidad JPA que representa un Proveedor en el sistema.
 * 
 * <h2>DESCRIPCIÓN</h2>
 * Un proveedor es una entidad externa que suministra ingredientes e insumos
 * a la pastelería. Cada proveedor tiene un catálogo de artículos que ofrece
 * con precios específicos.
 * 
 * <h2>CAMPOS PRINCIPALES</h2>
 * <ul>
 *   <li><b>code</b>: Código único del proveedor (PROV-XXX)</li>
 *   <li><b>name</b>: Nombre comercial del proveedor</li>
 *   <li><b>phone/email</b>: Información de contacto</li>
 *   <li><b>active</b>: Estado del proveedor (soft delete)</li>
 * </ul>
 * 
 * <h2>REGLAS DE NEGOCIO</h2>
 * <ul>
 *   <li>El código debe ser único en el sistema</li>
 *   <li>Un proveedor inactivo no aparece en nuevas órdenes de compra</li>
 *   <li>La auditoría (createdAt, updatedAt) se mantiene automáticamente</li>
 * </ul>
 * 
 * <h2>RELACIONES</h2>
 * <ul>
 *   <li>One-to-Many con {@link ItemProveedorEntity} (catálogo)</li>
 *   <li>One-to-Many con OrdenCompra (historial)</li>
 * </ul>
 * 
 * <h2>OPTIMISTIC LOCKING</h2>
 * El campo {@code version} habilita el bloqueo optimista para evitar
 * actualizaciones concurrentes. Si dos usuarios modifican el mismo proveedor
 * simultáneamente, el segundo recibirá un error de versión obsoleta.
 * 
 * @see ItemProveedorEntity
 * @author Pastelería Development Team
 */
@Entity
@Table(name = "proveedor")
public class ProveedorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "proveedor_id")
  private Long id;

  @Column(name = "codigo", nullable = false, unique = true, length = 40)
  private String code;

  @Column(name = "nombre", nullable = false, length = 160)
  private String name;

  @Column(name = "telefono", length = 30)
  private String phone;

  @Column(name = "correo", length = 120)
  private String email;

  @Column(name = "direccion", length = 500)
  private String address;

  @Column(name = "observaciones", length = 1000)
  private String observations;

  @Column(name = "activo", nullable = false)
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}