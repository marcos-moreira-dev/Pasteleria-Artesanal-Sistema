# Implementación Pastelería ERP

Esta carpeta concentra la documentación de implementación para evolucionar la aplicación de pastelería hacia un ERP completo, usando Cedro como referencia de ingeniería y manteniendo la UX/UI propia de la pastelería.

## Reglas maestras

1. **La UX/UI actual de la pastelería se respeta.** Cedro no reemplaza la interfaz visual.
2. **Cedro es canon de ingeniería**, no de estética: se rescatan patrones de arquitectura, seguridad, configuración, operación, calidad, scripts, contratos, storage, guía operativa, tests y evidencia.
3. **No se copia restaurante literal.** Cocina se adapta a Producción/obrador; Menú se adapta a Productos/Catálogo; RestauranteErpBridgeService se adapta a bridges ERP separados.
4. **No es solo extender comportamiento.** Si el código actual tiene clases enormes, servicios mezclados, componentes gigantes o contratos confusos, se debe hacer refactoring decente y trazable.
5. **Cada tanda se documenta antes de tocar código.** Los archivos Markdown de implementación van dentro de esta carpeta.
6. **No declarar producción final ni cumplimiento SRI final.** La fiscalidad inicial queda preparada y prudente.
7. **No romper storefront.** La app pública debe seguir funcionando mientras la app administrativa evoluciona.

## Estructura

- `tandas/`: bitácora profunda por tanda.
- `00_producto/`: decisiones de producto y alcance.
- `10_dominio/`: lenguaje, glosario y mapeo Cedro→Pastelería.
- `20_backend/`: reglas de backend, refactoring y arquitectura transversal.
- `25_contratos_api/`: contratos backend/frontend.
- `30_frontend/`: reglas Angular y conservación de UX/UI.
- `40_base_de_datos/`: estrategia V1/V2, fuentes de verdad y migraciones.
- `50_operacion/`: scripts, perfiles y operación local.
- `60_calidad/`: Definition of Done, riesgos y validaciones.
- `70_adr/`: decisiones arquitectónicas.
- `80_roadmap/`: orden de tandas.
- `90_release/`: checklist pre-GitHub, UAT y evidencia.
- `historico/`: documentos de referencia que ya no son fuente ejecutable.

## Flujo obligatorio por tanda

Antes de modificar código funcional:

1. Crear o actualizar el `.md` de la tanda en `implementacion_pasteleria_erp/tandas/`.
2. Explicar objetivo, contexto heredado, patrón de Cedro, alcance, fuera de alcance, archivos a leer, cambios, riesgos, pruebas y notas para continuidad.
3. Hacer cambios pequeños, trazables y revisables.
4. Reportar qué se modificó, qué falta y cuál tanda sigue.


## Estado T07

T07 dejó ordenada la línea futura de base de datos: `db/migration`, `dev-migration`, `presentation-migration`, `validation` y legacy Flyway histórico.


## Estado T16

T16 agregó `ErpFinancialPolicy` como política financiera común para dinero, saldos, líneas contables, cuadre contable, origen fiscal, totales fiscales y cuenta imputable. La integración fue prudente: caja y compras reutilizan reglas monetarias comunes sin reemplazar sus políticas operativas.

## Estado T17

T17 agregó la primera base funcional de cartera, cobranzas y cuentas por pagar. Se incorporaron documentos por cobrar, cobranzas aplicadas contra saldo, pagos a proveedor, aplicaciones contra documentos por pagar, contratos API y validaciones SQL de saldos. La tanda no activa todavía contabilidad, fiscalidad ni bridges automáticos; esas responsabilidades quedan para las tandas posteriores.



## Estado T17-HF1 / T17-HF2 / T17-HF3 / T17-HF4 / T17-HF5 / T17-HF6 / T17-HF7

Después de T17 se corrigieron varios puntos de estabilización:

- **T17-HF1** resolvió llamadas ambiguas a `findById` usando puertos de aplicación en lugar de repositorios concretos dentro de los servicios nuevos.
- **T17-HF2** actualizó tests desalineados por Spring Boot 4 y por la firma actual de `ProductImageService`.
- **T17-HF3** estabilizó la base de pruebas de integración con Testcontainers para arrancar PostgreSQL antes de resolver las propiedades dinámicas de datasource.
- **T17-HF4** corrigió las vistas repeatable de reporting/semántica para usar `cliente.nombre_completo` en lugar de una columna inexistente `cliente.nombre`.
- **T17-HF5** corrigió la reutilización de `ApplicationContext` en tests de integración con Testcontainers, evitando que Spring mantenga una URL JDBC asociada a un contenedor PostgreSQL ya detenido. También incorporó `10_validate_receivables_payables.sql` al test de migración.
- **T17-HF6** corrigió la ejecución de scripts de validación por JDBC, ignorando metacomandos de `psql` como `\echo` antes de enviar el SQL a `JdbcTemplate`.
- **T17-HF7** corrigió el escape Java de esos metacomandos para que `testCompile` vuelva a compilar.

La regla para continuar era no avanzar a T18 hasta que `scripts\test-backend.bat` pase completo en la máquina local. El backend pasó completo después de T17-HF7.

## Estado T18

T18 agregó contabilidad aplicada mínima: plan de cuentas, tipos de diario, asientos contables y detalle debe/haber. Usa `ErpFinancialPolicy` para validar partida doble y deja los bridges automáticos para T19. No se tocó Angular ni storefront.

## Estado T19

T19 agregó bridges ERP separados e idempotentes para conectar operaciones con consecuencias ERP internas: pedido a documento por cobrar, documento por cobrar a asiento de venta, cobranza a asiento de cobro, documento por pagar a asiento de compra y pago proveedor a asiento de pago. No se automatizaron aún dentro de los flujos operativos ni se tocó frontend.

## Estado T20

T20 agregó fiscalidad preparada y prudente: documentos fiscales internos asociados a documentos por cobrar o documentos de compra, validación de origen único, totales fiscales y estados internos (`BORRADOR`, `EMITIDO_INTERNO`, `ANULADO`). No se declara integración SRI productiva, firma electrónica, XML autorizado ni RIDE final.


## Estado T21

T21 agregó vistas semánticas e inteligencia/reporting de solo lectura: dashboard ERP, cartera, cuentas por pagar, caja, contabilidad, fiscalidad y stock bajo. La capa consulta vistas en schema `inteligencia` y no modifica datos operativos. No se tocó Angular ni storefront.

## Estado T22

T22 preparó el Angular Admin para los dominios ERP sin cambiar la UX/UI: agregó modelos TypeScript para terceros, cartera, cuentas por pagar, contabilidad, fiscalidad, bridges e inteligencia; amplió `ApiClientService`; y agregó `ErpCoreFacade` como fachada transversal para T23. No se crearon pantallas nuevas ni rutas visibles.



## Estado T23

T23 agregó la ruta `/erp` y una primera pantalla de workspaces Angular por dominio: inteligencia, terceros, cartera, cuentas por pagar, contabilidad, fiscalidad interna y bridges ERP. La pantalla usa el core de T22, conserva la estética de Pastelería y se mantiene principalmente como vista de consulta prudente. No se tocó storefront ni backend.

## Estado T24

T24 reemplazó los placeholders de `db/presentation-migration` por datos reales de presentación/SIT: clientes, proveedores, terceros, productos, ingredientes, pedidos, producción, compras, cartera, pagos, caja, asientos contables y documentos fiscales internos. También actualizó la inicialización local, que ahora queda accesible desde `scripts\run-demo.bat` y delega internamente en `tools\powershell\init-db.ps1`, usando la línea SQL ejecutable actual (`V1`, `V2`, vistas repeatable y seeds SIT), y agregó `15_validate_presentation_sit.sql` como validación condicional.



## T25 — Auditoría, soporte y evidencia

Se agregaron consultas de auditoría, evidencia de soporte, checklist operativo y validación SQL de trazabilidad.


## T25-HF1 — Corrección de soporte/evidencia sobre marcadores ERP

Se corrigió `SupportEvidenceService` para consultar `core.erp_migration_marker.codigo` en vez de una columna inexistente `marcador`. El cambio desbloquea el endpoint `GET /api/v1/soporte/evidencia` dentro del smoke backend, sin tocar migraciones, endpoints productivos ni frontend.

- T25-HF2 — Corrección Angular Admin: métodos bridge en ErpCoreFacade.


## T26 — Checklist release, UAT y cierre pre-GitHub

Cierra la repotenciación ERP con scripts finales simplificados, validación UAT mínima y documentación pre-GitHub.


## T26-HF1 — Corrección de scripts frontend npm/call

Se corrigieron `scripts/test-admin.bat` y `scripts/test-storefront.bat` para invocar `npm -v` mediante `call npm -v`, evitando el error de etiqueta `Run` en Windows. La carpeta `scripts/` conserva solo entradas `.bat` humanas.

## T26-HF2 — Corrección de arranque backend por argumento Maven

Se corrigió el script de arranque local para que `spring-boot:run` no reciba `-Dmaven.test.skip=true`, porque en PowerShell/Windows podía llegar a Maven como una fase inválida `.test.skip=true`. Los tests ya estaban aprobados; este hotfix solo ajusta ejecución local de `run-production.bat` y `run-demo.bat`.
