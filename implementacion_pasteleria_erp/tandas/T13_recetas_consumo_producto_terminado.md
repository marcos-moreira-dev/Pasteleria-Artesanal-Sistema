# T13 — Recetas técnicas, consumo y producto terminado

## 1. Objetivo

Conectar la producción con recetas técnicas, consumo de ingredientes, lotes documentales y entrada de producto terminado sin romper la UX/UI actual ni intentar cerrar todavía la contabilidad completa.

Esta tanda convierte la finalización de producción en un hecho operativo más serio:

```text
producción FINALIZADA
→ receta activa por producto, si existe
→ consumo de ingredientes
→ movimiento de inventario SALIDA_PRODUCCION
→ lote de producción
→ entrada documental de producto terminado
```

## 2. Contexto heredado

Hasta T12 el sistema protegía los estados de pedido y producción mediante máquinas de estado. Sin embargo, una producción podía finalizarse sin dejar rastro técnico de materiales consumidos, lotes generados o entrada de producto terminado.

Cedro enseñó el patrón:

```text
hecho operativo confirmado
→ inventario/caja/documento
→ auditoría
→ ERP futuro
```

En esta tanda se aplica ese patrón a producción.

## 3. Fuente Cedro usada como referencia

Se rescata de Cedro:

- producción conectada a receta;
- consumo que descuenta inventario;
- lote/producto terminado como evidencia operativa;
- trazabilidad por referencia tipo/id;
- auditoría al generar consecuencias.

No se copia:

- dominio restaurante;
- cocina como nombre visible;
- costeo industrial completo;
- contabilidad automática.

## 4. Estado actual de Pastelería

La pastelería ya tenía:

- `receta`;
- `detalle_receta`;
- `produccion`;
- `inventario_movimiento`;
- `ingrediente.stock_actual`;
- `insumo.stock_actual`;
- movimientos con saldo anterior/posterior desde T10.

Faltaba:

- consumo material por producción;
- lotes generados;
- entrada documental de producto terminado;
- endpoints de consulta de estos artefactos;
- validación SQL específica.

## 5. Alcance

Se implementa:

- `consumo_material_produccion`;
- `lote_produccion`;
- `entrada_producto_terminado`;
- entidades JPA correspondientes;
- repositorios/puertos;
- `ProductionMaterialPolicy`;
- `ProductionMaterialService`;
- endpoints de consulta;
- validación SQL;
- contratos API;
- tipos TypeScript.

## 6. Fuera de alcance

No se implementa todavía:

- contabilidad automática;
- costeo industrial completo;
- mano de obra;
- costos indirectos;
- stock formal de producto terminado;
- unidades de conversión complejas;
- merma operativa completa;
- UI nueva para lotes/consumos.

## 7. Decisiones de diseño

### 7.1 Receta activa opcional transicional

Si un producto tiene receta activa, se generan consumos automáticos.

Si un producto no tiene receta activa, se genera lote documental sin consumo automático. Esta decisión evita romper operaciones actuales mientras se completa el catálogo de recetas técnicas.

### 7.2 Producto terminado documental

El sistema crea `lote_produccion` y `entrada_producto_terminado`, pero todavía no incrementa stock formal de producto terminado porque el modelo V1 no tiene inventario de productos terminados.

### 7.3 Consumo real mediante inventario existente

El consumo de ingredientes sí usa `InventarioMovimientoCommandService` con:

```text
itemTipo = INGREDIENTE
tipoMovimiento = SALIDA_PRODUCCION
referenciaTipo = PRODUCCION
referenciaId = produccion_id
```

### 7.4 Idempotencia básica

Si una producción ya tiene consumos o lotes, no se generan duplicados.

## 8. Criterios de aceptación

La tanda se considera terminada si:

- finalizar producción genera consumos si hay receta activa;
- cada consumo tiene movimiento de inventario;
- finalizar producción genera lote por detalle de pedido;
- finalizar producción genera entrada documental de producto terminado;
- no se generan duplicados si se consulta o reintenta dentro del flujo normal;
- la UX/UI no se rediseña;
- los endpoints nuevos están en contratos API;
- existen validaciones SQL.

## 9. Pruebas mínimas

Backend:

```bat
scripts\test-backend.bat
```

Prueba manual:

```http
PATCH /api/v1/produccion/{id}/estado
{
  "status": "FINALIZADO",
  "reason": "Producción terminada"
}

GET /api/v1/produccion/{id}/consumos
GET /api/v1/produccion/{id}/lotes
GET /api/v1/produccion/{id}/entradas-producto-terminado
```

## 10. Riesgos

- Productos sin receta activa generan solo lote documental.
- No hay todavía stock formal de producto terminado.
- Las unidades de medida se asumen compatibles con la receta actual.
- La contabilidad queda para T18/T19.

## 11. Nota para la siguiente tanda

La siguiente tanda es T14 — Compras: orden, recepción, documento y cuenta por pagar.

T14 debe conectar compras con inventario de forma más formal y preparar la relación con cuentas por pagar.
