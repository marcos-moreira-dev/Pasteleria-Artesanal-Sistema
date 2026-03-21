package com.pasteleria.abastecimiento.application;

import java.time.OffsetDateTime;

import com.pasteleria.abastecimiento.application.port.ProveedorRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;
import com.pasteleria.abastecimiento.application.mapper.ProveedorDtoMapper;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Servicio de Aplicación para operaciones de escritura (comandos) de Proveedores.
 * 
 * <h2>RESPONSABILIDAD</h2>
 * Este servicio implementa todos los casos de uso que modifican el estado
 * de los proveedores en el sistema. Sigue el patrón CQRS (Command Query Responsibility Segregation)
 * donde las operaciones de escritura están separadas de las de lectura.
 * 
 * <h2>OPERACIONES SOPORTADAS</h2>
 * <ul>
 *   <li>{@link #createProveedor}: Crear un nuevo proveedor</li>
 *   <li>{@link #updateProveedor}: Actualizar datos de un proveedor existente</li>
 *   <li>{@link #deleteProveedor}: Eliminar un proveedor (soft delete)</li>
 * </ul>
 * 
 * <h2>CARACTERÍSTICAS</h2>
 * 
 * <h3>Transaccionalidad</h3>
 * Todos los métodos están anotados con {@code @Transactional}, garantizando
 * que las operaciones sean atómicas. Si ocurre un error, se hace rollback
 * automático de todos los cambios.
 * 
 * <h3>Auditoría</h3>
 * Cada operación registra un trail de auditoría usando {@link AuditTrailService},
 * almacenando: quién hizo el cambio, qué cambió, valores antes/después,
 * timestamp, e IP del solicitante.
 * 
 * <h3>Mapeo</h3>
 * Usa {@link ProveedorDtoMapper} para convertir entre:
 * <ul>
 *   <li>DTOs de Request (CreateProveedorRequest, UpdateProveedorRequest)</li>
 *   <li>Entidades JPA (ProveedorEntity)</li>
 *   <li>DTOs de Response (ProveedorSummary)</li>
 * </ul>
 * 
 * <h3>Manejo de Errores</h3>
 * <ul>
 *   <li>{@link ResourceNotFoundException}: Cuando el proveedor no existe</li>
 *   <li>DataIntegrityViolationException: Violación de constraints (ej: código duplicado)</li>
 * </ul>
 * 
 * <h2>FLUJO DE CREACIÓN</h2>
 * <pre>
 * 1. Validar request (hecho por @Valid en controller)
 * 2. Crear nueva instancia de ProveedorEntity
 * 3. Aplicar datos del request usando mapper
 * 4. Guardar en base de datos
 * 5. Registrar auditoría
 * 6. Retornar DTO de respuesta
 * </pre>
 * 
 * <h2>FLUJO DE ACTUALIZACIÓN</h2>
 * <pre>
 * 1. Buscar proveedor por ID (lanza excepción si no existe)
 * 2. Capturar estado anterior para auditoría
 * 3. Aplicar cambios usando mapper
 * 4. Guardar entidad actualizada
 * 5. Registrar auditoría con before/after
 * 6. Retornar DTO actualizado
 * </pre>
 * 
 * <h2>INYECCIÓN DE DEPENDENCIAS</h2>
 * El constructor recibe:
 * <ul>
 *   <li>{@code repository}: Puerto de repositorio (abstracción)</li>
 *   <li>{@code mapper}: Conversor DTO <-> Entity</li>
 *   <li>{@code auditTrailService}: Servicio de auditoría</li>
 * </ul>
 * 
 * <h2>EJEMPLO DE USO</h2>
 * <pre>
 * // Crear proveedor
 * CreateProveedorRequest request = new CreateProveedorRequest(
 *     "PROV-001",
 *     "Distribuidora El Norte",
 *     "04-2100-9901",
 *     "ventas@elnorte.com",
 *     "Av. Principal 123",
 *     "Proveedor principal"
 * );
 * 
 * ProveedorSummary creado = commandService.createProveedor(request, httpRequest);
 * </pre>
 * 
 * @see ProveedorQueryService
 * @see ProveedorRepositoryPort
 * @see ProveedorDtoMapper
 * @author Pastelería Development Team
 */
@Service
public class ProveedorCommandService {

  private final ProveedorRepositoryPort repository;
  private final ProveedorDtoMapper mapper;
  private final AuditTrailService auditTrailService;

  public ProveedorCommandService(
      ProveedorRepositoryPort repository,
      ProveedorDtoMapper mapper,
      AuditTrailService auditTrailService
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public ProveedorSummary createProveedor(CreateProveedorRequest request, HttpServletRequest httpRequest) {
    ProveedorEntity entity = new ProveedorEntity();
    mapper.applyCreateRequest(entity, request, OffsetDateTime.now());

    ProveedorSummary summary = mapper.toSummary(repository.save(entity));

    auditTrailService.recordChange(
        "PROVEEDOR_CREADO",
        "ABASTECIMIENTO",
        "proveedor",
        summary.id().toString(),
        "CREAR_PROVEEDOR",
        null,
        summary,
        "Alta de proveedor.",
        httpRequest
    );

    return summary;
  }

  @Transactional
  public ProveedorSummary updateProveedor(Long id, UpdateProveedorRequest request, HttpServletRequest httpRequest) {
    ProveedorEntity entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));

    ProveedorSummary previous = mapper.toSummary(entity);
    mapper.applyUpdateRequest(entity, request, OffsetDateTime.now());
    ProveedorSummary current = mapper.toSummary(repository.save(entity));

    auditTrailService.recordChange(
        "PROVEEDOR_ACTUALIZADO",
        "ABASTECIMIENTO",
        "proveedor",
        entity.getId().toString(),
        "ACTUALIZAR_PROVEEDOR",
        previous,
        current,
        "Actualización de proveedor.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public void deleteProveedor(Long id, HttpServletRequest httpRequest) {
    ProveedorEntity entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));

    auditTrailService.recordChange(
        "PROVEEDOR_ELIMINADO",
        "ABASTECIMIENTO",
        "proveedor",
        entity.getId().toString(),
        "ELIMINAR_PROVEEDOR",
        mapper.toSummary(entity),
        null,
        "Eliminación de proveedor.",
        httpRequest
    );

    repository.delete(entity);
  }
}