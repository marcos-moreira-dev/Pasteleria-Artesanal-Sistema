# Calidad — Validación de contabilidad

T18 agrega validaciones SQL y pruebas de migración para verificar que la estructura contable exista y que las reglas mínimas se mantengan.

## Validaciones cubiertas

- Diario general activo.
- Cuenta Caja principal activa e imputable.
- Asientos con debe igual a haber.
- Líneas contables con debe o haber, pero no ambos.
- Asientos registrados con detalle.
- Totales del encabezado coherentes con el detalle.

## Comando recomendado

```bat
scripts\test-backend.bat
```
