# T26 — Scripts finales de operación local

La carpeta `scripts/` queda como punto de entrada humano y contiene únicamente archivos `.bat`.

## Scripts disponibles

- `test-backend.bat`: compila y ejecuta tests del backend.
- `test-admin.bat`: instala dependencias del Admin Angular si hace falta y ejecuta `npm run build`.
- `test-storefront.bat`: instala dependencias del storefront Astro si hace falta y ejecuta `npm run build`.
- `test-all.bat`: ejecuta backend, admin y storefront en secuencia.
- `run-production.bat`: levanta el programa completo en modo operativo local, sin datos inventados de demo.
- `run-demo.bat`: recrea la base local, carga datos inventados de presentación/SIT y levanta el programa completo.
- `stop-local.bat`: detiene la infraestructura Docker local.

## Alcance del modo operativo local

`run-production.bat` no equivale a un despliegue real en servidor productivo. Es un modo local limpio para operar sin datos de demostración.

## Alcance del modo demo

`run-demo.bat` está pensado para revisión visual, pruebas funcionales, UAT y presentación. Este modo usa datos inventados y puede recrear la base local.
