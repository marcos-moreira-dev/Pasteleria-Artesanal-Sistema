# 01 - Frontend admin: shell, rutas y modulos

## 1. Proposito

Este documento fija la forma correcta de navegar y dividir el admin de
Pasteleria segun la aplicacion actual.

---

## 2. Shell recomendado

El shell administrativo debe incluir:

- barra superior sobria
- menu lateral
- area central de contenido
- acciones contextuales por modulo

Regla:

- el shell no debe saturarse con widgets sin valor operativo

---

## 3. Mapa de rutas real de la V1

Las rutas visibles hoy son:

- `/login`
- `/`
- `/clientes`
- `/productos`
- `/cotizaciones`
- `/pedidos`
- `/produccion`
- `/reportes`
- `/abastecimiento/dashboard`
- `/abastecimiento/inventario`
- `/abastecimiento/compras`
- `/abastecimiento/proveedores`
- `/abastecimiento/movimientos`

No existe hoy un prefijo `/app` en el routing real del admin.

---

## 4. Modulos funcionales minimos

### Dashboard

Resumen ligero del estado operativo.

### Clientes

Listado, alta, edicion y consulta.

### Productos

Mantenimiento del catalogo operativo y sus imagenes.

### Cotizaciones

Seguimiento y posible conversion a pedido.

### Pedidos

Registro, consulta y cambio de estado.

### Produccion

Vista operativa de pedidos por atender y prioridad.

### Reportes

Solicitud, seguimiento y descarga de reportes internos.

### Abastecimiento

Submodulo con varias superficies:

- dashboard
- inventario
- compras
- proveedores
- movimientos

---

## 5. Navegacion por rol

La V1 puede empezar con un modelo moderado:

- administrador
- operador
- produccion

Regla:

- la UI puede ocultar opciones no permitidas
- la autorizacion real siempre la decide el backend

---

## 6. Cierre

Si el shell y las rutas se documentan con fidelidad al codigo real, el admin se
vuelve mas legible, mas mantenible y mas facil de explicar en demos.
