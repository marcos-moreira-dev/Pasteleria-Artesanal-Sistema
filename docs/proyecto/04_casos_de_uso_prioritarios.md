# 04 - Casos de uso prioritarios

## 1. Proposito del documento

Este documento identifica y codifica los casos de uso prioritarios del sistema.

Su objetivo es que Pasteleria tenga una base mas profesional de trazabilidad entre:

- requerimientos
- reglas de negocio
- modulos
- DB
- backend
- pruebas

---

## 2. Convencion de lectura

Cada caso de uso se documenta con:

- codigo
- objetivo
- actor principal
- requerimientos relacionados
- reglas relacionadas
- flujo basico
- resultado esperado

Los codigos `RF-xx` y `RN-xx` se apoyan en:

- `docs/negocio/02a_catalogo_requerimientos_codificados.md`
- `docs/negocio/04a_catalogo_reglas_codificadas.md`

---

## 3. Casos de uso prioritarios

### CU-01. Registrar cliente

**Objetivo:** registrar la informacion basica de un cliente para futuras cotizaciones y pedidos.

**Actor principal:** personal de atencion.

**Requerimientos relacionados:** RF-01, RF-02, RF-03.

**Reglas relacionadas:** RN-01, RN-02.

**Flujo basico:**

1. El personal accede al modulo de clientes.
2. Ingresa los datos del cliente.
3. El sistema valida datos minimos.
4. El sistema registra la informacion.
5. El cliente queda disponible para futuras operaciones.

**Resultado esperado:** cliente registrado correctamente.

---

### CU-02. Registrar pedido

**Objetivo:** registrar un pedido formal asociado a un cliente.

**Actor principal:** personal de atencion.

**Requerimientos relacionados:** RF-06, RF-07.

**Reglas relacionadas:** RN-01, RN-05, RN-06, RN-07, RN-08, RN-10, RN-11.

**Flujo basico:**

1. El personal selecciona o registra un cliente.
2. Agrega uno o varios productos al detalle.
3. Registra fecha de entrega y observaciones.
4. El sistema valida coherencia minima.
5. El sistema guarda el pedido.

**Resultado esperado:** pedido creado y disponible para seguimiento.

---

### CU-03. Registrar cotizacion personalizada

**Objetivo:** registrar una cotizacion preliminar para un pedido personalizado.

**Actor principal:** personal de atencion.

**Requerimientos relacionados:** RF-09.

**Reglas relacionadas:** RN-12, RN-13, RN-14.

**Flujo basico:**

1. El cliente describe la necesidad.
2. El personal registra caracteristicas relevantes.
3. El sistema registra los items y el estimado.
4. La cotizacion queda en estado inicial controlado.

**Resultado esperado:** cotizacion registrada.

---

### CU-04. Convertir cotizacion en pedido

**Objetivo:** formalizar como pedido una cotizacion aprobada.

**Actor principal:** personal de atencion.

**Requerimientos relacionados:** RF-10.

**Reglas relacionadas:** RN-13, RN-14, RN-15.

**Flujo basico:**

1. El personal consulta la cotizacion.
2. Confirma aceptacion del cliente.
3. El sistema valida que no haya sido convertida antes.
4. El sistema crea el pedido asociado.
5. La cotizacion queda marcada como convertida.

**Resultado esperado:** pedido creado desde cotizacion sin duplicidad.

---

### CU-05. Consultar pedidos pendientes

**Objetivo:** visualizar pedidos abiertos segun estado y proximidad operativa.

**Actor principal:** personal de atencion o produccion.

**Requerimientos relacionados:** RF-07, RF-12.

**Reglas relacionadas:** RN-07, RN-16, RN-17.

**Flujo basico:**

1. El usuario accede al modulo de pedidos.
2. El sistema muestra pedidos por estado.
3. El usuario filtra y consulta detalle.

**Resultado esperado:** listado operativo util para seguimiento diario.

---

### CU-06. Gestionar produccion

**Objetivo:** visualizar y actualizar el avance de preparacion de pedidos.

**Actor principal:** personal de produccion.

**Requerimientos relacionados:** RF-08.

**Reglas relacionadas:** RN-16, RN-17.

**Flujo basico:**

1. El personal accede al panel de produccion.
2. El sistema muestra pedidos pendientes.
3. El usuario actualiza el estado operativo.
4. El sistema refleja la trazabilidad de produccion.

**Resultado esperado:** produccion actualizada correctamente.

---

### CU-07. Registrar entrega del pedido

**Objetivo:** cerrar el ciclo operativo de un pedido al ser entregado.

**Actor principal:** personal de atencion.

**Requerimientos relacionados:** RF-11.

**Reglas relacionadas:** RN-09, RN-18, RN-19.

**Flujo basico:**

1. El cliente llega o confirma retiro.
2. El personal consulta el pedido.
3. Revisa saldo o condicion operativa si aplica.
4. El sistema marca el pedido como entregado.

**Resultado esperado:** pedido cerrado de forma coherente.

---

### CU-08. Consultar informacion operativa

**Objetivo:** consultar informacion clave del negocio para control diario.

**Actor principal:** administrador o personal autorizado.

**Requerimientos relacionados:** RF-12.

**Reglas relacionadas:** RN-20, RN-21.

**Flujo basico:**

1. El usuario ingresa al modulo de consultas.
2. El sistema ofrece filtros y vistas resumidas.
3. El usuario consulta pedidos del dia, pendientes o por cliente.

**Resultado esperado:** informacion operativa accesible para decisiones rapidas.

---

## 4. Resultado esperado

Estos casos de uso sirven como base para:

- `ENT-xx` de DB
- `API-xx` de backend
- `TC-xx` de pruebas

Con esto Pasteleria deja de tener solo narrativa funcional y pasa a tener una capa mas profesional de trazabilidad.

---

## 3.1 Casos de uso operativos agregados por la guía interna

### CU-09. Revisar guía operativa

**Objetivo:** consultar procedimientos internos por área para reducir dependencia de memoria informal.

**Actor principal:** administrador, encargada de turno o personal autorizado.

**Requerimientos relacionados:** RF-12.

**Reglas relacionadas:** RN-20, RN-21.

**Flujo básico:**

1. El usuario ingresa al panel administrativo.
2. Abre la ruta **Guía operativa**.
3. Selecciona un área de trabajo.
4. Selecciona el procedimiento que necesita revisar.
5. Lee responsable, punto de inicio, objetivo y pasos.

**Resultado esperado:** el usuario cuenta con una ruta clara para operar el frente de trabajo elegido.

### CU-10. Revisar abastecimiento operativo

**Objetivo:** consultar inventario, compras, proveedores y movimientos como frente de reposición y control de insumos.

**Actor principal:** administrador o responsable de compras.

**Flujo básico:**

1. El usuario abre el módulo de abastecimiento.
2. Revisa alertas y stock crítico.
3. Consulta compras, proveedores o movimientos según necesidad.
4. Decide si debe ajustar inventario, crear compra o recibir insumos.

**Resultado esperado:** el negocio mantiene trazabilidad de materia prima e insumos críticos.

### CU-11. Atender notificaciones internas

**Objetivo:** revisar avisos operativos generados por reportes, pedidos, producción o abastecimiento.

**Actor principal:** usuario del sistema.

**Resultado esperado:** los avisos importantes no quedan escondidos en memoria verbal.
