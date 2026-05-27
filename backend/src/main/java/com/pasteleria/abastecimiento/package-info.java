/**
 * Módulo de Abastecimiento (Supply/Inventory Management Module)
 * 
 * Este paquete contiene toda la lógica relacionada con la gestión de inventario,
 * proveedores, compras, recetas y producción en el sistema de pastelería.
 * 
 * <h2>ARQUITECTURA</h2>
 * El módulo sigue una arquitectura hexagonal (ports and adapters) con las siguientes capas:
 * 
 * <h3>1. API Layer (api/)</h3>
 * <ul>
 *   <li>Controladores REST que exponen endpoints HTTP</li>
 *   <li>Manejo de requests/responses HTTP</li>
 *   <li>Validación de entrada (@Valid)</li>
 *   <li>Documentación OpenAPI/Swagger</li>
 * </ul>
 * 
 * <h3>2. Application Layer (application/)</h3>
 * <ul>
 *   <li>Servicios de aplicación que orquestan casos de uso</li>
 *   <li>Separación CQRS: CommandServices (escritura) y QueryServices (lectura)</li>
 *   <li>DTOs (Data Transfer Objects) para contratos de API</li>
 *   <li>Mappers para conversión Entity <-> DTO</li>
 *   <li>Ports (interfaces) que definen contratos de repositorios</li>
 * </ul>
 * 
 * <h3>3. Domain Layer (domain/)</h3>
 * <ul>
 *   <li>Modelos de dominio (enums, value objects)</li>
 *   <li>Lógica de negocio pura, independiente de frameworks</li>
 *   <li>Reglas de negocio codificadas</li>
 * </ul>
 * 
 * <h3>4. Infrastructure Layer (infrastructure/persistence/)</h3>
 * <ul>
 *   <li>Entidades JPA para mapeo objeto-relacional</li>
 *   <li>Implementaciones de repositorios usando Spring Data JPA</li>
 *   <li>Configuración de base de datos</li>
 * </ul>
 * 
 * <h2>PATRONES DE DISEÑO UTILIZADOS</h2>
 * <ul>
 *   <li><b>Repository Pattern</b>: Abstracción del acceso a datos</li>
 *   <li><b>CQRS</b>: Separación de comandos y consultas</li>
 *   <li><b>DTO Pattern</b>: Separación entre modelo de dominio y contratos de API</li>
 *   <li><b>Mapper Pattern</b>: Conversión explícita entre capas</li>
 *   <li><b>Unit of Work</b>: Transacciones atómicas con @Transactional</li>
 * </ul>
 * 
 * <h2>FLUJO TÍPICO DE UN REQUEST</h2>
 * <ol>
 *   <li>Controller recibe HTTP request</li>
 *   <li>Valida entrada con Bean Validation (@Valid)</li>
 *   <li>Delega a Service (Command o Query según operación)</li>
 *   <li>Service usa Repository para acceder a datos</li>
 *   <li>Mapper convierte Entity a DTO</li>
 *   <li>Controller devuelve ApiResponse estandarizada</li>
 * </ol>
 * 
 * <h2>EJEMPLO DE FLUJO - Crear Proveedor</h2>
 * <pre>
 * POST /api/v1/abastecimiento/proveedores
 *   ↓
 * ProveedorController.createProveedor()
 *   ↓
 * ProveedorCommandService.createProveedor()
 *   ↓
 * ProveedorDtoMapper.applyCreateRequest() + repository.save()
 *   ↓
 * AuditTrailService.recordChange() (auditoría)
 *   ↓
 * ProveedorSummary (DTO de respuesta)
 * </pre>
 * 
 * <h2>CARACTERÍSTICAS PRINCIPALES</h2>
 * <ul>
 *   <li>Gestión de proveedores y catálogos de artículos</li>
 *   <li>Control de inventario con alertas de stock</li>
 *   <li>Registro de movimientos (entradas, salidas, ajustes)</li>
 *   <li>Gestión de recetas con cálculo de costos</li>
 *   <li>Órdenes de compra y producción</li>
 *   <li>Dashboard con métricas y KPIs</li>
 *   <li>Auditoría completa de cambios</li>
 * </ul>
 * 
 * <h2>INTEGRACIONES</h2>
 * <ul>
 *   <li>Módulo de Productos (recetas asociadas a productos)</li>
 *   <li>Módulo de Usuarios (auditoría, responsables)</li>
 *   <li>Módulo de Pedidos (producción vinculada a pedidos)</li>
 * </ul>
 * 
 * @author Pastelería Development Team
 * @version 1.0
 * @since 2026
 */
package com.pasteleria.abastecimiento;