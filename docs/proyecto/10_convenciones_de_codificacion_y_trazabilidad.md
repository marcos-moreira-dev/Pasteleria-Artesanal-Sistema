# 10 - Convenciones de codificacion y trazabilidad

## 1. Proposito

Este documento fija una convencion profesional para que Pasteleria tenga
trazabilidad real entre:

- negocio
- producto
- DB
- backend
- frontend
- operacion
- pruebas

Y, ademas, una base ordenada de buenas practicas de desarrollo.

---

## 2. Codigos oficiales recomendados

### 2.1 Negocio y producto

- `RF-01` Requerimiento funcional
- `RN-01` Regla de negocio
- `CU-01` Caso de uso
- `DA-01` Decision de arquitectura
- `CFS-01` Contrato funcional del sistema

### 2.2 DB

- `ENT-01` Entidad principal
- `CAT-01` Catalogo
- `REL-01` Relacion critica
- `RI-01` Regla de integridad
- `IDX-01` Indice importante

### 2.3 Backend, frontend y operacion

- `API-01` Endpoint importante
- `PUB-01` Ruta o flujo publico relevante
- `ADM-01` Pantalla o flujo administrativo relevante
- `REP-01` Reporte o consulta relevante
- `OPS-01` Runbook o regla operativa
- `TC-01` Caso de prueba
- `ADR-01` Decision arquitectonica formal

---

## 3. Regla de uso

1. Los codigos deben mantenerse estables.
2. No se crean codigos por decorar.
3. Un mismo concepto no debe cambiar de codigo sin razon.
4. La matriz debe poder conectar `RF`, `RN`, `CU`, `ENT`, `API` y `TC`.
5. Se permiten subseries como `TC-BE-01`, `TC-FE-01` y `TC-E2E-01`.

---

## 4. Convenciones de desarrollo por tecnologia

### 4.1 Java y Spring Boot

- nombres de clases con intencion clara
- controladores finos
- logica en servicios de aplicacion
- DTOs explicitos
- no exponer entidades JPA por API
- excepciones traducidas en una capa comun

### 4.2 TypeScript y Angular

- modelos por feature
- componentes con responsabilidad clara
- no concentrar todo en un archivo bolsa
- tipar contratos HTTP
- separar estado, vista y API

### 4.3 TypeScript y Astro

- layouts y componentes pequenos
- cliente API pequeno y explicito
- no hidratar de mas
- mantener rutas y contenido claros

### 4.4 SQL

- naming consistente
- schema canonico y seed canonico como fuente de verdad
- constraints utiles
- evitar scripts paralelos sin ownership claro

### 4.5 Scripts y automatizacion

- nombres claros
- mensajes comprensibles
- evitar secretos embebidos sin control
- arranque reproducible para humanos e IA

---

## 5. Convenciones de comentarios y documentacion viva

Cuando exista codigo implementado, se espera:

- Javadocs utiles en casos de uso importantes
- comentarios solo donde expliquen reglas, concurrencia o decisiones
- OpenAPI alineado al contrato real
- README y docs auxiliares coherentes con el estado real del proyecto

No se esperan comentarios decorativos que describan lo obvio.

---

## 6. Trazabilidad profesional

La trazabilidad no debe verse solo como burocracia.

Sirve para:

- conectar negocio con implementacion
- reducir contradicciones
- justificar pruebas
- explicar alcance
- y facilitar trabajo asistido por IA

---

## 7. Temas computacionales que debes dominar aqui

- convenciones de naming
- cohesion y separacion de responsabilidades
- trazabilidad entre capas
- contratos estables
- documentacion viva
- disciplina de cambios en un sistema real
