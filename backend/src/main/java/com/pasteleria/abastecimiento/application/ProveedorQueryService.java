package com.pasteleria.abastecimiento.application;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.pasteleria.abastecimiento.application.port.ProveedorRepositoryPort;
import com.pasteleria.abastecimiento.application.mapper.ProveedorDtoMapper;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de Aplicación para operaciones de lectura (queries) de Proveedores.
 * 
 * <h2>RESPONSABILIDAD</h2>
 * Este servicio implementa todos los casos de uso de consulta sobre proveedores.
 * Sigue el patrón CQRS donde las operaciones de lectura están optimizadas
 * y separadas de las de escritura.
 * 
 * <h2>OPTIMIZACIONES</h2>
 * La clase está anotada con {@code @Transactional(readOnly = true)}, lo cual:
 * <ul>
 *   <li>Permite optimizaciones del motor de persistencia</li>
 *   <li>Evita dirty checking innecesario</li>
 *   <li>Mejora performance en operaciones de solo lectura</li>
 * </ul>
 * 
 * <h2>OPERACIONES SOPORTADAS</h2>
 * <ul>
 *   <li>{@link #listProveedores}: Listar todos ordenados por fecha de creación</li>
 *   <li>{@link #listProveedoresPage}: Listado paginado con búsqueda</li>
 *   <li>{@link #findById}: Buscar por ID (Optional)</li>
 *   <li>{@link #getById}: Buscar por ID (lanza excepción si no existe)</li>
 *   <li>{@link #listActive}: Listar solo proveedores activos</li>
 * </ul>
 * 
 * <h2>PAGINACIÓN</h2>
 * El método {@code listProveedoresPage} implementa paginación con:
 * <ul>
 *   <li>Número de página (0-indexed)</li>
 *   <li>Tamaño de página</li>
 *   <li>Búsqueda por texto (búsqueda en nombre, código, email)</li>
 *   <li>Ordenamiento por createdAt DESC</li>
 * </ul>
 * 
 * <h2>BÚSQUEDA</h2>
 * La búsqueda implementa:
 * <ul>
 *   <li>Búsqueda case-insensitive (toLowerCase)</li>
 *   <li>Búsqueda parcial (LIKE %query%)</li>
 *   <li>Búsqueda en múltiples campos (nombre, código, email)</li>
 * </ul>
 * 
 * <h2>COMPONENTES</h2>
 * <ul>
 *   <li>{@code repository}: Puerto de repositorio para acceso a datos</li>
 *   <li>{@code mapper}: Conversor Entity -> DTO</li>
 *   <li>{@code pageMapper}: Conversor Page<Entity> -> PageResponseDto</li>
 *   <li>{@code pageRequestFactory}: Factory para crear objetos PageRequest</li>
 * </ul>
 * 
 * <h2>EJEMPLOS DE USO</h2>
 * 
 * <h3>Listar todos</h3>
 * <pre>
 * List&lt;ProveedorSummary&gt; proveedores = queryService.listProveedores();
 * </pre>
 * 
 * <h3>Listado paginado con búsqueda</h3>
 * <pre>
 * PageResponseDto&lt;ProveedorSummary&gt; page = 
 *     queryService.listProveedoresPage(0, 10, "norte");
 * // page.content: lista de proveedores
 * // page.totalElements: total de resultados
 * // page.totalPages: total de páginas
 * </pre>
 * 
 * <h3>Buscar por ID</h3>
 * <pre>
 * // Opción 1: Usar Optional (recomendado para validaciones)
 * Optional&lt;ProveedorSummary&gt; opt = queryService.findById(1L);
 * 
 * // Opción 2: Lanzar excepción si no existe (para operaciones que requieren el recurso)
 * ProveedorSummary prov = queryService.getById(1L); // lanza ResourceNotFoundException
 * </pre>
 * 
 * @see ProveedorCommandService
 * @see ProveedorRepositoryPort
 * @see PageResponseDto
 * @author Pastelería Development Team
 */
@Service
@Transactional(readOnly = true)
public class ProveedorQueryService {

  private final ProveedorRepositoryPort repository;
  private final ProveedorDtoMapper mapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public ProveedorQueryService(
      ProveedorRepositoryPort repository,
      ProveedorDtoMapper mapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<ProveedorSummary> listProveedores() {
    return repository.findAllByOrderByCreatedAtDesc().stream()
        .map(mapper::toSummary)
        .toList();
  }

  public PageResponseDto<ProveedorSummary> listProveedoresPage(int page, int size, String query) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

    return pageMapper.toPageResponseDto(
        (normalizedQuery.isBlank()
            ? repository.findAllByOrderByCreatedAtDesc(pageable)
            : repository.findBySearchTerm(normalizedQuery, pageable))
            .map(mapper::toSummary)
    );
  }

  public Optional<ProveedorSummary> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toSummary);
  }

  public ProveedorSummary getById(Long id) {
    return findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));
  }

  public List<ProveedorSummary> listActive() {
    return repository.findByActiveTrue().stream()
        .map(mapper::toSummary)
        .toList();
  }
}