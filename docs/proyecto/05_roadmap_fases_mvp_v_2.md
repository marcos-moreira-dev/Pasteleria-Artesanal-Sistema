# 05 — Roadmap de desarrollo: fases MVP y V2

## 1. Propósito del documento

Este documento describe la planificación general del desarrollo del sistema en **fases progresivas**. La intención es mantener el proyecto manejable, priorizando primero las funcionalidades esenciales y dejando mejoras adicionales para etapas posteriores.

La estrategia consiste en construir primero un **MVP (Minimum Viable Product)** que permita operar el negocio de forma organizada. Posteriormente, se podrán agregar mejoras y funcionalidades adicionales en una segunda versión (V2).

---

## 2. Principios de planificación

El roadmap se basa en los siguientes principios:

- priorizar funcionalidades que impacten directamente la operación diaria
- evitar complejidad innecesaria en las primeras etapas
- construir un núcleo de sistema estable antes de agregar mejoras
- permitir evolución del sistema sin rediseños radicales

---

## 3. Fase 1 — MVP (Producto mínimo viable)

La primera fase busca construir el núcleo funcional del sistema para apoyar las operaciones internas del negocio.

### 3.1 Backend

Funcionalidades principales:

- gestión de clientes
- gestión de productos
- registro de pedidos
- registro de cotizaciones
- conversión de cotización a pedido
- consulta de pedidos
- gestión de estados de pedido

El backend proveerá servicios para todos los frontends del sistema.

---

### 3.2 Base de datos

Estructura inicial de datos para soportar:

- clientes
- productos
- categorías
- pedidos
- detalles de pedido
- cotizaciones
- producción
- usuarios del sistema

---

### 3.3 Frontend administrativo

El frontend administrativo permitirá a los usuarios internos operar el sistema.

Módulos principales:

- gestión de clientes
- gestión de productos
- registro de pedidos
- consulta de pedidos
- panel de producción

Este frontend será utilizado por personal de atención, producción y administración.

---

### 3.4 Frontend público

El frontend público funcionará como sitio web del negocio.

Funcionalidades principales:

- mostrar información de la pastelería
- mostrar catálogo básico de productos
- permitir solicitud de cotizaciones

Este componente tendrá principalmente un propósito informativo.

---

## 4. Fase 2 — V2 (Mejoras y ampliaciones)

Una vez que el MVP esté funcionando de forma estable, se podrán incorporar mejoras adicionales.

### 4.1 Mejoras en el sistema administrativo

Posibles mejoras:

- reportes más avanzados
- filtros y búsquedas más sofisticadas
- mejoras en paneles operativos

---

### 4.2 Mejoras en producción

Posibles mejoras:

- mejor visualización de prioridades
- planificación por fechas
- indicadores de carga de trabajo

---

### 4.3 Mejoras en el frontend público

Posibles mejoras:

- cotizador más interactivo
- visualización ampliada de productos

---

## 5. Funcionalidades fuera del alcance actual

Las siguientes funcionalidades quedan explícitamente fuera del alcance en esta etapa del proyecto:

- comercio electrónico completo
- pagos en línea
- aplicaciones móviles dedicadas
- integración directa con plataformas externas de mensajería

Estas funciones podrían evaluarse en etapas futuras si el proyecto lo requiere.

---

## 6. Beneficios del enfoque por fases

El desarrollo por fases permite:

- reducir el riesgo del proyecto
- validar el sistema de forma progresiva
- evitar sobreingeniería en las primeras etapas

Además, facilita que el sistema crezca de forma ordenada a medida que el negocio lo necesite.

---

## 7. Conclusión

El roadmap establece una estrategia clara para construir el sistema en etapas manejables. Primero se desarrolla el núcleo funcional del sistema (MVP), y posteriormente se incorporan mejoras adicionales en versiones futuras.

