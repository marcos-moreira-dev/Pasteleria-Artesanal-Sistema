# 00 - Mapa de productos de software y alcance global

## 1. Proposito del documento

Este documento responde una pregunta practica:

**que productos de software existen en Pasteleria y que hace realmente cada uno.**

Su objetivo es que una persona nueva no tenga que deducir el sistema leyendo
casos de uso, backend y frontends por separado.

---

## 2. Problema que resuelve el sistema

Pasteleria no es solo una landing con catalogo ni solo un panel CRUD.

El sistema completo busca cubrir el ciclo operativo de una pasteleria artesanal:

- atraer interes comercial desde una vitrina publica
- registrar solicitudes de cotizacion
- administrar clientes y catalogo
- convertir interes comercial en pedidos
- dar seguimiento a produccion
- controlar abastecimiento e inventario
- generar reportes y trazabilidad operativa

---

## 3. Productos de software del sistema

### 3.1 Base de datos PostgreSQL

Es la fuente de verdad transaccional.

Guarda:

- usuarios y roles
- clientes
- categorias y productos
- cotizaciones y pedidos
- produccion
- ingredientes, insumos y recetas
- proveedores, ordenes de compra y movimientos
- archivos, reportes, notificaciones y auditoria

### 3.2 Backend Spring Boot

Es el nucleo tecnico del sistema.

Se encarga de:

- seguridad y login
- validaciones de negocio
- contratos REST
- transiciones de estado
- persistencia y trazabilidad
- reportes, notificaciones y assets publicos

### 3.3 Frontend publico en Astro

Es la vitrina comercial del negocio.

Su funcion es:

- presentar marca
- mostrar catalogo publicado
- guiar al cliente hacia contacto
- registrar una solicitud publica de cotizacion

### 3.4 Frontend administrativo en Angular

Es la consola de trabajo interna.

Permite operar:

- clientes
- productos
- cotizaciones
- pedidos
- produccion
- reportes
- abastecimiento

### 3.5 Panel de produccion

No es un producto separado en esta V1.

Es una superficie operativa dentro del admin para:

- ver cola de trabajo
- actualizar estados
- priorizar pendientes

### 3.6 Vertical de abastecimiento

Tampoco es un producto aislado.

Es una capacidad transversal entre DB, backend y admin para:

- controlar inventario
- administrar proveedores
- mantener recetas
- emitir y recibir ordenes de compra
- trazar movimientos de stock

---

## 4. Que hace el sistema hoy

La foto honesta del producto actual es esta:

1. Publica branding y catalogo desde el backend para la vitrina web.
2. Permite enviar solicitudes publicas de cotizacion desde la pagina de contacto.
3. Administra clientes internos con busqueda, altas y edicion.
4. Administra productos del catalogo y sus imagenes.
5. Gestiona cotizaciones internas y su conversion a pedido.
6. Registra pedidos y cambios de estado.
7. Muestra y actualiza la cola de produccion.
8. Solicita, lista y descarga reportes.
9. Gestiona notificaciones internas.
10. Opera abastecimiento con dashboard, inventario, compras, proveedores y movimientos.

---

## 5. Superficies visibles por producto

### 5.1 Frontend publico

Rutas visibles hoy:

- `/`
- `/catalogo`
- `/contacto`
- `/en`
- `/en/catalog`
- `/en/contact`

Lectura correcta:

- la cotizacion publica vive hoy en la pagina de contacto
- no existe aun una ruta dedicada `/cotizador`
- no existe aun una ficha publica de producto por `slug`

### 5.2 Frontend administrativo

Rutas visibles hoy:

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

### 5.3 Backend

Areas funcionales visibles hoy:

- auth
- catalogo publico
- cotizaciones publicas
- clientes
- productos
- cotizaciones internas
- pedidos
- produccion
- reportes
- notificaciones
- abastecimiento
- health publico

---

## 6. Relacion entre componentes

La relacion base del sistema es:

- Astro consume catalogo y branding del backend
- Astro envia cotizaciones publicas al backend
- Angular consume contratos protegidos del backend
- el backend concentra reglas y persiste en PostgreSQL
- la DB sostiene trazabilidad para negocio, operacion y soporte

No hay varios backends ni varias bases.

La arquitectura real es:

- un backend central
- una DB central
- dos frontends con responsabilidades distintas

---

## 7. Lo que el sistema no promete en esta V1

Para no sobredimensionar el producto, esta V1 no debe venderse como:

- ecommerce completo
- checkout transaccional
- pagos en linea
- app movil nativa
- integracion dura con plataformas externas

---

## 8. Como conviene leer el producto

Si quieres entenderlo rapido y bien:

1. lee este mapa
2. pasa a `docs/negocio/` para entender el dominio
3. pasa a `docs/backend/`, `docs/frontend-publico-astro/` y `docs/frontend-admin-angular/`
4. termina en `docs/base-datos/`, `docs/infraestructura/` y `docs/calidad/`

---

## 9. Conclusion

Pasteleria es un sistema integral de operacion comercial y productiva.

No solo vende una marca.
No solo registra pedidos.
No solo hace CRUD.

Combina vitrina publica, operacion administrativa, produccion, abastecimiento
y control tecnico sobre una misma base de negocio.
