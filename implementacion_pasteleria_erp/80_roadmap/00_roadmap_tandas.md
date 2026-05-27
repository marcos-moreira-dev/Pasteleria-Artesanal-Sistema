# Roadmap de tandas

## Estado

Actualizado hasta T26 + cierre pre-GitHub.

## Tandas completadas

- T01 — Gobierno documental y reglas de refactoring.
- T02 — Scripts, perfiles y operación local.
- T03 — Backend transversal: ApiResponse, errores, requestId y paginación.
- T04 — Contratos API y permisos.
- T05 — Storage, archivos, assets y PDF base.
- T06 — Guía Operativa / Casos de uso.
- T07 — Base de datos V1/V2 y migraciones por ambiente.
- T08 — Tests base y smoke API.
- T09 — Identity, roles, permisos y sucursales.
- T10 — InventarioMovimientoService y stock serio.
- T11 — Caja operativa.
- T12 — Pedidos y producción con máquinas de estado.
- T13 — Recetas técnicas, consumo y producto terminado.
- T14 — Compras: orden, recepción, documento y cuenta por pagar.
- T15 — Terceros unificados.
- T15-HF1 — Hotfix Maven/Testcontainers y limpieza de roadmap.
- T16 — ErpFinancialPolicy.
- T17 — Cartera, cobranzas y cuentas por pagar.
- T17-HF1 — Corrección de compilación por findById ambiguo.
- T17-HF2 — Corrección de testCompile backend.
- T17-HF3 — Corrección de arranque Testcontainers en smoke integration.
- T17-HF4 — Corrección de vistas reporting/semánticas para cliente.nombre_completo.
- T17-HF5 — Corrección de caché de ApplicationContext con Testcontainers.
- T17-HF6 — Corrección de validaciones SQL con metacomandos psql ejecutadas por JDBC.
- T17-HF7 — Corrección de escape Java en validaciones JDBC.
- T18 — Contabilidad aplicada.
- T19 — Bridges ERP separados.
- T20 — Fiscalidad preparada y prudente.
- T21 — Vistas semánticas e inteligencia/reportes.
- T22 — Angular core sin tocar UX/UI.
- T23 — Workspaces Angular por dominio.
- T24 — Presentation seed y demo/SIT validada.
- T25 — Auditoría, soporte y evidencia.
- T25-HF1 — Corrección de soporte/evidencia sobre marcadores ERP.
- T25-HF2 — Corrección Angular Admin: métodos bridge en ErpCoreFacade.

## Tandas pendientes
- T26 — Checklist release, UAT y cierre pre-GitHub.

## Siguiente tanda

T26 — Checklist release, UAT y cierre pre-GitHub.


## Cierre final

- T26 — Checklist release, UAT y cierre pre-GitHub. Implementada.


## T26-HF1 — Corrección de scripts frontend npm/call

Se corrigieron `scripts/test-admin.bat` y `scripts/test-storefront.bat` para invocar `npm -v` mediante `call npm -v`, evitando el error de etiqueta `Run` en Windows. La carpeta `scripts/` conserva solo entradas `.bat` humanas.

## T26-HF2 — Corrección de arranque backend por argumento Maven

Se corrigió el script de arranque local para que `spring-boot:run` no reciba `-Dmaven.test.skip=true`, porque en PowerShell/Windows podía llegar a Maven como una fase inválida `.test.skip=true`. Los tests ya estaban aprobados; este hotfix solo ajusta ejecución local de `run-production.bat` y `run-demo.bat`.
