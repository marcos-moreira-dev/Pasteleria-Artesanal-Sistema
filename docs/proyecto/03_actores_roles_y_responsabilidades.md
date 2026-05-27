# 03 — Actores, roles y responsabilidades

## 1. Propósito del documento

Este documento identifica los **actores** que interactúan con el sistema y define los **roles** y **responsabilidades** asociadas a cada uno.

Su objetivo es establecer quién utiliza el sistema, qué tipo de acciones realiza y qué nivel de acceso debería tener dentro de la aplicación.

Esta información será utilizada posteriormente para:

- definir permisos de acceso
- organizar módulos del frontend administrativo
- establecer reglas de autorización en el backend

---

## 2. Diferencia entre actor y rol

**Actor:** entidad o persona que interactúa con el sistema.

**Rol:** conjunto de permisos o responsabilidades que un actor puede tener dentro del sistema.

Un actor puede tener uno o varios roles dependiendo de la organización del negocio.

---

## 3. Actores del sistema

### 3.1 Cliente

Persona que compra productos o solicita pedidos a la pastelería.

El cliente normalmente interactúa con el sistema a través del **frontend público**.

Interacciones posibles:

- consultar información del negocio
- solicitar cotizaciones
- enviar solicitudes de tortas personalizadas

El cliente no tiene acceso al sistema administrativo interno.

---

### 3.2 Personal de atención

Persona encargada de atender a los clientes y registrar información en el sistema.

Responsabilidades principales:

- registrar clientes
- registrar pedidos
- consultar pedidos
- registrar observaciones

Este actor utiliza principalmente el **frontend administrativo**.

---

### 3.3 Personal de producción

Persona encargada de preparar los productos solicitados por los clientes.

Responsabilidades principales:

- consultar pedidos pendientes
- revisar observaciones de producción
- actualizar estados de preparación

Este actor interactúa principalmente con el **panel de producción**.

---

### 3.4 Administrador del sistema

Persona responsable de supervisar la operación del negocio dentro del sistema.

Responsabilidades principales:

- gestionar productos
- gestionar categorías
- revisar pedidos
- consultar reportes
- administrar usuarios del sistema

El administrador tiene acceso amplio dentro del sistema administrativo.

---

## 4. Roles del sistema

### 4.1 Rol: Administrador

Permisos generales:

- acceso completo al sistema administrativo
- gestión de productos
- gestión de clientes
- gestión de pedidos
- consulta de reportes
- gestión de usuarios del sistema

---

### 4.2 Rol: Atención

Permisos principales:

- registrar clientes
- registrar pedidos
- consultar pedidos
- registrar observaciones

Este rol no necesariamente gestiona configuraciones del sistema.

---

### 4.3 Rol: Producción

Permisos principales:

- consultar pedidos pendientes
- consultar información relevante de producción
- actualizar estados de preparación

Este rol se enfoca en la operación del taller o cocina.

---

## 5. Relación entre actores y roles

Un mismo usuario del sistema puede asumir diferentes roles dependiendo de la estructura del negocio.

Ejemplo:

- el propietario puede tener rol de administrador
- una persona de atención puede registrar pedidos
- una persona de cocina puede actualizar estados de producción

---

## 6. Uso de roles dentro del sistema

Los roles permitirán:

- controlar el acceso a diferentes módulos del sistema
- proteger información sensible
- simplificar la interfaz para cada tipo de usuario

Por ejemplo:

- el personal de producción puede ver solo pedidos relevantes para preparación
- el personal de atención puede registrar nuevos pedidos
- el administrador puede gestionar todo el sistema

---

## 7. Consideraciones para el diseño del sistema

En la implementación del sistema, los roles probablemente se representarán mediante:

- un catálogo de roles
- permisos asociados a cada rol
- validaciones de autorización en el backend

Esto permitirá mantener control sobre quién puede realizar cada acción dentro del sistema.

---

## 8. Conclusión

La identificación de actores y roles permite comprender cómo diferentes personas interactúan con el sistema.

Este documento sirve como base para definir los permisos y las restricciones de acceso dentro del sistema de software.

