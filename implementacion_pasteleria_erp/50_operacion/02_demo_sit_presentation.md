# Demo/SIT local de presentación

T24 deja un flujo reproducible para levantar Pastelería ERP en modo presentación.

## Comando principal

```bat
scripts\pasteleria-demo.bat
```

Este flujo reinicia infraestructura, recrea la base local, aplica la línea SQL actual y carga los datos SIT.

## Solo base de presentación

```bat
backend\scripts\seed-presentation.bat
```

Este comando recrea la base local con datos de presentación, sin levantar necesariamente toda la experiencia visual.

## Perfil

El backend de presentación usa:

```text
SPRING_PROFILES_ACTIVE=presentation
APP_STORAGE_ROOT=./storage-sit
```

## Advertencia

No usar contra datos reales. El objetivo es revisar flujos, pantallas, indicadores y consistencia de extremo a extremo.
