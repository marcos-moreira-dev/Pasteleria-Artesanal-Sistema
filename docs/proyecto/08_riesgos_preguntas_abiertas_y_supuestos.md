# 08 — Riesgos, preguntas abiertas y supuestos

## 1. Propósito del documento

Este documento registra **riesgos potenciales**, **preguntas aún no resueltas** y **supuestos adoptados durante el análisis del proyecto**.

El objetivo es dejar explícitas aquellas decisiones que todavía no están completamente validadas o que podrían cambiar durante el desarrollo del sistema.

Documentar estos elementos ayuda a:

- reducir ambigüedades
- facilitar revisiones futuras del diseño
- evitar decisiones implícitas no documentadas

---

## 2. Riesgos potenciales del proyecto

### 2.1 Complejidad innecesaria del sistema

Existe el riesgo de que el sistema crezca más allá de las necesidades reales del negocio si se agregan demasiadas funcionalidades desde el inicio.

Mitigación:

- mantener el enfoque en el MVP
- priorizar funcionalidades operativas esenciales

---

### 2.2 Modelado incorrecto del dominio

Si el modelo conceptual no refleja correctamente el funcionamiento real del negocio, el diseño de la base de datos y del backend podría requerir cambios importantes posteriormente.

Mitigación:

- validar el modelo conceptual
- revisar procesos operativos con frecuencia

---

### 2.3 Cambios en el alcance del proyecto

Durante el desarrollo podrían aparecer nuevas ideas o necesidades que amplíen el alcance original del sistema.

Mitigación:

- mantener un roadmap claro
- diferenciar funcionalidades del MVP y de futuras versiones

---

### 2.4 Falta de información operativa real

Al tratarse de un proyecto hipotético o de práctica, algunas decisiones se basan en suposiciones sobre cómo opera una pastelería.

Mitigación:

- documentar todos los supuestos
- mantener flexibilidad en el diseño

---

## 3. Preguntas abiertas

Existen algunos aspectos del negocio que podrían requerir definición más detallada en el futuro.

### 3.1 Inventario de insumos

Preguntas:

- ¿Se necesita control detallado de ingredientes?
- ¿O basta con un control simple de productos?

---

### 3.2 Anticipos en pedidos

Preguntas:

- ¿Todos los pedidos personalizados requieren anticipo?
- ¿Cómo se registran pagos parciales?

---

### 3.3 Flujo exacto de producción

Preguntas:

- ¿La producción se maneja por etapas internas?
- ¿O basta con estados simples como "en preparación"?

---

### 3.4 Nivel de detalle de reportes

Preguntas:

- ¿El negocio necesita reportes analíticos avanzados?
- ¿O solo consultas operativas básicas?

---

## 4. Supuestos adoptados

Para poder avanzar en el diseño del sistema se adoptan algunos supuestos iniciales.

### 4.1 Tamaño del negocio

- una sola sucursal
- pocos usuarios internos
- operación local

---

### 4.2 Complejidad operativa

- producción relativamente simple
- flujo de pedidos directo
- sin logística compleja de reparto

---

### 4.3 Alcance del sistema

En la primera etapa el sistema cubrirá principalmente:

- gestión de clientes
- registro de pedidos
- seguimiento de producción
- consultas operativas

---

## 5. Posibles decisiones futuras

Dependiendo de cómo evolucione el proyecto, podrían evaluarse mejoras como:

- inventario detallado de ingredientes
- integración con plataformas de mensajería
- reportes analíticos más avanzados

Estas decisiones no forman parte del alcance actual.

---

## 6. Conclusión

Registrar riesgos, preguntas abiertas y supuestos permite mantener transparencia en el proceso de diseño del sistema.

Este documento servirá como referencia para futuras revisiones del proyecto y facilitará la evolución del sistema de forma controlada.

