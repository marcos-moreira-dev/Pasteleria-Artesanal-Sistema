# 00 — Mapa de productos de software y alcance global

## 1. Propósito del documento

Este documento describe el conjunto de productos de software que forman parte del proyecto y define el alcance general del sistema.

El objetivo es tener una visión clara de **qué componentes de software existen**, **cómo se relacionan entre sí** y **qué responsabilidades tiene cada uno** dentro del sistema.

Este documento no entra en detalles técnicos de implementación. Su función es ofrecer una vista de alto nivel del sistema completo.

---

## 2. Contexto general del proyecto

El proyecto consiste en el diseño de un sistema de software para apoyar la operación de una pastelería ubicada en el norte de Guayaquil.

El sistema busca mejorar la organización del negocio mediante herramientas que permitan:

- registrar pedidos
- organizar producción
- gestionar clientes
- visualizar información operativa

Para cumplir estos objetivos, el sistema se divide en varios productos de software con responsabilidades distintas.

---

## 3. Componentes principales del sistema

El proyecto contempla los siguientes componentes principales.

### 3.1 Base de datos

La base de datos es el componente encargado de almacenar toda la información estructurada del sistema.

Entre los tipos de información que gestionará se incluyen:

- clientes
- productos
- categorías de productos
- pedidos
- detalles de pedido
- cotizaciones
- planificación de producción
- usuarios del sistema

La base de datos representa la fuente central de información del sistema.

---

### 3.2 Backend del sistema

El backend es el núcleo lógico del sistema. Se encarga de procesar las reglas del negocio y ofrecer servicios a los diferentes frontends.

Responsabilidades principales del backend:

- aplicar reglas de negocio
- registrar y consultar información
- gestionar pedidos
- gestionar clientes
- organizar la información para los frontends

El backend actúa como intermediario entre la base de datos y las aplicaciones que utilizan el sistema.

---

### 3.3 Frontend público

El frontend público corresponde al sitio web orientado a los clientes de la pastelería.

Su propósito es presentar información del negocio y permitir interacciones básicas con los clientes.

Funciones principales:

- mostrar información del negocio
- presentar el catálogo de productos
- permitir solicitudes de cotización
- mostrar información de contacto

Este componente tiene principalmente un rol informativo y de captación de clientes.

---

### 3.4 Frontend administrativo

El frontend administrativo es la aplicación utilizada por el personal del negocio para operar el sistema.

Funciones principales:

- gestión de clientes
- gestión de productos
- registro de pedidos
- seguimiento de pedidos
- visualización de información operativa

Este componente es utilizado exclusivamente por usuarios internos del negocio.

---

### 3.5 Panel de producción

El panel de producción es una vista operativa del sistema orientada al personal encargado de preparar los pedidos.

Su objetivo es facilitar la organización del trabajo en la cocina o área de producción.

Funciones principales:

- visualizar pedidos pendientes
- identificar pedidos próximos a entregar
- consultar observaciones de producción

Este panel puede integrarse dentro del frontend administrativo como un módulo especializado.

---

### 3.6 Cotizador de tortas

El cotizador de tortas es una funcionalidad orientada a facilitar solicitudes de pedidos personalizados.

Permite registrar características de una torta personalizada, como:

- tamaño
- sabor
- relleno
- tipo de decoración
- mensaje personalizado

El resultado del cotizador puede generar una cotización preliminar que posteriormente puede convertirse en un pedido formal dentro del sistema.

---

## 4. Relación entre los componentes

Los componentes del sistema se relacionan de la siguiente manera:

- la base de datos almacena toda la información del sistema
- el backend gestiona la lógica del negocio y el acceso a los datos
- el frontend público se comunica con el backend para obtener información o registrar solicitudes
- el frontend administrativo utiliza el backend para operar el sistema
- el panel de producción utiliza la misma información del backend para visualizar pedidos

Todos los componentes comparten el mismo núcleo de negocio.

---

## 5. Alcance funcional inicial

En la primera etapa del proyecto se priorizarán las siguientes capacidades:

- registrar clientes
- registrar productos
- registrar pedidos
- visualizar pedidos pendientes
- organizar la producción
- consultar información operativa

El sistema se enfocará principalmente en mejorar la organización interna del negocio.

---

## 6. Funcionalidades fuera del alcance inicial

Para mantener el proyecto manejable, algunas funcionalidades no se incluirán en la primera etapa:

- comercio electrónico completo
- pagos en línea
- integración directa con plataformas externas
- aplicaciones móviles dedicadas

Estas funcionalidades podrían evaluarse en etapas posteriores del proyecto.

---

## 7. Conclusión

El mapa de productos de software permite comprender cómo se estructura el sistema en diferentes componentes y qué rol cumple cada uno.

Este documento servirá como referencia general para la documentación posterior de base de datos, backend y frontends.