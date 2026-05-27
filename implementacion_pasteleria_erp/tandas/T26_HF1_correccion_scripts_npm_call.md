# T26-HF1 — Corrección de scripts de validación frontend

## Problema

El script `scripts/test-admin.bat` podía fallar en Windows con el mensaje:

```text
El sistema no encuentra la etiqueta por lotes especificada: Run
```

El fallo ocurría durante la sección de versiones, antes de compilar Angular.

## Causa

Dentro de un archivo `.bat`, `npm` se resuelve como otro `.cmd`. Cuando se invoca desde una subrutina sin `call`, el flujo del batch puede perder el retorno correcto al script principal.

## Corrección

Se actualizó:

```text
scripts/test-admin.bat
scripts/test-storefront.bat
```

para ejecutar:

```bat
call npm -v
```

en vez de:

```bat
npm -v
```

También se normalizaron los `.bat` de `scripts/` con finales de línea CRLF para mayor compatibilidad con Windows.

## Alcance

No se tocaron:

- backend productivo;
- migraciones;
- Angular productivo;
- Astro productivo;
- UX/UI;
- endpoints;
- base de datos.

## Validación esperada

Ejecutar:

```bat
scripts\test-admin.bat
scripts\test-storefront.bat
scripts\test-all.bat
```

