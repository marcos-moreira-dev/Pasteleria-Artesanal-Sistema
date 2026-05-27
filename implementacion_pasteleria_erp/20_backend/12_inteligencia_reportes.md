# Backend de inteligencia/reportes

T21 agrega el módulo `com.pasteleria.inteligencia`.

## Componentes

- `InteligenciaController`
- `InteligenciaQueryService`
- DTOs tipo record para dashboard, cartera, cuentas por pagar, caja, contabilidad, fiscalidad y stock bajo.

## Regla de arquitectura

El servicio no calcula consecuencias operativas ni corrige datos. Consulta vistas SQL preparadas y devuelve contratos de lectura.

## Endpoints

- `/api/v1/inteligencia/dashboard`
- `/api/v1/inteligencia/cartera`
- `/api/v1/inteligencia/cuentas-pagar`
- `/api/v1/inteligencia/caja`
- `/api/v1/inteligencia/contabilidad`
- `/api/v1/inteligencia/fiscal`
- `/api/v1/inteligencia/stock-bajo`

Todos son seguros para lectura y quedan documentados en `ApiContractRegistry`.
