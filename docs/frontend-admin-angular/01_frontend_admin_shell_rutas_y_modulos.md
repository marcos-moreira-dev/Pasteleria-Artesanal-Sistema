# 01 - Frontend admin: shell, rutas y modulos

## 1. Proposito

Este documento fija la forma de navegar y dividir el admin de Pasteleria.

---

## 2. Shell recomendado

El shell administrativo debe incluir:

- barra superior sobria
- menu lateral
- area central de contenido
- breadcrumb simple si aporta
- zona de acciones contextuales por modulo

Regla:

- el shell no debe saturarse con widgets sin valor operativo

---

## 3. Mapa de rutas sugerido

Rutas base razonables:

- `/login`
- `/app`
- `/app/dashboard`
- `/app/clientes`
- `/app/productos`
- `/app/categorias`
- `/app/cotizaciones`
- `/app/pedidos`
- `/app/produccion`
- `/app/reportes`
- `/app/configuracion` solo si el alcance lo justifica

---

## 4. Modulos funcionales minimos

### Dashboard

Resumen ligero de estado operativo.

### Clientes

Listado, alta, edicion y consulta.

### Productos y categorias

Mantenimiento de catalogo operativo.

### Cotizaciones

Seguimiento, cambio de estado y posible conversion a pedido.

### Pedidos

Registro, consulta, detalle y cambio de estado.

### Produccion

Vista operativa de pedidos por atender y prioridad.

### Reportes

Solicitud, seguimiento y descarga de reportes internos.

---

## 5. Lazy loading y modularidad

Conviene aplicar lazy loading en:

- cotizaciones
- pedidos
- produccion
- reportes

En modulos pequenos, la prioridad sigue siendo claridad antes que micro-optimizar.

---

## 6. Navegacion por rol

La V1 puede empezar con un modelo moderado:

- administrador
- operador
- produccion

Regla:

- la UI puede ocultar opciones no permitidas
- la autorizacion real siempre la decide el backend

---

## 7. Cierre

Si el shell y las rutas se congelan bien desde ahora, toda la implementacion del admin se vuelve mas predecible y menos propensa a crecer de forma caotica.
