# Quick Reference - Modulo de Abastecimiento

## Atajos para Desarrolladores

### Encontrar Codigo Rapidamente

| Que buscar        | Donde encontrar                               |
| ----------------- | --------------------------------------------- |
| API Endpoints     | `api/*Controller.java`                        |
| Logica de negocio | `application/*Service.java`                   |
| Entidades BD      | `infrastructure/persistence/entity/*.java`    |
| Contratos API     | `application/*Summary.java` / `*Request.java` |
| Mapeos            | `application/mapper/*Mapper.java`             |
| Interfaces BD     | `application/port/*RepositoryPort.java`       |
| Modelos TS        | `models/abastecimiento.models.ts`             |
| Componentes UI    | `pages/*.component.ts`                        |

### Comandos Utiles

```bash
# Backend - Tests
./mvnw test -Dtest=Proveedor*Test
./mvnw test -Dtest=Abastecimiento*Test

# Backend - Compilar
./mvnw clean compile

# Backend - Empaquetar
./mvnw -DskipTests package

# Frontend - Tests
ng test --include='**/abastecimiento/**'

# Frontend - Serve
ng serve --project=frontend-admin-angular

# DB - Seed data
psql -d pasteleria -f db/V1/DATABASE_SEED_CANONICO.sql

# Smoke real de orden de compra
powershell -ExecutionPolicy Bypass -File backend/scripts/smoke-orden-compra.ps1
```

### Estructura de URLs

```text
/api/v1/abastecimiento/
|-- proveedores
|   |-- GET    /                    # Listar todos
|   |-- GET    /paginado            # Paginado + busqueda
|   |-- GET    /{id}                # Obtener uno
|   |-- POST   /                    # Crear
|   |-- PUT    /{id}                # Actualizar
|   |-- PATCH  /{id}/toggle-activo  # Activar/desactivar
|   `-- DELETE /{id}                # Eliminar
|-- ingredientes (misma estructura)
|-- insumos (misma estructura)
|-- recetas
|   |-- GET    /                    # Listar
|   |-- GET    /{id}                # Con detalles
|   |-- GET    /producto/{id}       # Por producto
|   |-- POST   /                    # Crear
|   |-- PUT    /{id}                # Actualizar
|   |-- DELETE /{id}                # Eliminar
|   `-- PATCH  /{id}/toggle-activa  # Activar/desactivar
|-- ordenes-compra
|   |-- GET    /paginado            # Tabla paginada
|   |-- GET    /{id}                # Detalle completo
|   |-- GET    /por-proveedor/{id}  # Historial por proveedor
|   |-- POST   /                    # Crear borrador
|   |-- PUT    /{id}                # Editar borrador
|   |-- PATCH  /{id}/estado         # Enviar o cancelar
|   `-- POST   /{id}/recibir        # Recepcion parcial o total
|-- inventario
|   |-- GET    /                    # Movimientos globales con filtros opcionales
|   |-- GET    /{tipo}/{id}         # Movimientos por item
|   |-- GET    /{tipo}/{id}/paginado
|   |-- GET    /referencia/{tipo}/{id}
|   `-- POST   /                    # Registrar movimiento
`-- dashboard
    `-- GET    /                    # Metricas consolidadas
```

### Produccion Operativa

```text
/api/v1/produccion/
|-- GET    /                    # Cola operativa completa
|-- GET    /paginado            # Cola paginada para tablero
`-- PATCH  /{id}/estado         # Cambio de etapa productiva
```

### Paleta de Colores (Frontend)

```css
/* Primarios */
--color-primary: #5a3424; /* Marron oscuro */
--color-secondary: #8a5c46; /* Marron medio */
--color-accent: #c96e4a; /* Terracota */

/* Fondos */
--bg-surface: #fff9f4; /* Crema */
--bg-card: #ffffff;
--bg-tinted: linear-gradient(
  180deg,
  rgba(255, 247, 241, 0.95),
  rgba(255, 252, 249, 0.95)
);

/* Estados */
--color-success: #1b5e20; /* Verde */
--color-warning: #d68910; /* Naranja */
--color-danger: #b71c1c; /* Rojo */
--color-info: #1565c0; /* Azul */

/* Alertas */
--alert-critical: #fdecea; /* Rojo claro */
--alert-low: #fff8e1; /* Amarillo claro */
--alert-risk: #e3f2fd; /* Azul claro */
--alert-block: #f3e5f5; /* Violeta claro */
```

### Snippets de Codigo

#### Backend - Crear un Service

```java
@Service
@Transactional(readOnly = true)
public class MiEntidadQueryService {

    private final MiEntidadRepositoryPort repository;
    private final MiEntidadDtoMapper mapper;

    public MiEntidadQueryService(
        MiEntidadRepositoryPort repository,
        MiEntidadDtoMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<MiEntidadSummary> listAll() {
        return repository.findAll().stream()
            .map(mapper::toSummary)
            .toList();
    }
}
```

#### Frontend - Componente con Signals

```typescript
@Component({
  selector: "app-mi-componente",
  standalone: true,
  imports: [CommonModule],
  template: `...`,
})
export class MiComponenteComponent implements OnInit {
  private readonly store = inject(BackofficeStoreService);

  readonly loading = signal(true);
  readonly data = signal<MiData[]>([]);
  readonly totalItems = computed(() => this.data().length);
  readonly hasData = computed(() => this.data().length > 0);

  ngOnInit() {
    this.loadData();
  }

  private loadData() {
    this.store.loadMiData().subscribe({
      next: (result) => {
        this.data.set(result);
        this.loading.set(false);
      },
      error: (err) => {
        console.error("Error:", err);
        this.loading.set(false);
      },
    });
  }
}
```

#### Base de Datos - Nuevo Movimiento

```sql
-- Registrar entrada por compra
INSERT INTO inventario_movimiento (
    item_tipo,
    item_id,
    tipo_movimiento,
    cantidad,
    saldo_posterior,
    referencia_tipo,
    referencia_id,
    observaciones,
    fecha_movimiento
) VALUES (
    'INGREDIENTE',
    1,
    'ENTRADA_COMPRA',
    1000.00,
    5000.00,
    'ORDEN_COMPRA',
    'OC-2026-001',
    'Recepcion proveedor El Norte',
    NOW()
);

UPDATE ingrediente
SET stock_actual = stock_actual + 1000.00,
    updated_at = NOW()
WHERE ingrediente_id = 1;
```

### Debugging

#### Backend

```java
log.info("Iniciando operacion: {} para ID: {}", operation, id);
log.debug("Stock anterior: {}, Cantidad: {}, Nuevo stock: {}",
    stockAnterior, cantidad, nuevoStock);
log.error("Error al procesar: {}", ex.getMessage(), ex);
```

#### Frontend

```typescript
constructor() {
  effect(() => {
    console.log("Dashboard updated:", this.dashboard());
  });
}

this.form.valueChanges.subscribe(values => {
  console.log("Form values:", values);
});

console.log("Store state:", this.store);
```

### Queries de Analisis

```sql
-- Stock critico
SELECT nombre, stock_actual, stock_minimo,
       ROUND((stock_actual / stock_minimo) * 100, 2) as pct
FROM ingrediente
WHERE stock_actual <= stock_minimo
ORDER BY pct ASC;

-- Consumo mensual por ingrediente
SELECT i.nombre,
       SUM(ABS(im.cantidad)) as total_consumido,
       COUNT(*) as num_movimientos
FROM inventario_movimiento im
JOIN ingrediente i ON im.item_id = i.ingrediente_id
WHERE im.item_tipo = 'INGREDIENTE'
  AND im.tipo_movimiento = 'SALIDA_PRODUCCION'
  AND im.fecha_movimiento >= DATE_TRUNC('month', CURRENT_DATE)
GROUP BY i.nombre
ORDER BY total_consumido DESC
LIMIT 10;

-- Valor del inventario
SELECT
    'INGREDIENTES' as tipo,
    SUM(stock_actual * costo_referencial) as valor_total
FROM ingrediente
WHERE activo = true
UNION ALL
SELECT
    'INSUMOS' as tipo,
    SUM(stock_actual * costo_referencial) as valor_total
FROM insumo
WHERE activo = true;
```

### Seguridad - Roles

```java
@PreAuthorize("hasRole('ADMIN') or hasRole('ATENCION')")
@PostMapping
public ResponseEntity<...> create(...) { ... }

@PreAuthorize("hasAnyRole('ADMIN', 'ATENCION', 'PRODUCCION')")
@GetMapping
public ResponseEntity<...> list(...) { ... }
```

### Testing - Ejemplo

```java
@Test
void shouldCreateProveedor() {
    CreateProveedorRequest request = new CreateProveedorRequest(
        "PROV-TEST",
        "Proveedor Test",
        "0999999999",
        "test@test.com",
        "Direccion",
        "Observaciones"
    );

    ProveedorSummary result = service.createProveedor(request, mockRequest);

    assertThat(result).isNotNull();
    assertThat(result.codigo()).isEqualTo("PROV-TEST");
    assertThat(result.activo()).isTrue();
}
```

### Checklist antes de commit

- [ ] Codigo compila sin errores
- [ ] Tests pasan (`./mvnw test`, `ng test`)
- [ ] Lint sin errores (`ng lint`)
- [ ] Documentacion actualizada (si aplica)
- [ ] Commits con mensajes claros
- [ ] No hay credenciales expuestas
- [ ] No hay `console.log` en produccion

### Troubleshooting

| Problema            | Solucion                                             |
| ------------------- | ---------------------------------------------------- |
| Bean not found      | Verificar `@Service`/`@Component`, constructor injection |
| Lazy Loading error  | Agregar `@Transactional`, verificar `FetchType`      |
| 404 en API          | Verificar `@RequestMapping`, `@PathVariable`         |
| Signal no actualiza | Verificar que se use `.set()` o `.update()`          |
| Form no valida      | Verificar `FormGroup`, `Validators`, `markAllAsTouched()` |
| Query lenta         | Revisar indices, usar `EXPLAIN ANALYZE`              |
| CORS error          | Verificar configuracion de `SecurityConfig`          |

### Recursos

- Documentacion completa: `docs/ABASTECIMIENTO_DOCUMENTACION_COMPLETA.md`
- Resumen de revision: `docs/ABASTECIMIENTO_RESUMEN_REVISION.md`
- Backend Java: `backend/src/main/java/com/pasteleria/abastecimiento/`
- Frontend Angular: `frontend-admin-angular/src/app/features/abastecimiento/`
- Seed BD: `db/V1/DATABASE_SEED_CANONICO.sql`

### Tips

1. Siempre usar `BigDecimal` para dinero y cantidades de precision.
2. Nunca exponer entities en la API; siempre usar DTOs.
3. Validar en multiples capas: frontend, controller y service.
4. Usar `Optional` para valores que pueden no existir.
5. Mantener metodos pequenos y con responsabilidad clara.
6. Preferir nombres descriptivos sobre abreviaturas ambiguas.
7. Comentar el por que, no el que.

---

**Version**: 1.1  
**Actualizado**: Marzo 2026
