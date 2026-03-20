# 00 - Calidad y pruebas canonicas

## 1. Proposito

Este documento fija la estrategia minima de calidad para que Pasteleria no se construya solo con "funciona en mi maquina".

---

## 2. Objetivos de calidad de la V1

La V1 debe cuidar como minimo:

- coherencia funcional
- estabilidad de contratos API
- integridad de datos
- usabilidad operativa basica
- capacidad de despliegue sin improvisacion

---

## 3. Piramide de calidad recomendada

### Base

- pruebas unitarias de logica de backend
- pruebas de validacion y reglas de negocio
- pruebas de mapeo y contratos

### Capa media

- pruebas de integracion backend + DB
- pruebas de endpoints principales
- pruebas de migraciones y seeds

### Capa superior

- smoke tests de frontends
- pruebas E2E de flujos criticos
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

- reglas `RN-01` a `RN-22`
- `ApiResponse<T>`
- seguridad de login y roles
- conversion de cotizacion en pedido
- reportes y jobs cuando existan

### Frontend publico

- catalogo
- cotizador
- mensajes de error
- build y accesibilidad base

### Frontend administrativo

- login
- tablas y formularios
- cambios de estado
- panel de produccion
- reportes

---

## 5. Tipos de prueba minimos a contemplar

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
2. Dar por valida una regla porque "ya esta en la documentacion".
3. Mezclar datos demo rotos con validacion de negocio real.
4. Marcar release sin revisar cotizador, pedidos y produccion.

---

## 8. Referencia inteligente

Para reforzar criterio de calidad y operacion se puede revisar:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Ninitos Sonadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

La referencia ayuda a cerrar mejor las pruebas, no a reemplazar la trazabilidad propia.
