# Backend — ErpFinancialPolicy

## Ubicación

La política quedó en:

`backend/src/main/java/com/pasteleria/erp/application/ErpFinancialPolicy.java`

## Responsabilidad

Es una clase pura de reglas financieras compartidas. No usa repositorios, no depende de Spring y no conoce controladores ni pantallas.

## Contrato funcional

La clase ofrece reglas para:

- dinero;
- saldos;
- aplicaciones contra saldo;
- líneas contables;
- cuadre contable;
- origen fiscal;
- totales fiscales;
- estado por saldo;
- cuenta imputable.

## Integración actual

La integración en Tanda 16 fue deliberadamente pequeña:

- `CajaPolicy` usa la política común para normalizar dinero y validar montos.
- `CompraFinancieraPolicy` usa la política común para impuesto y total.

No se alteraron services ni controladores.

## Criterio para futuras tandas

Cuando se implementen cartera, cuentas por pagar, contabilidad y fiscalidad, deben reutilizar esta política antes de crear validaciones nuevas.

Si una regla pertenece al rubro o al flujo operativo, debe quedarse en la política del módulo. Si una regla es financiera transversal, debe ir aquí.
