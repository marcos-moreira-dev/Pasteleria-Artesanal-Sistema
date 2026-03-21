# Módulo de Abastecimiento - Documentación Completa

## Índice

1. [Visión General](#visión-general)
2. [Arquitectura del Módulo](#arquitectura-del-módulo)
3. [Entidades del Dominio](#entidades-del-dominio)
4. [Flujo de Datos](#flujo-de-datos)
5. [Procesos de Negocio](#procesos-de-negocio)
6. [Integración Frontend-Backend](#integración-frontend-backend)
7. [Decisiones de Diseño](#decisiones-de-diseño)
8. [Patrones de Diseño Utilizados](#patrones-de-diseño-utilizados)

---

## Visión General

El **Módulo de Abastecimiento** es un componente crítico del sistema de gestión de pastelería que administra:

- **Inventario**: Control de ingredientes e insumos
- **Proveedores**: Directorio y relaciones comerciales
- **Compras**: Órdenes de compra y recepciones
- **Recetas**: Fórmulas de producción con costos estimados
- **Movimientos**: Registro de entradas, salidas y ajustes
- **Producción**: Órdenes de producción y consumos

### Objetivos del Módulo

1. **Mantener stock óptimo**: Evitar desabastecimiento y exceso de inventario
2. **Control de costos**: Seguimiento de precios y costos de producción
3. **Trazabilidad**: Auditoría completa de movimientos
4. **Eficiencia operativa**: Automatización de procesos de reposición
5. **Toma de decisiones**: Dashboard con métricas y alertas

---

## Arquitectura del Módulo

### Arquitectura Hexagonal (Ports & Adapters)

El backend sigue una arquitectura hexagonal que separa:

```
┌─────────────────────────────────────────────────────────────┐
│                         API Layer                           │
│  (Controllers - REST Endpoints)                             │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Command    │  │    Query     │  │    DTOs      │     │
│  │   Services   │  │   Services   │  │   Mappers    │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
├─────────────────────────────────────────────────────────────┤
│                    Domain Layer                             │
│  ┌──────────────┐  ┌──────────────┐                        │
│  │    Model     │  │    Ports     │                        │
│  │   (Enums)    │  │(Interfaces)  │                        │
│  └──────────────┘  └──────────────┘                        │
├─────────────────────────────────────────────────────────────┤
│                 Infrastructure Layer                        │
│  ┌──────────────┐  ┌──────────────┐                        │
│  │  Entities    │  │ Repositories │                        │
│  │   (JPA)      │  │ (JPA Impl)   │                        │
│  └──────────────┘  └──────────────┘                        │
└─────────────────────────────────────────────────────────────┘
```

### Estructura de Paquetes (Backend)

```
com.pasteleria.abastecimiento/
├── api/                           # Controladores REST
│   ├── ProveedorController.java
│   ├── InsumoController.java
│   ├── IngredienteController.java
│   ├── RecetaController.java
│   ├── InventarioMovimientoController.java
│   └── AbastecimientoDashboardController.java
├── application/                   # Lógica de aplicación
│   ├── port/                     # Interfaces de repositorio
│   ├── mapper/                   # Mapeo DTO <-> Entity
│   ├── *CommandService.java      # Operaciones de escritura
│   ├── *QueryService.java        # Operaciones de lectura
│   └── *Request.java / *Summary.java  # DTOs
├── domain/                       # Lógica de dominio
│   └── model/
│       ├── TipoMovimiento.java
│       ├── EstadoOrdenCompra.java
│       └── ItemTipo.java
└── infrastructure/               # Implementación técnica
    └── persistence/
        ├── entity/              # Entidades JPA
        └── repository/          # Implementaciones JPA
```

### Arquitectura Frontend (Angular)

```
app/features/abastecimiento/
├── models/
│   └── abastecimiento.models.ts    # Interfaces TypeScript
└── pages/
    ├── abastecimiento-shell.component.ts      # Landing page
    ├── abastecimiento-dashboard.component.ts  # Dashboard
    ├── abastecimiento-proveedores.component.ts # CRUD Proveedores
    ├── abastecimiento-inventario.component.ts  # Gestión inventario
    ├── abastecimiento-movimientos.component.ts # Historial
    ├── abastecimiento-compras.component.ts     # Órdenes de compra
    └── abastecimiento-reportes.component.ts    # Reportes
```

---

## Entidades del Dominio

### Diagrama Entidad-Relación

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Proveedor   │────<│ItemProveedor │>────│ Ingrediente  │
└──────────────┘     └──────────────┘     └──────────────┘
       │                                          │
       │         ┌──────────────┐                │
       └────────<│OrdenCompra   │>───────────────┘
                 └──────────────┘
                        │
       ┌────────────────┼────────────────┐
       ▼                ▼                ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│OrdenCompra   │  │Inventario    │  │   Receta     │
│  Detalle     │  │ Movimiento   │  └──────────────┘
└──────────────┘  └──────────────┘         │
                                          │
                                    ┌─────┴──────┐
                                    ▼            ▼
                              ┌──────────┐  ┌──────────┐
                              │  Receta  │  │ Detalle  │
                              │  Entity  │  │  Receta  │
                              └──────────┘  └──────────┘
```

### Descripción de Entidades

#### 1. Proveedor

- **Propósito**: Directorio de proveedores de materias primas
- **Campos clave**: código, nombre, contacto, dirección, activo
- **Relaciones**:
  - One-to-Many con `ItemProveedor` (catálogo de artículos)
  - One-to-Many con `OrdenCompra` (historial de compras)

#### 2. Ingrediente / Insumo

- **Propósito**: Materias primas para producción
- **Diferencia**: Ingrediente = comestible, Insumo = empaque/decoración
- **Campos clave**: código, nombre, stock_minimo, stock_actual, costo_referencial
- **Relaciones**: Many-to-One con `Umedida` (unidad de medida)

#### 3. ItemProveedor (Catálogo)

- **Propósito**: Relacionar proveedores con sus artículos y precios
- **Campos clave**: precio_suministro, es_principal
- **Business rule**: Solo un proveedor principal por item

#### 4. Receta

- **Propósito**: Fórmulas de producción con costos estimados
- **Campos clave**: rendimiento_base, costo_estimado, es_activa
- **Relaciones**:
  - Many-to-One con `Producto`
  - One-to-Many con `DetalleReceta`

#### 5. InventarioMovimiento

- **Propósito**: Registro auditable de todos los movimientos
- **Tipos**: ENTRADA_COMPRA, ENTRADA_AJUSTE, SALIDA_PRODUCCION, SALIDA_MERMA, SALIDA_AJUSTE
- **Campos clave**: cantidad, saldo_posterior, referencia_tipo, referencia_id

#### 6. OrdenCompra / OrdenProduccion

- **Propósito**: Gestión de procesos operativos
- **Estados**: BORRADOR → ENVIADA → RECIBIDA_PARCIAL → RECIBIDA

---

## Flujo de Datos

### 1. Flujo de Compra (Purchase Flow)

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  Alerta  │───>│ Crear OC │───>│  Enviar  │───>│ Recibir  │
│ Stock    │    │          │    │  a Prov  │    │ Mercadería│
└──────────┘    └──────────┘    └──────────┘    └──────────┘
                                                      │
                                                      ▼
                                               ┌──────────┐
                                               │ Registrar│
                                               │ Entrada  │
                                               │ Inventario│
                                               └──────────┘
```

**API Calls**:

1. `GET /api/v1/abastecimiento/dashboard` - Ver alertas
2. `POST /api/v1/abastecimiento/ordenes-compra` - Crear OC
3. `PATCH /api/v1/abastecimiento/ordenes-compra/{id}/estado` - Cambiar estado
4. `POST /api/v1/abastecimiento/inventario/movimientos` - Registrar entrada

### 2. Flujo de Producción (Production Flow)

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  Pedido  │───>│ Crear OP │───>│ Consumir │───>│ Finalizar│
│  Nuevo   │    │          │    │ Materiales│    │ Producción│
└──────────┘    └──────────┘    └──────────┘    └──────────┘
```

**API Calls**:

1. `POST /api/v1/abastecimiento/ordenes-produccion` - Crear OP
2. `POST /api/v1/abastecimiento/consumos` - Registrar consumo
3. `PATCH /api/v1/abastecimiento/ordenes-produccion/{id}/estado` - Finalizar

### 3. Flujo de Ajuste de Inventario

```
┌──────────┐    ┌──────────┐    ┌──────────┐
│ Detectar │───>│ Crear    │───>│ Actualizar│
│ Diferencia│    │ Movimiento│    │ Stock     │
└──────────┘    └──────────┘    └──────────┘
```

---

## Procesos de Negocio

### 1. Cálculo de Costos de Receta

```java
// Fórmula: SUM(cantidad_ingrediente * costo_referencial)
BigDecimal costoEstimado = detalles.stream()
    .map(d -> d.getCantidadBase()
        .multiply(d.getIngrediente().getCostoReferencial()))
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

**Ejemplo real (Torta de Chocolate)**:

- Harina 0000: 500g × $1.20 = $600.00
- Azúcar blanca: 400g × $1.10 = $440.00
- Huevos: 6 × $0.50 = $3.00
- Mantequilla: 250g × $4.50 = $1,125.00
- **Total estimado**: ~$3,500 (varía según ingredientes)

### 2. Alertas de Stock

```typescript
// Cálculo de estado del inventario
if (stockActual <= 0) return "AGOTADO";
if (stockActual <= stockMinimo * 0.5) return "CRITICO";
if (stockActual <= stockMinimo) return "BAJO";
return "NORMAL";
```

### 3. Movimientos de Inventario

Cada movimiento genera:

1. Registro en `inventario_movimiento`
2. Actualización de `stock_actual` en ingrediente/insumo
3. Auditoría de cambios
4. Posible generación de alertas

### 4. Cálculo de Cobertura

```sql
-- Días de cobertura = Stock actual / Consumo promedio diario
SELECT
    i.stock_actual,
    i.stock_minimo,
    COALESCE(
        i.stock_actual / NULLIF(
            (SELECT AVG(cantidad)
             FROM consumo_produccion
             WHERE item_id = i.ingrediente_id
             AND fecha >= CURRENT_DATE - 30), 0
        ), 999
    ) as cobertura_dias
FROM ingrediente i;
```

---

## Integración Frontend-Backend

### 1. Modelos Compartidos

Los DTOs Java corresponden a interfaces TypeScript:

```typescript
// Backend: Java Record
public record ProveedorSummary(
    Long id,
    String codigo,
    String nombre,
    ...
) {}

// Frontend: TypeScript Interface
export interface ProveedorSummary {
  id: number;           // Long -> number
  codigo: string;       // String -> string
  nombre: string;
  createdAt: string;    // OffsetDateTime -> ISO string
}
```

### 2. Estado Global (Angular Signals)

```typescript
// backoffice-store.service.ts
readonly abastecimientoDashboard = signal<AbastecimientoDashboard | null>(null);
readonly proveedores = signal<ProveedorSummary[]>([]);
readonly ingredientesPage = signal<Page<IngredienteSummary> | null>(null);
```

**Ventajas de Signals**:

- Reactividad granular
- Detección de cambios eficiente
- Código más limpio que RxJS para estado simple

### 3. Paginación

```typescript
// Backend devuelve PageResponseDto
public record PageResponseDto<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
) {}

// Frontend usa signals computadas
readonly paginatedItems = computed(() => {
  const start = (currentPage() - 1) * pageSize;
  return items().slice(start, start + pageSize);
});
```

### 4. Manejo de Errores

```typescript
// Backend: ProblemDetail (RFC 7807)
@ExceptionHandler(ResourceNotFoundException.class)
public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
    return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
}

// Frontend: Interceptor
intercept(req: HttpRequest<any>, next: HttpHandler) {
  return next.handle(req).pipe(
    catchError(error => {
      if (error.status === 404) {
        this.notification.show('Recurso no encontrado');
      }
      return throwError(() => error);
    })
  );
}
```

---

## Decisiones de Diseño

### 1. Separación Ingrediente/Insumo

**Decisión**: Mantener tablas separadas aunque sean similares.

**Justificación**:

- Semántica clara (comestible vs no-comestible)
- Posibles campos diferentes en el futuro
- Reporting separado para análisis de costos
- Validaciones de negocio diferentes

### 2. Tabla Unificada de Movimientos

**Decisión**: Una sola tabla `inventario_movimiento` para ambos tipos.

**Justificación**:

- Campos idénticos para ambos tipos
- Consultas más simples (un solo punto de entrada)
- Auditoría unificada
- Evita duplicación de lógica

### 3. Campos `item_tipo` e `item_id` (Polimorfismo)

**Decisión**: No usar JPA inheritance, usar discriminador manual.

**Ventajas**:

- Mejor performance (sin JOINs de inheritance)
- Flexibilidad para nuevos tipos
- Queries más simples y rápidas

**Desventajas**:

- Menos type-safety en compilación
- Requiere validaciones manuales

### 4. Uso de Records para DTOs (Java 17+)

**Decisión**: Usar `record` en lugar de `class` para DTOs.

**Beneficios**:

- Inmutabilidad por defecto
- Menos código boilerplate (getters, equals, hashCode, toString)
- Pattern matching en switch (Java 21+)

### 5. Angular Standalone Components

**Decisión**: Usar componentes standalone sin NgModules.

**Beneficios**:

- Lazy loading más granular
- Menos boilerplate
- Tree-shaking mejorado
- Carga más rápida

### 6. Signals vs Observables

**Decisión**: Usar Signals para estado local, Observables para HTTP.

**Justificación**:

- Signals: Mejor performance, código más limpio
- Observables: Necesarios para operaciones asíncronas complejas

---

## Patrones de Diseño Utilizados

### 1. CQRS (Command Query Responsibility Segregation)

Separación clara entre:

- **Commands**: Operaciones que modifican estado (Create, Update, Delete)
- **Queries**: Operaciones de solo lectura

```java
// Commands
ProveedorCommandService.createProveedor(...)
ProveedorCommandService.updateProveedor(...)

// Queries
ProveedorQueryService.listProveedores()
ProveedorQueryService.getById(...)
```

### 2. Repository Pattern

Abstracción del acceso a datos mediante puertos:

```java
// Port (Interfaz en application layer)
public interface ProveedorRepositoryPort {
    Optional<ProveedorEntity> findById(Long id);
    ProveedorEntity save(ProveedorEntity entity);
    void delete(ProveedorEntity entity);
}

// Implementation (Infrastructure layer)
public interface ProveedorRepository
    extends JpaRepository<ProveedorEntity, Long>,
            ProveedorRepositoryPort {
}
```

### 3. DTO Pattern

Separación entre entidades de dominio y contratos de API:

```java
// Entity (uso interno)
@Entity
public class ProveedorEntity { ... }

// DTO (contrato API)
public record ProveedorSummary(...) {}
public record CreateProveedorRequest(...) {}
```

### 4. Mapper Pattern

Conversión explícita entre capas:

```java
@Component
public class ProveedorDtoMapper {
    public ProveedorSummary toSummary(ProveedorEntity entity) { ... }
    public void applyCreateRequest(ProveedorEntity entity,
                                   CreateProveedorRequest request,
                                   OffsetDateTime now) { ... }
}
```

### 5. Unit of Work (Spring Transactions)

```java
@Transactional
public ProveedorSummary createProveedor(...) {
    // Todas las operaciones son atómicas
    // Si falla una, se hace rollback de todo
}
```

### 6. Strategy Pattern (Movimientos)

Diferentes estrategias según tipo de movimiento:

```java
public interface MovimientoStrategy {
    void procesar(InventarioMovimientoEntity movimiento);
}

@Component
public class EntradaCompraStrategy implements MovimientoStrategy { ... }

@Component
public class SalidaProduccionStrategy implements MovimientoStrategy { ... }
```

### 7. Factory Pattern (PageRequest)

```java
@Component
public class PageRequestFactory {
    public PageRequest create(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
```

### 8. Singleton Pattern (Spring Beans)

Todos los servicios son singletons gestionados por Spring:

```java
@Service  // = @Component + Singleton scope
public class ProveedorCommandService { ... }
```

### 9. Observer Pattern (Angular Signals)

```typescript
// Signal es observable
readonly dashboard = computed(() => this.store.abastecimientoDashboard());

// Auto-actualización cuando cambia la señal
effect(() => {
  console.log('Dashboard updated:', this.dashboard());
});
```

### 10. Template Method (Auditoría)

```java
// Base class define el flujo
public abstract class AuditableService {
    public final <T> T executeWithAudit(String action, Supplier<T> operation) {
        T result = operation.get();
        auditTrailService.recordChange(action, ...);
        return result;
    }
}
```

---

## API Endpoints Reference

### Proveedores

```
GET    /api/v1/abastecimiento/proveedores
GET    /api/v1/abastecimiento/proveedores/paginado?page=0&size=8&query=
GET    /api/v1/abastecimiento/proveedores/{id}
POST   /api/v1/abastecimiento/proveedores
PUT    /api/v1/abastecimiento/proveedores/{id}
DELETE /api/v1/abastecimiento/proveedores/{id}
```

### Inventario (Ingredientes/Insumos)

```
GET    /api/v1/abastecimiento/ingredientes/paginado
GET    /api/v1/abastecimiento/insumos/paginado
POST   /api/v1/abastecimiento/ingredientes
PUT    /api/v1/abastecimiento/ingredientes/{id}
```

### Movimientos

```
GET    /api/v1/abastecimiento/inventario/{itemTipo}/{itemId}
GET    /api/v1/abastecimiento/inventario/{itemTipo}/{itemId}/paginado
POST   /api/v1/abastecimiento/inventario              # Crear movimiento
```

### Recetas

```
GET    /api/v1/abastecimiento/recetas
GET    /api/v1/abastecimiento/recetas/paginado
GET    /api/v1/abastecimiento/recetas/{id}            # Con detalles
GET    /api/v1/abastecimiento/recetas/producto/{productoId}
POST   /api/v1/abastecimiento/recetas
PUT    /api/v1/abastecimiento/recetas/{id}
DELETE /api/v1/abastecimiento/recetas/{id}
PATCH  /api/v1/abastecimiento/recetas/{id}/toggle-activa
```

### Dashboard

```
GET    /api/v1/abastecimiento/dashboard
```

---

## Métricas y KPIs

### Dashboard Metrics

| Métrica            | Descripción                   | Cálculo                                                  |
| ------------------ | ----------------------------- | -------------------------------------------------------- |
| Items Críticos     | Stock <= 50% del mínimo       | `COUNT WHERE stock_actual <= stock_minimo * 0.5`         |
| Items Bajo Mínimo  | Stock <= mínimo               | `COUNT WHERE stock_actual <= stock_minimo`               |
| Órdenes Pendientes | OC en estado BORRADOR/ENVIADA | `COUNT WHERE estado IN ('BORRADOR', 'ENVIADA')`          |
| Costo Reposición   | Costo estimado de reposición  | `SUM((stock_minimo - stock_actual) * costo_referencial)` |

### Alertas

| Tipo    | Condición           | Acción Sugerida       |
| ------- | ------------------- | --------------------- |
| CRÍTICO | Stock <= 50% mínimo | Ordenar urgente       |
| BAJO    | Stock <= mínimo     | Planificar reposición |
| RIESGO  | Cobertura < 7 días  | Monitorear consumo    |
| BLOQUEO | Stock = 0           | Bloquear producción   |

---

## Seguridad y Permisos

### Roles y Acceso

| Rol                      | Permisos                              |
| ------------------------ | ------------------------------------- |
| ADMIN                    | CRUD completo                         |
| ENCARGADO_ABASTECIMIENTO | CRUD proveedores, inventario, recetas |
| OPERADOR                 | Solo lectura + registrar movimientos  |
| CONTADOR                 | Lectura + reportes                    |

### Auditoría

Todos los cambios se registran en `audit_trail`:

- Quién (usuario)
- Qué (tipo de cambio)
- Cuándo (timestamp)
- Dónde (IP, endpoint)
- Valor anterior y nuevo

---

## Testing

### Estrategia de Testing

```
Unit Tests (JUnit 5 + Mockito)
├── Service Layer (lógica de negocio)
├── Mapper Layer (conversiones)
└── Repository Layer (queries custom)

Integration Tests (Spring Boot Test)
├── API Layer (@WebMvcTest)
├── Database (@DataJpaTest)
└── End-to-End (@SpringBootTest)
```

### Cobertura Mínima

- Lógica de negocio: 80%
- Mappers: 100%
- Validaciones: 100%

---

## Performance

### Optimizaciones Implementadas

1. **Lazy Loading**: `@ManyToOne(fetch = FetchType.LAZY)`
2. **Índices de Base de Datos**:
   - `CREATE INDEX idx_item_tipo_item_id ON inventario_movimiento(item_tipo, item_id)`
3. **Paginación**: Todos los endpoints de listado
4. **Proyecciones**: DTOs en lugar de entidades completas
5. **Caché**: Spring Cache para catálogos (Umedida, Proveedores)

### Consultas Costosas

```sql
-- Dashboard: Alertas (ejecutada cada carga)
SELECT i.*,
       (i.stock_actual <= i.stock_minimo * 0.5) as es_critico
FROM ingrediente i
WHERE i.stock_actual <= i.stock_minimo;

-- Optimización: Índice compuesto
CREATE INDEX idx_stock_alerta ON ingrediente(stock_actual, stock_minimo);
```

---

## Escalabilidad

### Escenarios de Crecimiento

| Escenario               | Solución                           |
| ----------------------- | ---------------------------------- |
| +10,000 movimientos/día | Particionar por fecha              |
| +100 proveedores        | Caché Redis para catálogos         |
| Múltiples sucursales    | Agregar `sucursal_id` a entidades  |
| Mobile app              | API GraphQL para queries flexibles |

---

## Conclusión

El Módulo de Abastecimiento está diseñado siguiendo principios sólidos:

1. **Clean Architecture**: Separación clara de responsabilidades
2. **Domain-Driven Design**: Modelo rico que refleja el negocio
3. **CQRS**: Optimizado para lecturas y escrituras
4. **RESTful API**: Contratos claros y versionados
5. **Modern Frontend**: Angular con signals para mejor UX

La arquitectura permite:

- ✅ Mantenibilidad a largo plazo
- ✅ Testing automatizado
- ✅ Escalabilidad horizontal
- ✅ Integración con otros módulos
- ✅ Evolución gradual del sistema

---

## Anexos

### A. Glosario

- **OC**: Orden de Compra
- **OP**: Orden de Producción
- **SKU**: Stock Keeping Unit (código único de item)
- **FIFO**: First In, First Out (método de inventario)
- **Lote**: Cantidad producida/comprada junta

### B. Códigos de Error

| Código    | Descripción                    |
| --------- | ------------------------------ |
| ABAST-001 | Proveedor no encontrado        |
| ABAST-002 | Ingrediente no encontrado      |
| ABAST-003 | Stock insuficiente             |
| ABAST-004 | Receta ya existe para producto |
| ABAST-005 | Orden de compra inválida       |

### C. Convenciones de Nomenclatura

- **Tablas**: singular, minúscula, snake_case
- **Columnas**: snake_case, español
- **Java classes**: CamelCase, inglés (entities), español (DTOs)
- **TypeScript**: camelCase, interfaces con sufijo `Summary`, `Request`

---

_Documentación generada para fines educativos y de referencia técnica._
_Última actualización: Marzo 2026_
