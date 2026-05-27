# T01 — Gobierno documental y reglas de refactoring

## 1. Objetivo

Crear en la raíz del proyecto la carpeta `implementacion_pasteleria_erp/` como centro único de documentación de implementación, decisiones, reglas de refactoring y continuidad.

Esta tanda prepara el proyecto para implementar las siguientes fases sin perder contexto y sin depender de Codex. Todo se trabajará en este chat, con documentación profunda antes de tocar código funcional.

## 2. Contexto heredado

Se leyó masivamente la pastelería, la base contable genérica y Cedro. El diagnóstico consolidado es:

- La pastelería actual ya tiene una UX/UI propia que se debe conservar.
- Cedro es un proyecto más maduro en gobierno documental, scripts, migraciones, backend transversal, identity, guía operativa, storage, reportes, auditoría, tests y evidencia.
- La base contable genérica sirve como tronco ERP, pero debe adaptarse a pastelería.
- La pastelería debe evolucionar hacia una V1 limpia y una V2 ERP unificada en tercera forma normal.
- No se debe copiar la estética ni el dominio restaurante de Cedro.
- No se debe solo extender comportamiento: también se debe refactorizar decentemente cuando el diseño actual lo pida.

## 3. Fuente Cedro usada como referencia

De Cedro se rescata el enfoque de:

- fuentes de verdad documentadas;
- producto serio, no “demo” como arquitectura;
- documentación vigente vs histórica;
- scripts humanos simples;
- migraciones core/dev/presentation;
- casos de uso como producto;
- Definition of Done;
- evidencia de release;
- tests y validaciones ejecutables.

## 4. Estado actual de Pastelería

La pastelería tiene carpetas funcionales existentes (`backend`, `frontend-admin-angular`, `frontend-publico-astro`, `db`, `scripts`, `docs`, `infra`, `storage`).

En esta tanda no se modifica nada de eso. Se agrega una carpeta raíz para documentación de implementación:

```text
implementacion_pasteleria_erp/
```

## 5. Alcance

Se crea la estructura documental base:

```text
implementacion_pasteleria_erp/
  README.md
  tandas/
  00_producto/
  10_dominio/
  20_backend/
  25_contratos_api/
  30_frontend/
  40_base_de_datos/
  50_operacion/
  60_calidad/
  70_adr/
  80_roadmap/
  90_release/
  historico/
```

Se escriben documentos mínimos para que otro chat pueda continuar.

## 6. Fuera de alcance

Esta tanda no debe:

- modificar backend Java;
- modificar Angular;
- modificar Astro;
- modificar SQL;
- activar Flyway;
- cambiar scripts existentes;
- cambiar estilos;
- cambiar UX/UI;
- corregir módulos funcionales;
- crear endpoints;
- crear tests.

## 7. Archivos que deben leerse antes de modificar

Antes de ejecutar esta tanda se debe revisar al menos:

```text
README.md
scripts/README.md si existe
backend/src/main/resources/application*.yml
frontend-admin-angular/package.json
frontend-publico-astro/package.json
```

Pero en esta tanda solo se crean documentos nuevos.

## 8. Cambios realizados

Se creó la carpeta `implementacion_pasteleria_erp/` con documentos para:

- producto serio/no demo;
- alcance ERP;
- conservación de UX/UI;
- mapeo Cedro→Pastelería;
- reglas de refactoring backend;
- reglas de refactoring frontend;
- contratos API objetivo;
- V1/V2 de base de datos;
- scripts/perfiles objetivo;
- Definition of Done;
- matriz de riesgos;
- ADR de Cedro como canon de ingeniería;
- roadmap de tandas;
- checklist pre-GitHub;
- histórico.

## 9. Riesgos

- Crear documentos que luego nadie actualice.
- Duplicar documentación existente en `docs/` sin aclarar que esta carpeta es para implementación.
- Convertir la carpeta en un basurero de planes sueltos.

Mitigación:

- Todo documento debe indicar si es vigente, histórico o pendiente.
- Las tandas deben actualizar su propio `.md`.
- Las decisiones finales deben concentrarse en esta carpeta.

## 10. Criterios de aceptación

La tanda se considera terminada si:

1. Existe `implementacion_pasteleria_erp/` en la raíz.
2. Todos los `.md` creados están dentro de esa carpeta.
3. No se modificó código funcional.
4. Queda escrita la regla de respetar la UX/UI actual.
5. Queda escrita la regla de refactoring decente.
6. Queda escrito que Cedro es canon de ingeniería, no de estética.
7. Queda clara la estrategia V1/V2.
8. Queda clara la siguiente tanda: `T02 — Scripts, perfiles y operación local`.

## 11. Pruebas mínimas

Como esta tanda solo crea documentación:

```bash
find implementacion_pasteleria_erp -type f | sort
```

Debe mostrar los archivos `.md` esperados.

## 12. Notas para el siguiente chat

Siguiente tanda: `T02 — Scripts, perfiles y operación local`.

T02 debe revisar los scripts actuales de la pastelería, compararlos con el patrón Cedro y proponer/implementar una superficie simple de operación local:

- `dev.bat`
- `pasteleria-demo.bat`
- `up-infra.bat`
- `reset-infra.bat`
- `down-infra.bat`
- `test-backend.bat`
- `test-admin.bat`
- `test-storefront.bat`

T02 sí puede modificar scripts, pero debe documentar primero.
