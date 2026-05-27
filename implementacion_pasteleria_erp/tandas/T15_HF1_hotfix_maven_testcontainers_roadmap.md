# T15-HF1 — Hotfix Maven/Testcontainers y limpieza de roadmap

## Objetivo

Corregir un bloqueo técnico detectado después de T15: el backend no alcanzaba a iniciar la compilación Maven porque las dependencias de Testcontainers estaban declaradas sin una versión gestionada.

Esta tanda no cambia reglas de negocio, pantallas, UX/UI, base de datos ni contratos funcionales. Es una tanda de saneamiento para que las siguientes tandas ERP se apoyen sobre una validación backend confiable.

## Problema detectado

Al ejecutar:

```bat
scripts\test-backend.bat
```

Maven detenía la lectura del proyecto con errores como:

```text
'dependencies.dependency.version' for org.testcontainers:junit-jupiter:jar is missing
'dependencies.dependency.version' for org.testcontainers:postgresql:jar is missing
```

El problema no estaba relacionado con Java 21 ni con Temurin. El `pom.xml` ya estaba alineado con Java 21 mediante `java.version` y `maven-toolchains-plugin`.

## Decisión aplicada

Se adaptó el patrón ya usado por el proyecto Cedro Damasco: declarar una propiedad para la versión de Testcontainers y gestionar sus artefactos mediante el BOM oficial.

En `backend/pom.xml` se agregó:

```xml
<testcontainers.version>1.21.4</testcontainers.version>
```

y también:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers-bom</artifactId>
      <version>${testcontainers.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

Así, las dependencias de prueba:

```xml
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>postgresql</artifactId>
  <scope>test</scope>
</dependency>
```

pueden permanecer sin versión directa, porque la versión queda centralizada en el BOM.

## Limpieza documental

Se actualizó:

```text
implementacion_pasteleria_erp/80_roadmap/00_roadmap_tandas.md
```

Correcciones realizadas:

- El estado ya no queda en T14, sino en T15 + T15-HF1.
- Se agrega T15 — Terceros unificados como tanda completada.
- Se registra T15-HF1 como hotfix completado.
- Se elimina la contradicción que marcaba T16 como implementada y pendiente al mismo tiempo.
- T16 queda correctamente como siguiente tanda funcional pendiente.

## Alcance explícitamente no tocado

No se modificó:

- Interfaz gráfica.
- UX/UI de Pastelería.
- Storefront Astro.
- Migraciones SQL.
- Servicios de negocio.
- Controllers.
- Contratos API.
- Permisos.
- Seeds.

## Validación esperada en máquina local

Ejecutar desde la raíz del proyecto:

```bat
scripts\test-backend.bat
```

En PowerShell clásico, para correr todo, usar uno por uno:

```powershell
.\test-admin.bat
.\test-backend.bat
.\test-storefront.bat
```

O delegar la cadena a `cmd`:

```powershell
cmd /c ".\test-admin.bat && .\test-backend.bat && .\test-storefront.bat"
```

## Resultado esperado

El backend ya no debería detenerse por ausencia de versión en Testcontainers. Si aparece otro error después, ya sería una falla posterior real de compilación, tests, migraciones o integración, no del modelo Maven inicial.
