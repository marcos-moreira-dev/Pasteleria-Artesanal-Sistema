# Hotfix 13 - Arranque local con guía operativa

## Problema detectado

Después de integrar la guía operativa, el backend puede compilar correctamente pero fallar al arrancar si la base local ya existía antes de las tandas 7 y 8.

El síntoma es:

```text
Schema-validation: missing table [caso_uso_modulo]
```

La causa no es una clase Java rota: Hibernate valida las entidades nuevas contra una base PostgreSQL antigua que todavía no tiene las tablas `caso_uso_modulo`, `caso_uso_operativo` y `paso_caso_uso`.

## Corrección aplicada

Se agregó una reparación no destructiva:

```text
scripts/repair-guia-operativa-db.ps1
scripts/repair-guia-operativa-db.bat
```

También se reforzó:

```text
scripts/init-db.ps1
backend/scripts/start-backend-dev.cmd
```

Ahora `init-db.ps1` detecta si la base existe pero le falta la guía operativa, y aplica `V13__guia_operativa.sql` sin borrar datos.

## Uso recomendado

Desde la raíz del proyecto:

```powershell
.\scripts\repair-guia-operativa-db.bat
```

Luego arrancar el backend:

```powershell
cd .\backend
.\scripts\start-backend-dev.cmd
```

## Opción destructiva para presentación limpia

Si se quiere regenerar toda la base canónica desde cero:

```powershell
.\scripts\reset-db-local.ps1
```

Eso elimina y recrea la base local `pasteleria` con el SQL canónico actualizado.
