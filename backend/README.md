# Backend Pastelería

Backend central en `Spring Boot 4` para catálogo, clientes, cotizaciones, pedidos, producción, abastecimiento, reportes, notificaciones, guía operativa y assets públicos del negocio.

## Qué ya cubre

- Seguridad con JWT.
- Contrato `ApiResponse<T>` uniforme.
- PostgreSQL con migraciones Flyway y SQL canónico para presentación local.
- Módulos de negocio iniciales y vertical de abastecimiento.
- Reportes asíncronos, archivos generados y notificaciones internas.
- Assets estáticos servidos desde storage externo.
- Guía operativa consultiva para explicar procedimientos internos al equipo.

## Módulos actuales

- `auth`
- `usuarios`
- `clientes`
- `catalogos`
- `productos`
- `cotizaciones`
- `pedidos`
- `produccion`
- `abastecimiento`
- `reportes`
- `notificaciones`
- `casosuso` — expuesto al usuario como **Guía operativa**
- `common`

## Endpoints destacados

- `POST /api/v1/auth/login`
- `GET /api/v1/public/catalogo/productos`
- `GET /api/v1/public/catalogo/categorias`
- `GET /api/v1/public/catalogo/branding`
- `GET /api/v1/clientes/paginado`
- `GET /api/v1/productos/paginado`
- `GET /api/v1/cotizaciones/paginado`
- `GET /api/v1/pedidos/paginado`
- `GET /api/v1/produccion/paginado`
- `GET /api/v1/reportes/paginado`
- `GET /api/v1/notificaciones/resumen`
- `GET /api/v1/abastecimiento/dashboard`
- `GET /api/v1/casos-uso/hub`
- `GET /api/v1/casos-uso/{codigo}`

## Assets estáticos

Las imágenes del catálogo, branding y fallbacks viven en `storage/assets/` fuera del JAR.

```text
backend/
├── storage/
│   └── assets/
│       ├── products/       ← imágenes de productos por slug
│       ├── branding/       ← logo y banner
│       └── placeholders/   ← imagen de respaldo cuando falta foto dedicada
├── src/
└── target/
    └── pasteleria-backend.jar
```

URLs públicas esperadas:

```text
GET /assets/products/torta-chocolate-mediana.png
GET /assets/branding/logo-cuadrado.png
GET /assets/placeholders/product-placeholder.png
```

## Base de datos

Hay dos caminos controlados:

1. **Presentación local:** usar `scripts\run-demo.bat`. Este comando recrea la base local, aplica la línea SQL actual del backend y carga datos inventados de presentación/SIT. En este modo debe usarse `SPRING_FLYWAY_ENABLED=false`.
2. **Validación de migraciones:** Flyway aplica `backend/src/main/resources/db/migration/*.sql` sobre una base limpia.

No mezclar ambos caminos en la misma base sin hacer `reset-db-local.ps1`.

## Comandos principales

```powershell
.\scripts\start-backend-dev.cmd
.\mvnw.cmd test
.\mvnw.cmd -DskipTests package
```

> Ejecutar desde la carpeta `backend/` para que `./storage/` se resuelva correctamente.

## Nota de entorno

El backend debe ejecutarse con Java 21. El script `start-backend-dev.cmd` fija variables locales de desarrollo como `DB_PORT`, `JWT_SECRET` y `SPRING_FLYWAY_ENABLED=false`.
