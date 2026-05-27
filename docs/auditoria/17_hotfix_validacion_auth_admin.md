# Hotfix 17 — validación backend y sesión administrativa

## Motivo
Durante la validación local posterior a las tandas 3 y 4 se detectaron dos problemas independientes:

1. `validate-backend.bat` se detenía en la etapa de compilación backend aunque la base canónica se creaba correctamente.
2. El panel administrativo podía quedar dentro del layout con una sesión local inválida; el backend respondía `403` en endpoints protegidos y el dashboard mostraba errores acumulados.

## Cambios aplicados

### Scripts de validación
- `scripts/validate-backend.bat`
  - Se eliminó el uso frágil de `call "%MVN_CMD%"` cuando el comando era `mvn`.
  - Ahora usa `call mvn ...` si Maven global existe.
  - Si no existe Maven global, usa `backend\mvnw.cmd` por ruta absoluta.

- `scripts/validate-system-full.ps1`
  - Se corrigió la interpolación PowerShell de `$TableName:` a `${TableName}:`.
  - Esto evita el error `InvalidVariableReferenceWithDrive`.

### Frontend administrativo
- `frontend-admin-angular/src/app/core/auth/auth.interceptor.ts`
  - Ahora trata `401` y `403` como sesión inválida en endpoints protegidos.
  - Limpia la sesión local y redirige a `/login`.

- `frontend-admin-angular/src/app/core/auth/auth.service.ts`
  - Al restaurar sesión desde `localStorage`, descarta tokens vencidos o malformados.

## Interpretación del problema visual
El backend arrancaba correctamente y los endpoints `OPTIONS` de CORS respondían `200`, pero los `GET` administrativos respondían `403`. Eso indica sesión/token inválido, no caída del backend.

## Acción esperada para el usuario
Después de este hotfix, si el navegador conserva una sesión vieja, la app debe sacarlo al login automáticamente. Si aún aparece el error, limpiar `localStorage` o cerrar sesión y volver a ingresar con `admin / admin12345`.
