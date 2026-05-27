# Calidad — Validación de bridges ERP

La calidad de T19 se centra en que los bridges no dupliquen consecuencias ERP.

## Validaciones cubiertas

- No duplicar asientos por origen.
- Mantener partida doble en asientos bridge.
- Verificar que los orígenes contables apunten a registros existentes.

## Prueba recomendada

Ejecutar:

```bat
scripts\test-backend.bat
```

No hace falta repetir frontend si no se tocó Angular ni Astro.
