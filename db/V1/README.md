# db/V1 — Base de Datos Pastelería

## 🎯 Archivo Recomendado

**`DATABASE_MASTER_COMPLETE.sql`** ← Usar este archivo

Script único que incluye:

- ✅ Esquema completo (27 tablas)
- ✅ Datos de ejemplo (seeds)
- ✅ Comentarios educativos extensivos
- ✅ Recetas JSON en productos

### Uso Rápido

```sql
-- 1. Crear base de datos
DROP DATABASE IF EXISTS pasteleria;
CREATE DATABASE pasteleria;

-- 2. Ejecutar script maestro
\i db/V1/DATABASE_MASTER_COMPLETE.sql

-- 3. Verificar
SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';
-- Debe retornar: 27
```

---

## 📂 Contenido de la Carpeta

```
db/V1/
├── DATABASE_MASTER_COMPLETE.sql   ← **USAR ESTE (todo en uno)**
│
├── docs/
│   └── Diagramas y query de creacion/
│       └── V1_3FN.sql              # DDL original (referencia)
│
├── seeds/                          # Seeds individuales (opcional)
│   ├── 01_seed_base.sql
│   ├── 02_seed_demo.sql
│   ├── 03_seed_enterprise.sql
│   └── 04_seed_abastecimiento.sql
│
└── tools/                          # Scripts utilitarios
    ├── 00_database_bootstrap.sql
    └── 99_reset_demo.sql
```

---

## ⚡ Opciones de Instalación

### Opción A: Script Maestro (Recomendado)

Un solo archivo, todo incluido:

```bash
psql -U postgres -d pasteleria -f db/V1/DATABASE_MASTER_COMPLETE.sql
```

**Ventajas:**

- Un solo paso
- Comentarios educativos extensos
- Datos de ejemplo con recetas JSON reales
- Sin dependencias entre archivos

### Opción B: Archivos Individuales (Legacy)

Si prefieres control granular:

```sql
-- 1. Crear BD
\i db/V1/tools/00_database_bootstrap.sql

-- 2. Conectar
\c pasteleria

-- 3. Crear tablas
\i db/V1/docs/Diagramas\ y\ query\ de\ creacion/V1_3FN.sql

-- 4. Poblar datos
\i db/V1/seeds/01_seed_base.sql
\i db/V1/seeds/02_seed_demo.sql
\i db/V1/seeds/03_seed_enterprise.sql
\i db/V1/seeds/04_seed_abastecimiento.sql
```

---

## 📊 Datos Incluidos (Script Maestro)

| Entidad            | Cantidad | Notas                                                            |
| ------------------ | -------- | ---------------------------------------------------------------- |
| Roles              | 3        | ADMIN, ATENCION, PRODUCCION                                      |
| Usuarios           | 3        | admin/admin123, atencion1/atencion123, produccion1/produccion123 |
| Categorías         | 4        | TORTAS, POSTRES, GALLETAS, BEBIDAS                               |
| Productos          | 5        | Con recetas JSON completas                                       |
| Clientes           | 3        | Datos de ejemplo                                                 |
| Unidades de medida | 10       | g, kg, ml, L, u, etc.                                            |
| Ingredientes       | 20       | Stock realista                                                   |
| Insumos            | 15       | Cajas, moldes, decoraciones                                      |
| Proveedores        | 8        | Con contactos                                                    |
| Órdenes de compra  | 10       | En diferentes estados                                            |

---

## 🏗️ Esquema de Tablas

```
Seguridad:
  rol_usuario → usuario_sistema

Catálogo:
  categoria_producto → producto (con receta_json!)

Operaciones:
  cliente → cotizacion → cotizacion_detalle
  cliente → pedido → pedido_detalle → produccion

Abastecimiento:
  umedida → ingrediente/insumo
  proveedor → item_proveedor/orden_compra
  inventario_movimiento (kardex)
  receta → detalle_receta

Producción:
  produccion → orden_produccion → orden_produccion_detalle → consumo_produccion

Soporte:
  archivo_recurso, job_reporte, notificacion, auditoria_evento
```

---

## ⚠️ Notas Importantes

### No usar Flyway

Las migraciones en `backend/src/main/resources/db/migration/` **no deben ejecutarse** si usas el script maestro.

En `application.yml`:

```yaml
spring:
  flyway:
    enabled: false # ← Importante!
  jpa:
    hibernate:
      ddl-auto: validate # ← Solo validar, no crear
```

### Regeneración Limpia

Si necesitas empezar de cero:

```sql
DROP DATABASE pasteleria;
CREATE DATABASE pasteleria;
-- Volver a ejecutar DATABASE_MASTER_COMPLETE.sql
```

---

## 🔧 Relación con Flyway (Referencia)

Las migraciones del backend corresponden a:

| Flyway                         | Archivo db/V1/                   |
| ------------------------------ | -------------------------------- |
| V1\_\_init_schema.sql          | docs/Diagramas.../V1_3FN.sql     |
| V2\_\_seed_base.sql            | seeds/01_seed_base.sql           |
| V3\_\_seed_demo.sql            | seeds/02_seed_demo.sql           |
| V4\_\_seed_enterprise_demo.sql | seeds/03_seed_enterprise.sql     |
| V9\_\_abastecimiento...        | seeds/04_seed_abastecimiento.sql |

**Nota:** El script `DATABASE_MASTER_COMPLETE.sql` ya incluye todo lo anterior consolidado.

---

## 🎓 Para Estudiar

El script maestro incluye comentarios extensivos que explican:

- Por qué cada tabla existe
- Tipos de datos y por qué se eligen
- Constraints y reglas de negocio
- Foreign keys e integridad referencial
- Uso de JSONB para recetas estructuradas
- Índices para optimización
- Seeds con datos realistas

**Todo en español y paso a paso.**
