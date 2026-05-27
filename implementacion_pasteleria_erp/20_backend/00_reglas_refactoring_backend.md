# Reglas de refactoring backend

## Estado

Vigente.

## Principio

No se trata solo de extender comportamiento. Si el backend actual tiene servicios gigantes, responsabilidades mezcladas, DTOs confusos, endpoints desalineados o reglas de negocio dispersas, se debe refactorizar decentemente.

## Reglas

1. No crear controllers con lógica de negocio.
2. No crear services gigantes que mezclen ventas, caja, inventario, contabilidad y fiscalidad.
3. No meter contabilidad dentro de controllers.
4. No duplicar validaciones financieras en muchos servicios.
5. No devolver `Page<T>` crudo de Spring.
6. Normalizar errores y respuestas.
7. Mantener `requestId` en respuestas y auditoría.
8. Usar servicios de aplicación por caso de uso.
9. Separar bridges ERP por dominio.
10. Mantener clases legibles y trazables.

## Bridges ERP

No crear un único `PasteleriaErpBridgeService` gigante. Crear:

- `VentaErpBridgeService`
- `CajaErpBridgeService`
- `CompraErpBridgeService`
- `ProduccionErpBridgeService`
- `FiscalErpBridgeService`

## Criterio

Un refactor es aceptable si mejora responsabilidad única, legibilidad, pruebas y continuidad sin cambiar comportamiento visual accidentalmente.
