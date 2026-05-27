# 11 - Matriz de trazabilidad final por componente

## 1. Proposito

Este documento cierra la trazabilidad de Pasteleria bajando la capa funcional hasta los componentes tecnicos que luego se van a implementar.

Su funcion es conectar:

- requerimientos y reglas
- casos de uso
- entidades principales
- operaciones de backend
- superficies frontend
- y pruebas iniciales

---

## 2. Alcance

Esta matriz no reemplaza:

- el catalogo funcional
- la documentacion de DB
- la documentacion de backend
- ni la documentacion de UX/UI

Su valor esta en amarrar esas capas con codigos estables.

---

## 3. Catalogo tecnico base usado en esta matriz

### 3.1 Entidades principales

- `ENT-01` Cliente
- `ENT-02` CategoriaProducto
- `ENT-03` Producto
- `ENT-04` Cotizacion
- `ENT-05` CotizacionDetalle
- `ENT-06` Pedido
- `ENT-07` PedidoDetalle
- `ENT-08` PlanificacionProduccion
- `ENT-09` UsuarioSistema
- `ENT-10` SolicitudReporte

### 3.2 Operaciones backend principales

- `API-01` Login administrativo
- `API-02` Registrar cliente
- `API-03` Consultar y actualizar clientes
- `API-04` Consultar catalogo publico
- `API-05` Consultar y administrar productos
- `API-06` Registrar cotizacion publica
- `API-07` Consultar y actualizar cotizaciones
- `API-08` Convertir cotizacion en pedido
- `API-09` Registrar pedido
- `API-10` Consultar pedidos y estados
- `API-11` Registrar entrega de pedido
- `API-12` Consultar y actualizar produccion
- `API-13` Stream o refresco operativo de produccion
- `API-14` Solicitar reporte
- `API-15` Consultar y descargar reporte

### 3.3 Superficies frontend principales

- `PUB-01` Home y catalogo publico
- `PUB-02` Cotizador publico
- `PUB-03` Contacto y confianza comercial
- `ADM-01` Login administrativo
- `ADM-02` Modulo de clientes
- `ADM-03` Modulo de catalogo
- `ADM-04` Modulo de cotizaciones
- `ADM-05` Modulo de pedidos
- `ADM-06` Panel de produccion
- `ADM-07` Modulo de reportes

---

## 4. Matriz final por flujo principal

| Flujo principal | RF | RN | CU | ENT | API | UI | TC iniciales |
|---|---|---|---|---|---|---|---|
| Gestion de clientes | RF-01, RF-02, RF-03 | RN-01, RN-02 | CU-01 | ENT-01, ENT-09 | API-02, API-03 | ADM-01, ADM-02 | TC-BE-01, TC-FE-01, TC-E2E-01 |
| Catalogo publico y administracion de productos | RF-04, RF-05 | RN-03, RN-04 | CU-08 como consulta base | ENT-02, ENT-03 | API-04, API-05 | PUB-01, ADM-03 | TC-BE-02, TC-FE-02, TC-E2E-02 |
| Registro de cotizacion personalizada | RF-09 | RN-12, RN-13, RN-22 | CU-03 | ENT-01, ENT-04, ENT-05 | API-06, API-07 | PUB-02, ADM-04 | TC-BE-03, TC-FE-03, TC-E2E-03 |
| Conversion de cotizacion a pedido | RF-10 | RN-13, RN-14, RN-15 | CU-04 | ENT-04, ENT-05, ENT-06, ENT-07 | API-08 | ADM-04, ADM-05 | TC-BE-04, TC-FE-04, TC-E2E-04 |
| Registro y seguimiento de pedido | RF-06, RF-07 | RN-01, RN-05, RN-06, RN-07, RN-08, RN-10, RN-11 | CU-02, CU-05 | ENT-01, ENT-03, ENT-06, ENT-07 | API-09, API-10 | ADM-05 | TC-BE-05, TC-FE-05, TC-E2E-05 |
| Gestion de produccion | RF-08 | RN-16, RN-17 | CU-06 | ENT-06, ENT-08 | API-12, API-13 | ADM-06 | TC-BE-06, TC-FE-06, TC-E2E-06 |
| Entrega y cierre operativo | RF-11 | RN-09, RN-18, RN-19 | CU-07 | ENT-06 | API-11 | ADM-05 | TC-BE-07, TC-FE-07, TC-E2E-07 |
| Consultas y reportes operativos | RF-12 | RN-20, RN-21, RN-22 | CU-08 | ENT-06, ENT-10 | API-14, API-15 | ADM-07 | TC-BE-08, TC-FE-08, TC-E2E-08 |

---

## 5. Trazabilidad por componente

### 5.1 DB

La capa DB queda trazada principalmente por:

- `ENT-01` a `ENT-10`
- `RI-01` a `RI-xx` definidos en la documentacion de integridad
- y los flujos de `RF-01` a `RF-12`

La regla practica es:

- si un flujo no toca entidad ni integridad visible, todavia no esta listo para implementacion

### 5.2 Backend

El backend queda trazado por:

- `API-01` a `API-15`
- validaciones derivadas de `RN-01` a `RN-22`
- DTOs y errores ligados a los casos `CU-01` a `CU-08`

### 5.3 Frontend publico

La capa publica queda trazada por:

- `PUB-01` Home y catalogo
- `PUB-02` Cotizador
- `PUB-03` Contacto

Su dependencia funcional fuerte esta en `API-04`, `API-06` y reglas `RN-12` a `RN-15`.

### 5.4 Frontend administrativo

La capa admin queda trazada por:

- `ADM-01` a `ADM-07`
- `API-01` a `API-15`
- y los casos `CU-01` a `CU-08`

### 5.5 Operacion y calidad

La capa operativa queda trazada por:

- `OPS-01` a `OPS-xx`
- `TC-BE-01` a `TC-BE-08`
- `TC-FE-01` a `TC-FE-08`
- `TC-E2E-01` a `TC-E2E-08`

---

## 6. Riesgos que esta matriz reduce

1. Endpoints sin caso de uso claro.
2. Pantallas que no responden a un requerimiento real.
3. Pruebas que no cubren nada importante.
4. DB que crece sin amarre funcional.
5. Implementacion con IA que inventa modulos o rutas fuera de alcance.

---

## 7. Regla de mantenimiento

Si cambia alguno de estos elementos, debe revisarse esta matriz:

- `RF`
- `RN`
- `CU`
- `ENT`
- `API`
- `PUB` o `ADM`
- `TC`

Si no se actualiza la matriz, la trazabilidad deja de ser confiable.

---

## 7. Actualización de trazabilidad T11/T12

### 7.1 Entidades agregadas

- `ENT-11` Notificación interna
- `ENT-12` Módulo de guía operativa
- `ENT-13` Guía operativa
- `ENT-14` Paso de guía operativa
- `ENT-15` Inventario de abastecimiento
- `ENT-16` Orden de compra
- `ENT-17` Proveedor

### 7.2 Operaciones backend agregadas

- `API-16` Consultar resumen de notificaciones
- `API-17` Marcar o archivar notificaciones
- `API-18` Consultar hub de guía operativa
- `API-19` Consultar guía operativa por código
- `API-20` Consultar dashboard de abastecimiento
- `API-21` Gestionar inventario, proveedores, compras y movimientos

### 7.3 Superficies frontend agregadas

- `ADM-08` Abastecimiento
- `ADM-09` Notificaciones
- `ADM-10` Guía operativa

### 7.4 Matriz extendida

| Flujo principal | CU | ENT | API | UI | Validación inicial |
|---|---|---|---|---|---|
| Guía operativa y manual interno | CU-09 | ENT-12, ENT-13, ENT-14 | API-18, API-19 | ADM-10 | Abrir `/guia-operativa` y revisar áreas, guías y pasos. |
| Abastecimiento operativo | CU-10 | ENT-15, ENT-16, ENT-17 | API-20, API-21 | ADM-08 | Revisar dashboard, inventario, compras, proveedores y movimientos. |
| Avisos internos | CU-11 | ENT-11 | API-16, API-17 | ADM-09 | Leer resumen de notificaciones y archivar avisos. |

### 7.5 Regla de cierre

La trazabilidad final ya no se limita a ventas y producción. El sistema también cubre operación de insumos, avisos internos, reportes y procedimientos consultivos para el equipo.
