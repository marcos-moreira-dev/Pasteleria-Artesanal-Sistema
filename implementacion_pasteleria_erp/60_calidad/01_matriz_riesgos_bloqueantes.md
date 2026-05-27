# Matriz de riesgos bloqueantes

## Estado

Vigente inicial.

## Riesgos P0

| Riesgo | Impacto | Mitigación |
|---|---|---|
| Romper UX/UI actual | Alto | Cambios visuales explícitos y justificados |
| Romper storefront | Alto | Smoke test público |
| Flyway viejo desalineado | Alto | Compactar V1/V2 antes de usarlo como fuente |
| Stock negativo | Alto | Movimiento con validación y tests |
| Caja con doble turno | Alto | Constraint + servicio + test |
| Asiento descuadrado | Alto | Validación debe/haber |
| Cobro mayor que saldo | Alto | ErpFinancialPolicy |
| Pago mayor que saldo | Alto | ErpFinancialPolicy |
| Fiscalidad prometida sin validar | Alto | Fiscalidad preparada, no certificada |
| Componentes gigantes | Medio/Alto | Refactoring por facades/componentes |
