# Caja operativa

La caja operativa de la pastelería se implementa como una capa transicional seria sobre V1.

## Conceptos

- `caja_operativa`: caja física o lógica, por ahora `CAJA_MATRIZ`.
- `turno_caja`: apertura/cierre de caja.
- `movimiento_caja`: entradas, salidas o ajustes dentro de un turno.
- `arqueo_caja`: comparación entre monto esperado y monto declarado.

## Flujo inicial

```text
abrir caja
→ registrar movimientos
→ cerrar caja con arqueo
→ auditar operación
```

## Reglas

- Una caja no puede tener dos turnos abiertos.
- Un movimiento requiere turno abierto.
- Los montos de movimiento son positivos.
- La naturaleza define si suma o resta.
- Una salida no puede dejar saldo negativo.
- El cierre calcula diferencia.

## Límites actuales

Esta fase no crea asientos contables ni cartera. La integración financiera queda para las tandas ERP posteriores.
