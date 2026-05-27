# Inteligencia y reportes ERP

La inteligencia administrativa de Pastelería se entiende como una capa de lectura sobre datos ya registrados por módulos operativos. No reemplaza reglas de negocio ni modifica saldos.

## Principio

Operación escribe datos. Inteligencia los consulta, resume y expone.

## Dominios cubiertos

- Cartera: documentos por cobrar, saldos y vencimiento.
- Cuentas por pagar: documentos, saldos y vencimiento.
- Caja: movimientos registrados por turno/caja.
- Contabilidad: asientos, diarios, origen y líneas.
- Fiscalidad: documentos fiscales internos preparados.
- Stock: ítems bajo mínimo.
- Producción/pedidos: estado operativo y cola pendiente.

## Decisión

La capa semántica vive en el schema `inteligencia` y se consume desde un módulo backend `inteligencia` de solo lectura.
