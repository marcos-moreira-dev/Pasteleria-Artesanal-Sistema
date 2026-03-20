# /base-datos/07_datos_semilla_y_escenarios_prueba.md

# 07 — Datos semilla y escenarios de prueba

## 1. Propósito del documento

Este documento define la estrategia para preparar **datos semilla** y **escenarios de prueba controlados** que permitan validar el sistema en ambiente local y de desarrollo.

Su propósito no es modelar datos productivos reales, sino proporcionar una base mínima, coherente y reutilizable para:

- probar migraciones
- probar el backend
- probar los frontends
- realizar demostraciones del sistema
- validar consultas, filtros y dashboards

Este documento complementa la estrategia de migraciones y el esquema SQL inicial definidos en documentos anteriores.

---

## 2. Diferencia entre datos semilla y datos de prueba

### 2.1 Datos semilla

Son datos mínimos que el sistema necesita para funcionar de manera razonable desde el primer arranque.

Ejemplos:

- roles de usuario
- categorías de producto
- algunos productos base

### 2.2 Datos de prueba o demo

Son datos adicionales orientados a validación funcional, demostraciones o desarrollo de frontend.

Ejemplos:

- clientes ficticios
- pedidos en distintos estados
- cotizaciones de ejemplo
- registros de producción

---

## 3. Objetivos de la estrategia de seeds

La estrategia de seeds debe permitir:

- levantar el sistema sin cargar datos manualmente desde cero
- disponer de un catálogo mínimo estable
- simular escenarios operativos del negocio
- probar reglas de integridad y transiciones de estado
- facilitar el trabajo simultáneo de backend y frontend

---

## 4. Clasificación recomendada de seeds

Se recomienda dividir los datos semilla en dos niveles.

### 4.1 Seed base

Contiene datos relativamente estables y necesarios para el funcionamiento inicial del sistema.

Incluye:

- roles
- categorías de producto
- productos base

### 4.2 Seed demo

Contiene datos orientados a desarrollo, pruebas visuales y validación funcional.

Incluye:

- clientes ficticios
- pedidos
- cotizaciones
- producción
- usuarios de ejemplo

Esta separación permite reiniciar o regenerar la información demo sin afectar el núcleo base del sistema.

---

## 5. Datos semilla mínimos por tabla

### 5.1 Tabla: rol_usuario

Datos mínimos sugeridos:

- `ADMINISTRADOR`
- `ATENCION`
- `PRODUCCION`

Justificación:

- permiten probar autenticación
- permiten validar autorización por rol
- son necesarios para usuarios del sistema

---

### 5.2 Tabla: categoria_producto

Datos mínimos sugeridos:

- `TORTAS`
- `POSTRES`
- `GALLETAS`
- `BEBIDAS`

Justificación:

- permiten clasificar productos
- habilitan catálogo mínimo del negocio

---

### 5.3 Tabla: producto

Datos mínimos sugeridos:

- Torta de chocolate mediana
- Torta tres leches pequeña
- Brownie individual
- Galleta decorada
- Café americano

Campos importantes a poblar:

- categoría
- nombre
- descripción
- precio base
- activo

Justificación:

- permiten crear pedidos y cotizaciones
- sirven para catálogo del frontend público

---

### 5.4 Tabla: usuario_sistema

Usuarios de ejemplo sugeridos:

- `admin`
- `atencion1`
- `produccion1`

Cada uno asociado a su rol correspondiente.

Justificación:

- permiten probar login
- permiten validar visibilidad por perfil

---

### 5.5 Tabla: cliente

Clientes demo sugeridos:

- María López
- Juan Pérez
- Andrea Gómez
- Carlos Vera

Campos sugeridos:

- nombre
- teléfono
- correo
- observaciones

Justificación:

- permiten probar historial
- permiten probar pedidos por cliente
- permiten probar cotizaciones por cliente

---

### 5.6 Tabla: cotizacion

Cotizaciones demo sugeridas:

- una `PENDIENTE`
- una `APROBADA`
- una `RECHAZADA`
- una `CONVERTIDA`

Justificación:

- permiten probar el flujo comercial
- permiten validar filtros por estado

---

### 5.7 Tabla: pedido

Pedidos demo sugeridos:

- un pedido `REGISTRADO`
- un pedido `EN_PREPARACION`
- un pedido `LISTO`
- un pedido `ENTREGADO`
- un pedido `CANCELADO`

Justificación:

- permiten probar dashboard y paneles
- permiten validar transiciones de estado
- permiten probar consultas operativas

---

### 5.8 Tabla: pedido_detalle

Se recomienda que los pedidos demo tengan detalles variados, por ejemplo:

- un pedido con un solo producto
- un pedido con varios productos
- un pedido con combinación de torta y adicionales

Justificación:

- permiten validar cálculo de subtotal y total
- permiten probar detalle de pedido en frontend

---

### 5.9 Tabla: produccion

Registros demo sugeridos:

- una producción `PENDIENTE`
- una producción `EN_PROCESO`
- una producción `FINALIZADO`

Justificación:

- permiten probar panel de producción
- permiten probar filtros SSE y visualización operativa

---

## 6. Estrategia de carga recomendada

### 6.1 Seeds versionados con migraciones

Se recomienda usar migraciones para datos base relativamente estables.

Ejemplos:

- roles
- categorías
- productos iniciales mínimos

Ejemplos de archivos:

```text
V2__seed_roles.sql
V3__seed_categorias.sql
V4__seed_productos_base.sql
```

### 6.2 Scripts auxiliares para demo

Para datos de demostración o desarrollo intensivo, puede usarse una capa adicional fuera del flujo principal de migraciones.

Ejemplos:

```text
scripts/seed_demo_local.sql
scripts/reset_demo_data.sql
```

Esto evita inflar las migraciones versionadas con demasiados datos temporales.

---

## 7. Escenarios de prueba funcionales mínimos

A continuación se definen escenarios base que deben poder ejecutarse con los seeds cargados.

### 7.1 Escenario: registrar cliente

Objetivo:

- validar creación correcta en `cliente`
- validar campos requeridos

Resultado esperado:

- se genera `cliente_id`
- el cliente queda disponible para pedidos y cotizaciones

---

### 7.2 Escenario: registrar pedido

Objetivo:

- validar relación entre `cliente`, `pedido` y `pedido_detalle`
- validar subtotales y total estimado

Resultado esperado:

- pedido creado correctamente
- detalles asociados correctamente

---

### 7.3 Escenario: listar pedidos pendientes

Objetivo:

- validar filtros por estado
- validar orden por fecha de entrega

Resultado esperado:

- se listan pedidos `REGISTRADO`, `EN_PREPARACION` y `LISTO`
- no aparecen `ENTREGADO` si el filtro es pendiente

---

### 7.4 Escenario: convertir cotización en pedido

Objetivo:

- validar transición de cotización
- validar creación de pedido derivado

Resultado esperado:

- cotización pasa a `CONVERTIDA`
- pedido nuevo creado correctamente

---

### 7.5 Escenario: actualizar producción

Objetivo:

- validar transición `PENDIENTE -> EN_PROCESO -> FINALIZADO`

Resultado esperado:

- producción cambia correctamente de estado
- el panel de producción refleja el cambio

---

### 7.6 Escenario: validar restricciones de base de datos

Objetivo:

- comprobar que los constraints realmente protegen el modelo

Casos mínimos:

- no permitir `cantidad <= 0`
- no permitir `precio_base < 0`
- no permitir `fecha_entrega < fecha_pedido`
- no permitir claves foráneas inexistentes

Resultado esperado:

- la base de datos rechaza operaciones inválidas

---

## 8. Escenarios de prueba para frontend

Los seeds deben soportar también validaciones de interfaz.

### Frontend administrativo

Casos sugeridos:

- tabla de clientes con datos visibles
- listado de pedidos por estado
- formulario de alta de pedido
- panel de producción con estados variados
- dashboard con métricas mínimas

### Frontend público

Casos sugeridos:

- catálogo con productos activos
- cotizador con categorías y productos base
- formularios de solicitud con datos realistas

---

## 9. Recomendaciones prácticas para este proyecto

Para este proyecto de práctica, se recomienda mantener el siguiente enfoque:

### Nivel 1 — mínimo obligatorio

- roles
- categorías
- productos base
- usuario administrador

### Nivel 2 — demo operativa

- varios clientes
- pedidos en estados variados
- cotizaciones en estados variados
- registros de producción
- usuarios por rol

### Nivel 3 — pruebas más ricas

- pedidos con múltiples detalles
- cotizaciones complejas
- datos suficientes para dashboard y paginación

---

## 10. Relación con otros documentos

Este documento se relaciona directamente con:

- `02_diccionario_de_datos.md`
- `03_reglas_de_integridad_y_constraints.md`
- `04_catalogos_enums_y_estados.md`
- `06_convenciones_nombres_y_migraciones.md`

Los datos semilla deben respetar las reglas de integridad, catálogos y convenciones definidas en esos documentos.

---

## 11. Conclusión

La definición explícita de datos semilla y escenarios de prueba permite iniciar el sistema con una base coherente y acelera el desarrollo de backend y frontend.

Este documento sirve como referencia para construir seeds reproducibles, validar flujos principales del negocio y preparar ambientes de demostración y prueba de manera ordenada.

