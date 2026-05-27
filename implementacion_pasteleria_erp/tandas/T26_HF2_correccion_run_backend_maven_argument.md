# T26-HF2 — Corrección de arranque backend por argumento Maven

## Problema

Después de T26-HF1, los tests del sistema pasaban, pero al abrir el programa con los scripts de ejecución local el backend recibía mal el argumento `-Dmaven.test.skip=true`.

Maven lo interpretaba como una fase inválida:

```text
Unknown lifecycle phase ".test.skip=true"
```

## Corrección

Se actualizó:

```text
tools/powershell/start-local-stack.ps1
```

El comando de backend pasó de:

```powershell
.\mvnw.cmd spring-boot:run -Dmaven.test.skip=true
```

a:

```powershell
.\mvnw.cmd spring-boot:run
```

Para `spring-boot:run` no hace falta pasar ese parámetro: el arranque local no ejecuta la suite de tests; solo compila y levanta la aplicación.

## Alcance

No se tocaron:

- backend productivo;
- frontend admin;
- storefront;
- migraciones;
- base de datos;
- UX/UI;
- tests.

Solo se corrigió el script de arranque local.

## Validación sugerida

```bat
scripts\run-production.bat
```

y para demo con datos inventados:

```bat
scripts\run-demo.bat
```

Los tests ya fueron reportados como aprobados antes de este hotfix.
