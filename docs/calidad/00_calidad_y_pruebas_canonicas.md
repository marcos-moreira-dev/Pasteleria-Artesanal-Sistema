# 00 - Calidad y pruebas canonicas

## 1. Proposito

Este documento fija la estrategia minima de calidad para que Pasteleria no se
construya solo con "funciona en mi maquina".

---

## 2. Objetivos de calidad de la V1

La V1 debe cuidar como minimo:

- coherencia funcional
- estabilidad de contratos
- integridad de datos
- usabilidad operativa
- capacidad de despliegue reproducible

---

## 3. Piramide de calidad recomendada

### Base

- pruebas unitarias de logica de backend
- pruebas de validacion y reglas de negocio
- pruebas de contratos y mapeo

### Capa media

- pruebas de integracion backend + DB
- pruebas de endpoints principales
- pruebas de migraciones y seeds

### Capa superior

- smoke tests de frontends
- E2E de flujos criticos
- pruebas manuales guiadas antes de release

Regla:

- no depender solo de E2E
- no depender solo de pruebas manuales

---

## 4. Prioridades de prueba por componente

### DB

- integridad
- migraciones
- seeds
- consultas operativas clave

### Backend

- seguridad de login y roles
- `ApiResponse<T>`
- conversion de cotizacion en pedido
- cambios de estado
- reportes async
- abastecimiento critico

### Frontend publico

- catalogo
- contacto con solicitud de cotizacion
- mensajes de error
- build y accesibilidad base

### Frontend administrativo

- login
- tablas y formularios
- cambios de estado
- produccion
- reportes
- abastecimiento

---

## 5. Tipos de prueba minimos

- `TC-BE` para backend
- `TC-FE` para frontends
- `TC-E2E` para flujos completos
- pruebas manuales de release

---

## 6. Criterio de salida de MVP

Antes de considerar cerrada una version candidata:

1. migraciones en verde
2. build backend en verde
3. build Astro en verde
4. build Angular en verde
5. smoke de flujos criticos completado
6. errores bloqueantes resueltos o aceptados formalmente

---

## 7. Antipatrones a evitar

1. Probar solo la UI.
2. Dar por valida una regla porque esta en la documentacion.
3. Mezclar datos de arranque rotos con validacion de negocio real.
4. Marcar release sin revisar reportes, produccion y abastecimiento.

---

## 8. Temas computacionales que debes dominar aqui

Si quieres estudiar calidad de software desde este proyecto, los temas clave son:

- piramide de testing
- cobertura util vs cobertura cosmetica
- pruebas de contrato
- pruebas de integracion con DB
- smoke tests y release checks
- criterio de salida
- deuda tecnica y riesgo residual

---

## 9. Resultado esperado

Pasteleria debe poder salir a presentación funcional o GitHub con una calidad argumentable, no
solo con intuicion.
