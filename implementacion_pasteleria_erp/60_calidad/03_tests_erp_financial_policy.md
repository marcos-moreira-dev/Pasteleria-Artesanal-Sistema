# Tests — ErpFinancialPolicy

## Archivo principal

`backend/src/test/java/com/pasteleria/erp/application/ErpFinancialPolicyTest.java`

## Cobertura agregada

Los tests cubren:

- normalización de dinero;
- rechazo de monto no positivo;
- aplicación contra saldo;
- línea contable válida e inválida;
- cuadre de asiento;
- origen fiscal único;
- totales fiscales;
- estado por saldo;
- cuenta imputable.

## Importancia

Estos tests son una barrera de seguridad para las tandas siguientes. Si cartera, cuentas por pagar, contabilidad o fiscalidad rompen una regla básica, el error debe detectarse temprano.

## Validación local sugerida

Ejecutar:

```bat
scripts\test-backend.bat
```

Si se quiere aislar solo esta clase en el futuro, se puede ejecutar el test específico desde Maven cuando el entorno local tenga dependencias disponibles.
