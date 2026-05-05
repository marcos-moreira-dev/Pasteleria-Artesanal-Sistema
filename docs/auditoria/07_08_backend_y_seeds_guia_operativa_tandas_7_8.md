# Tandas 7 y 8 — Backend y seeds de guía operativa

## Objetivo aplicado

Se incorporó el módulo backend de **Guía operativa** para que el sistema pueda consultar flujos de trabajo reales de la pastelería desde endpoints internos, sin mezclar esta guía con ejecución automática de pedidos o producción.

## Archivos backend creados

```text
backend/src/main/java/com/pasteleria/casosuso/
  api/CasoUsoOperativoController.java
  api/dto/CasoUsoHubResponse.java
  api/dto/CasoUsoModuloResponse.java
  api/dto/CasoUsoOperativoResponse.java
  api/dto/PasoCasoUsoResponse.java
  application/CasoUsoOperativoService.java
  infrastructure/persistence/entity/CasoUsoModuloEntity.java
  infrastructure/persistence/entity/CasoUsoOperativoEntity.java
  infrastructure/persistence/entity/PasoCasoUsoEntity.java
  infrastructure/persistence/repository/CasoUsoModuloRepository.java
  infrastructure/persistence/repository/CasoUsoOperativoRepository.java
  infrastructure/persistence/repository/PasoCasoUsoRepository.java
```

## Endpoints disponibles

```text
GET /api/v1/casos-uso
GET /api/v1/casos-uso?modulo=PEDIDOS
GET /api/v1/casos-uso/hub
GET /api/v1/casos-uso/{codigo}
```

Estos endpoints devuelven `ApiResponse.success` según el contrato actual de Pastelería.

## Base de datos

Se agregó la migración:

```text
backend/src/main/resources/db/migration/V13__guia_operativa.sql
```

Tablas nuevas:

```text
caso_uso_modulo
caso_uso_operativo
paso_caso_uso
```

También se actualizó el camino canónico local:

```text
db/V1/DATABASE_SCHEMA_CANONICO.sql
db/V1/DATABASE_SEED_CANONICO.sql
```

## Datos sembrados

Se sembraron 14 áreas operativas:

1. Resumen del día
2. Clientes
3. Catálogo de productos
4. Cotizaciones
5. Pedidos
6. Producción
7. Decoración y empaque
8. Abastecimiento
9. Recetas técnicas
10. Proveedores
11. Reportes
12. Notificaciones
13. Usuarios y permisos
14. Guía operativa

Cada área incluye un flujo principal con pasos redactados para operación real de pastelería: vitrina, encargo, torta, relleno, decoración, empaque, retiro, entrega, mesa dulce, insumos, merma, receta, proveedor y mostrador.

## Decisiones técnicas

- Se usaron IDs `Long` para mantener coherencia con Pastelería.
- Las entidades extienden `AuditableEntity` e incluyen columna técnica `version` para JPA.
- La versión visible del flujo se guardó como `version_flujo` para no chocar con `@Version`.
- El módulo es consultivo: no muta pedidos, producción ni inventario.

## Siguiente tanda

La Tanda 9 fue aplicada posteriormente: pantalla Angular de guía operativa, ruta `/guia-operativa` e integración con backend.
