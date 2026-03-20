# 01 - Modelo de dominios, agregados y casos de uso

## 1. Proposito

Este documento aterriza el backend de Pasteleria a nivel de dominio y casos de uso.

La intencion no es hacer DDD ceremonial, sino dejar claro:

- que agregados existen
- donde viven las invariantes
- que servicios de aplicacion se necesitan
- y donde estan los puntos transaccionales del sistema

---

## 2. Agregados principales

### AG-01. Cliente

- raiz: `Cliente`
- responsabilidad: identidad comercial y datos de contacto
- invariantes: datos minimos validos, telefono y correo con formato coherente si se informan

### AG-02. Producto

- raiz: `Producto`
- entidades relacionadas: `CategoriaProducto`
- invariantes: nombre obligatorio, categoria valida, precio base no negativo

### AG-03. Cotizacion

- raiz: `Cotizacion`
- entidades internas: `CotizacionDetalle`
- invariantes:
  - pertenece a un cliente
  - tiene estado controlado
  - no puede convertirse dos veces

### AG-04. Pedido

- raiz: `Pedido`
- entidades internas: `PedidoDetalle`
- invariantes:
  - pertenece a un cliente
  - tiene al menos un detalle
  - no puede entregarse desde estado incoherente

### AG-05. Produccion

- raiz: `Produccion`
- invariantes:
  - pertenece a un pedido
  - su estado debe ser coherente con el flujo operativo

### AG-06. Usuario

- raiz: `UsuarioSistema`
- entidades relacionadas: `RolUsuario`
- invariantes:
  - usuario unico por login
  - password siempre persistida como hash
  - rol obligatorio

### AG-07. SolicitudReporte

- raiz: `SolicitudReporte`
- invariantes:
  - estado controlado
  - parametros serializables
  - ruta de archivo solo cuando el reporte esta listo

---

## 3. Casos de uso principales por modulo

### Clientes

- registrar cliente
- actualizar cliente
- buscar cliente
- consultar historial resumido

### Productos

- crear producto
- editar producto
- activar o desactivar producto
- listar catalogo publico
- listar catalogo interno con filtros

### Cotizaciones

- registrar cotizacion
- consultar cotizacion
- aprobar o rechazar
- convertir a pedido

### Pedidos

- registrar pedido
- consultar detalle
- listar pedidos con filtros
- cambiar estado
- marcar entrega

### Produccion

- listar cola operativa
- iniciar produccion
- actualizar avance
- finalizar produccion
- emitir actualizacion SSE

### Usuarios y auth

- iniciar sesion
- crear usuario
- cambiar rol
- activar o desactivar usuario

### Reportes

- solicitar reporte
- reclamar trabajo pendiente
- completar reporte
- consultar estado
- descargar resultado

---

## 4. Servicios de aplicacion sugeridos

- `ClienteApplicationService`
- `ProductoApplicationService`
- `CotizacionApplicationService`
- `PedidoApplicationService`
- `ProduccionApplicationService`
- `UsuarioApplicationService`
- `AuthApplicationService`
- `ReporteApplicationService`

Regla:

- un servicio de aplicacion representa casos de uso
- no debe convertirse en un cajon de sastre sin limites de modulo

---

## 5. Transacciones recomendadas

Conviene usar `@Transactional` al menos en:

- crear pedido
- convertir cotizacion en pedido
- cambiar estado critico de pedido
- actualizar produccion
- crear solicitud de reporte
- reclamar y cerrar trabajo de reporte

---

## 6. Eventos internos utiles

Sin usar broker externo en V1, conviene manejar el concepto de eventos internos:

- `CotizacionConvertida`
- `PedidoRegistrado`
- `EstadoPedidoActualizado`
- `ProduccionActualizada`
- `ReporteSolicitado`
- `ReporteGenerado`

Sirven para desacoplar:

- auditoria
- notificaciones internas
- refresco SSE
- trazabilidad operativa

---

## 7. Regla practica de modelado

Las entidades pueden contener:

- invariantes simples
- metodos con intencion
- transiciones de estado controladas

La orquestacion de varios agregados debe vivir en `application`, no en controladores.

