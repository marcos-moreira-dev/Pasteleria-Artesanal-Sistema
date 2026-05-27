package com.pasteleria.abastecimiento.api;

import java.util.List;

import com.pasteleria.abastecimiento.application.ProveedorCommandService;
import com.pasteleria.abastecimiento.application.ProveedorQueryService;
import com.pasteleria.abastecimiento.application.ProveedorSummary;
import com.pasteleria.abastecimiento.application.CreateProveedorRequest;
import com.pasteleria.abastecimiento.application.UpdateProveedorRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de Proveedores.
 * 
 * <h2>RESPONSABILIDAD</h2>
 * Este controlador expone endpoints HTTP para operaciones CRUD sobre proveedores,
 * siguiendo los principios RESTful y proporcionando documentación OpenAPI.
 * 
 * <h2>ENDPOINTS</h2>
 * 
 * <h3>Listado y Consulta (Query Operations)</h3>
 * <ul>
 *   <li>{@code GET /api/v1/abastecimiento/proveedores} - Listar todos</li>
 *   <li>{@code GET /api/v1/abastecimiento/proveedores/paginado} - Listado paginado con búsqueda</li>
 *   <li>{@code GET /api/v1/abastecimiento/proveedores/{id}} - Obtener por ID</li>
 * </ul>
 * 
 * <h3>Operaciones de Escritura (Command Operations)</h3>
 * <ul>
 *   <li>{@code POST /api/v1/abastecimiento/proveedores} - Crear nuevo</li>
 *   <li>{@code PUT /api/v1/abastecimiento/proveedores/{id}} - Actualizar existente</li>
 *   <li>{@code DELETE /api/v1/abastecimiento/proveedores/{id}} - Eliminar</li>
 * </ul>
 * 
 * <h2>ARQUITECTURA</h2>
 * <pre>
 * Cliente HTTP
 *      ↓
 * ProveedorController (REST API)
 *      ↓
 * ProveedorQueryService / ProveedorCommandService (Application Layer)
 *      ↓
 * ProveedorRepositoryPort (Port/Interface)
 *      ↓
 * ProveedorRepository (JPA Implementation)
 *      ↓
 * Base de Datos
 * </pre>
 * 
 * <h2>PAGINACIÓN Y BÚSQUEDA</h2>
 * El endpoint paginado soporta:
 * <ul>
 *   <li>page: Número de página (0-indexed)</li>
 *   <li>size: Tamaño de página (default: 8)</li>
 *   <li>query: Término de búsqueda (búsqueda en nombre, código, email)</li>
 * </ul>
 * 
 * <h2>VALIDACIÓN</h2>
 * Todos los endpoints de escritura usan {@code @Valid} para validar
 * los request bodies según las anotaciones en los DTOs:
 * <ul>
 *   <li>{@code @NotBlank}: Campos obligatorios</li>
 *   <li>{@code @Size}: Longitud máxima</li>
 *   <li>{@code @Email}: Formato de correo</li>
 * </ul>
 * 
 * <h2>RESPUESTAS</h2>
 * Todas las respuestas usan {@link ApiResponse} para estandarizar:
 * <ul>
 *   <li>success: boolean indicando éxito</li>
 *   <li>message: Mensaje descriptivo</li>
 *   <li>data: Payload de la respuesta</li>
 *   <li>timestamp: Momento de la respuesta</li>
 *   <li>path: Endpoint solicitado</li>
 * </ul>
 * 
 * <h2>CÓDIGOS HTTP</h2>
 * <ul>
 *   <li>200 OK: Operaciones exitosas</li>
 *   <li>201 Created: Creación exitosa</li>
 *   <li>400 Bad Request: Validación fallida</li>
 *   <li>404 Not Found: Recurso no existe</li>
 *   <li>409 Conflict: Violación de restricción (ej: código duplicado)</li>
 * </ul>
 * 
 * <h2>EJEMPLOS DE USO</h2>
 * 
 * <h3>Crear Proveedor</h3>
 * <pre>
 * POST /api/v1/abastecimiento/proveedores
 * Content-Type: application/json
 * 
 * {
 *   "codigo": "PROV-001",
 *   "nombre": "Distribuidora El Norte S.A.",
 *   "telefono": "04-2100-9901",
 *   "correo": "ventas@elnorte.com",
 *   "direccion": "Av. Carlos Julio Arosemena Km 8",
 *   "observaciones": "Proveedor principal de harinas"
 * }
 * </pre>
 * 
 * <h3>Listado Paginado con Búsqueda</h3>
 * <pre>
 * GET /api/v1/abastecimiento/proveedores/paginado?page=0&size=10&query=norte
 * </pre>
 * 
 * @see ProveedorQueryService
 * @see ProveedorCommandService
 * @see ProveedorSummary
 * @author Pastelería Development Team
 */
@Validated
@Tag(name = "Proveedores", description = "Gestión de proveedores para abastecimiento.")
@RestController
@RequestMapping("/api/v1/abastecimiento/proveedores")
public class ProveedorController {

  private final ProveedorQueryService queryService;
  private final ProveedorCommandService commandService;

  public ProveedorController(ProveedorQueryService queryService, ProveedorCommandService commandService) {
    this.queryService = queryService;
    this.commandService = commandService;
  }

  @Operation(summary = "Listar todos los proveedores.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ProveedorSummary>>> listProveedores(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Proveedores obtenidos correctamente.",
        queryService.listProveedores(),
        request
    ));
  }

  @Operation(summary = "Listar proveedores en formato paginado.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<ProveedorSummary>>> listProveedoresPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Página de proveedores obtenida correctamente.",
        queryService.listProveedoresPage(page, size, query),
        request
    ));
  }

  @Operation(summary = "Registrar nuevo proveedor.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<ProveedorSummary>> createProveedor(
      @Valid @RequestBody CreateProveedorRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Proveedor registrado correctamente.",
        commandService.createProveedor(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar proveedor.")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<ProveedorSummary>> updateProveedor(
      @PathVariable Long id,
      @Valid @RequestBody UpdateProveedorRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Proveedor actualizado correctamente.",
        commandService.updateProveedor(id, body, request),
        request
    ));
  }

  @Operation(summary = "Activar o desactivar proveedor.")
  @PatchMapping("/{id}/toggle-activo")
  public ResponseEntity<ApiResponse<ProveedorSummary>> toggleActivo(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Estado del proveedor actualizado correctamente.",
        commandService.toggleActivo(id, request),
        request
    ));
  }

  @Operation(summary = "Obtener proveedor por ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProveedorSummary>> getById(
      @PathVariable Long id,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Proveedor obtenido correctamente.",
        queryService.getById(id),
        request
    ));
  }

  @Operation(summary = "Eliminar proveedor.")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteProveedor(@PathVariable Long id, HttpServletRequest request) {
    commandService.deleteProveedor(id, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Proveedor eliminado correctamente.",
        null,
        request
    ));
  }
}
