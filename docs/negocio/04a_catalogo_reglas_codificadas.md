# 04a - Catalogo de reglas de negocio codificadas

## 1. Proposito

Este documento codifica las reglas del levantamiento original para que puedan rastrearse mejor hacia:

- casos de uso
- constraints
- validaciones backend
- y pruebas funcionales

---

## 2. Reglas codificadas

### RN-01. Todo pedido debe pertenecer a un cliente identificado.

### RN-02. Un cliente puede tener varios pedidos y varias cotizaciones.

### RN-03. Todo producto debe pertenecer a una categoria valida.

### RN-04. Un producto puede estar activo o inactivo para nuevas operaciones.

### RN-05. Todo pedido debe tener fecha de registro.

### RN-06. Todo pedido programado debe tener fecha de entrega.

### RN-07. Todo pedido debe tener un estado operativo identificable.

### RN-08. Un pedido puede tener uno o varios detalles.

### RN-09. Un pedido no debe marcarse como entregado si no ha pasado por un estado operativo previo coherente.

### RN-10. Cada detalle de pedido debe estar asociado a un producto valido.

### RN-11. Cada detalle de pedido debe indicar cantidad y precio acordado.

### RN-12. Una cotizacion no implica confirmacion automatica del pedido.

### RN-13. Una cotizacion puede estar pendiente, aprobada, rechazada o convertida.

### RN-14. Una cotizacion aprobada puede originar un pedido formal.

### RN-15. Una cotizacion convertida no puede volver a convertirse.

### RN-16. Todo pedido que requiera preparacion debe aparecer en planificacion de produccion.

### RN-17. La produccion debe priorizar fecha de entrega comprometida.

### RN-18. Un pedido puede requerir anticipo segun nivel de personalizacion.

### RN-19. Un pedido puede tener pagos parciales mientras el negocio lo permita.

### RN-20. Todo usuario del sistema debe tener rol definido.

### RN-21. Los permisos pueden variar segun rol.

### RN-22. El backend debe ser compartido entre el flujo publico de cotizacion y la operacion administrativa.

---

## 3. Regla de uso

Estas reglas sirven como base para:

- `CU-xx`
- `RI-xx`
- `API-xx`
- `TC-xx`

Si una regla cambia, debe actualizarse su trazabilidad asociada.

