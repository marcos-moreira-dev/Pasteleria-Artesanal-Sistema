# 02a - Catalogo de requerimientos codificados

## 1. Proposito

Este documento no reemplaza el levantamiento original de requerimientos.

Su funcion es codificar el contenido ya levantado para volverlo mas trazable y mas util para:

- casos de uso
- diseno DB
- contratos backend
- pruebas
- y futura implementacion asistida por IA

---

## 2. Convencion usada

- `RF-01` y sucesivos para requerimientos funcionales
- `RN-01` y sucesivos para reglas de negocio
- `CU-01` y sucesivos para casos de uso

Los codigos de este documento deben mantenerse estables.

---

## 3. Catalogo funcional base

### RF-01. Registrar cliente

El sistema debe permitir registrar nuevos clientes con datos de contacto basicos.

### RF-02. Consultar clientes

El sistema debe permitir consultar clientes por nombre, telefono y otros filtros utiles para atencion.

### RF-03. Actualizar cliente

El sistema debe permitir actualizar datos de contacto y observaciones del cliente.

### RF-04. Registrar producto

El sistema debe permitir registrar productos del negocio con categoria, descripcion, precio base y estado.

### RF-05. Consultar y actualizar catalogo de productos

El sistema debe permitir consultar productos disponibles y actualizar su informacion operativa.

### RF-06. Registrar pedido

El sistema debe permitir registrar pedidos con cliente, fecha de entrega, observaciones y uno o varios detalles.

### RF-07. Consultar pedidos y estados

El sistema debe permitir consultar pedidos por fecha, cliente, estado y prioridad operativa.

### RF-08. Gestionar produccion

El sistema debe permitir visualizar pedidos por producir y actualizar el estado operativo de produccion.

### RF-09. Registrar cotizacion personalizada

El sistema debe permitir registrar cotizaciones de tortas o pedidos personalizados.

### RF-10. Convertir cotizacion en pedido

El sistema debe permitir convertir una cotizacion aprobada en un pedido formal.

### RF-11. Registrar entrega del pedido

El sistema debe permitir marcar un pedido como entregado cuando el flujo operativo este completo.

### RF-12. Consultar informacion operativa

El sistema debe permitir consultar listados y reportes operativos basicos como pedidos pendientes, pedidos del dia y pedidos por cliente.

---

## 4. Nota de alcance

Este catalogo mantiene el proyecto en una V1 moderada:

- no introduce ecommerce completo
- no introduce pasarela de pagos
- no introduce inventario complejo por insumos
- y no infla el alcance con modulos que todavia no son necesarios

