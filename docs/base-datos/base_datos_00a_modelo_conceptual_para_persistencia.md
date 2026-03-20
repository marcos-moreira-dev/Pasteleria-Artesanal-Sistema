# 00a - Modelo conceptual para persistencia

## 1. Proposito

Este documento toma el modelo conceptual del dominio y lo aterriza a una perspectiva de persistencia moderada, realista y profesional.

No reemplaza el modelo conceptual del negocio. Lo traduce a una forma util para disenar la base de datos sin sobredimensionarla.

---

## 2. Regla de moderacion

La base de datos de Pasteleria debe parecerse a un sistema administrativo comun de la vida real:

- varias entidades principales
- relaciones de negocio naturales
- catalogos utiles
- constraints reales

Pero no debe inflarse con tablas que no aportan operacion real en V1.

---

## 3. Entidades conceptuales que si justifican persistencia

### ENT-01. Cliente

Representa a quien solicita cotizaciones o realiza pedidos.

### ENT-02. Categoria de producto

Agrupa el catalogo comercial del negocio.

### ENT-03. Producto

Representa articulos comercializables del catalogo base.

### ENT-04. Cotizacion

Representa una propuesta comercial preliminar para un pedido personalizado o especial.

### ENT-05. Detalle de cotizacion

Representa items o componentes incluidos en una cotizacion.

### ENT-06. Pedido

Representa una venta o encargo formal registrado por el negocio.

### ENT-07. Detalle de pedido

Representa los productos o elementos acordados dentro de un pedido.

### ENT-08. Produccion

Representa el seguimiento operativo de la preparacion de un pedido.

### ENT-09. Usuario del sistema

Representa al usuario interno autenticado.

### ENT-10. Rol de usuario

Representa el perfil funcional de acceso.

### ENT-11. Solicitud de reporte

Representa la peticion de generacion de un reporte async cuando aplique.

---

## 4. Relaciones conceptuales criticas

### REL-01. Cliente a cotizacion

Un cliente puede tener muchas cotizaciones.

### REL-02. Cliente a pedido

Un cliente puede tener muchos pedidos.

### REL-03. Producto a categoria

Muchos productos pertenecen a una categoria.

### REL-04. Pedido a detalle de pedido

Un pedido tiene uno o varios detalles.

### REL-05. Cotizacion a detalle de cotizacion

Una cotizacion tiene uno o varios items o componentes.

### REL-06. Cotizacion a pedido

Una cotizacion aprobada puede originar como maximo un pedido.

### REL-07. Pedido a produccion

Un pedido puede tener seguimiento operativo de produccion.

### REL-08. Usuario a rol

Muchos usuarios pueden compartir un mismo rol.

---

## 5. Lo que no conviene modelar como entidad en V1

Para mantener una DB moderada, en fase 1 no conviene convertir en entidad independiente:

- cada decoracion menor de torta
- cada atributo visual secundario del producto
- cada mensaje libre de atencion
- inventario complejo de insumos con recetas y costos completos
- pasarela de pagos y conciliacion financiera formal
- reparto o tracking logistico complejo

Eso puede documentarse como ampliacion futura si el producto lo exige.

---

## 6. Resultado esperado

Este puente conceptual debe desembocar en esta secuencia:

1. modelo conceptual del dominio
2. modelo conceptual para persistencia
3. normalizacion a 2FN
4. consolidacion a 3FN
5. modelo logico relacional oficial

