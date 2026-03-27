# 13 - Mapa de conceptos computacionales y de ingenieria

## 1. Proposito

Este documento traduce Pasteleria a lenguaje de computacion e ingenieria de
software.

La idea no es repetir el dominio del negocio, sino mostrar que conceptos
tecnicos y profesionales puedes estudiar usando este proyecto como caso real.

---

## 2. Como conviene usar este documento

Piensalo como un mapa de estudio.

Cada capa del sistema te permite practicar una familia distinta de conceptos:

- modelado de informacion
- arquitectura de software
- diseno de APIs
- programacion frontend
- seguridad
- testing
- operacion
- release y mantenibilidad

---

## 3. Conceptos computacionales transversales del sistema

### 3.1 Arquitectura

Pasteleria te permite estudiar:

- monolito modular
- separacion por capas
- desacople entre frontend, backend y DB
- fuente de verdad unica
- trazabilidad de decisiones

### 3.2 Modelado

Puedes estudiar:

- traduccion de dominio a entidades
- estados y transiciones
- contratos de entrada y salida
- mapeo entre modelo relacional y modelo de aplicacion

### 3.3 Integracion entre componentes

Puedes estudiar:

- consumo de API REST
- paginacion y filtros
- manejo uniforme de respuestas
- consistencia de contratos entre varios clientes

### 3.4 Calidad

Puedes estudiar:

- criterios de salida
- smoke tests
- deuda tecnica
- alineacion entre codigo, docs y operacion

---

## 4. Conceptos por producto

### 4.1 Base de datos

Conceptos importantes:

- modelo relacional
- normalizacion
- integridad referencial
- constraints
- seeds coherentes
- versionado de schema
- criterios de naming

### 4.2 Backend

Conceptos importantes:

- organizacion modular
- DTOs y `ApiResponse<T>`
- validacion
- manejo de errores
- transacciones
- seguridad con JWT
- jobs async
- request id y trazabilidad

### 4.3 Frontend publico

Conceptos importantes:

- renderizado ligero
- composicion de layouts
- consumo de datos del backend
- formularios publicos
- accesibilidad
- SEO
- i18n simple

### 4.4 Frontend administrativo

Conceptos importantes:

- SPA para backoffice
- rutas protegidas
- formularios reactivos
- estado de pantalla
- tablas, filtros y paginacion
- integracion HTTP tipada
- UX operativa

### 4.5 Infraestructura

Conceptos importantes:

- archivos y storage
- scheduler
- polling
- jobs de reporte
- notificaciones internas
- configuracion por entorno

### 4.6 Operacion y release

Conceptos importantes:

- reproducibilidad local
- scripts de arranque
- variables de entorno
- build por componente
- checklists de release
- README tecnico y README comercial

---

## 5. Buenas practicas por lenguaje y tecnologia

### 5.1 Java y Spring Boot

Buenas practicas que aparecen o deberian consolidarse:

- separar controlador, servicio y persistencia
- no exponer entidades JPA por API
- usar DTOs explicitos
- manejar errores en una capa comun
- encapsular transiciones de estado
- mantener seguridad y configuracion fuera del controlador

### 5.2 TypeScript y Angular

Buenas practicas:

- tipar contratos de red
- evitar componentes gigantes
- separar modelos por feature
- usar estado local claro
- no duplicar reglas del backend
- modelar `loading`, `empty`, `error` y `success`

### 5.3 TypeScript y Astro

Buenas practicas:

- hidratar solo donde hace falta
- no volver SPA una vitrina simple
- consumir datos con una capa API pequena
- mantener contenido y datos separados
- cuidar SEO y semantica HTML

### 5.4 SQL y PostgreSQL

Buenas practicas:

- preferir schema canonico y seed canonico
- evitar SQL legacy paralelo
- alinear DB con entidades y casos de uso
- mantener constraints utiles
- no inflar el modelo sin valor operativo

### 5.5 PowerShell y BAT

Buenas practicas:

- usar scripts reproducibles
- dejar mensajes claros
- evitar credenciales duras innecesarias
- automatizar bootstrap local
- estandarizar el camino de arranque para humanos e IA

---

## 6. Conceptos profesionales de ingenieria de software

Este proyecto tambien te deja practicar nociones mas maduras:

- consistencia entre fuente de verdad y capas derivadas
- control de alcance
- evolucion sin romper contratos
- deuda tecnica visible vs deuda tecnica tolerable
- documentacion como parte del sistema
- criterio de release readiness
- diseno para demo y para mantenimiento

---

## 7. Orden recomendado de estudio

Si quieres aprender al maximo desde este repo:

1. entiende el dominio y el mapa del producto
2. estudia la DB y los estados
3. estudia el backend y sus contratos
4. estudia el frontend publico como superficie comercial
5. estudia el admin como herramienta operativa
6. cierra con calidad, operacion e infraestructura

---

## 8. Resultado esperado

La meta no es solo saber que ventanas tiene el sistema.

La meta es que puedas explicar:

- por que esta dividido asi
- que decisiones tecnicas sostiene
- que buenas practicas aplica
- y que conceptos de computacion puedes extraer de cada capa
