# 03 - Frontend admin: tablas, formularios, produccion y reportes

## 1. Proposito

Este documento fija los patrones UX y de implementacion para las pantallas mas operativas del admin.

---

## 2. Tablas operativas

Toda tabla de operacion debe resolver:

- filtros claros
- ordenamiento cuando aporte
- paginacion estable
- acciones visibles
- estados vacio, carga y error

Patron recomendado:

- filtros arriba
- tabla al centro
- acciones por fila a la derecha
- resumen o totales solo si aportan

---

## 3. Formularios

Los formularios deben ser:

- reactivos
- agrupados por secciones
- claros en obligatorios y opcionales
- coherentes con las validaciones del backend

Evitar:

- formularios gigantes sin agrupacion
- errores solo al final
- labels ambiguos

---

## 4. Modales y drawers

Uso correcto:

- acciones pequenas o confirmaciones
- detalle rapido
- edicion corta

No usar para:

- flujos complejos de varias secciones
- pantallas que merecen ruta propia

---

## 5. Panel de produccion

Este modulo debe privilegiar:

- prioridad visible
- estado operativo
- fecha de entrega
- observaciones cortas
- acciones directas de cambio de estado

El patron puede ser:

- tablero por estado
- o lista operativa con filtros

La eleccion final debe seguir el volumen real de trabajo.

---

## 6. Reportes

El modulo de reportes debe contemplar:

- formulario de solicitud
- estado del reporte
- fecha de generacion
- disponibilidad de descarga

Como el backend ya contempla reportes asincronos, la UI debe mostrar:

- pendiente
- procesando
- listo
- error

---

## 7. Accesibilidad operativa

Incluso siendo backoffice, debe cuidarse:

- foco visible
- atajos o navegacion consistente
- contraste correcto
- mensajes comprensibles

---

## 8. Pruebas superficiales sugeridas

1. Alta y edicion de cliente.
2. Alta y edicion de producto.
3. Cambio de estado de cotizacion.
4. Cambio de estado de pedido.
5. Consulta del panel de produccion.
6. Solicitud y descarga de reporte.

---

## 9. Cierre

Tablas, formularios, produccion y reportes son el corazon operativo del admin. Si sus patrones quedan estables, el resto del frontend crece con mucha menos friccion.
