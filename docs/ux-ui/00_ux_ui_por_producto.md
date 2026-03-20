# 00 - UX/UI por producto

## 1. Proposito

Este documento baja la capa UX/UI del proyecto de Pasteleria a decisiones usables por otra IA o por implementacion manual.

No intenta imponer una sola estetica para todo el sistema. Su criterio es:

- **marca y captacion** para la cara publica,
- **claridad guiada** para el cotizador,
- **sobriedad operativa** para el admin,
- y **alta legibilidad** para produccion.

---

## 2. Principio rector

En este proyecto no conviene usar el mismo lenguaje visual para todas las superficies.

Regla practica:

- si la superficie vende o presenta marca, puede ser mas calida y visual,
- si la superficie administra, debe ser sistematica,
- si la superficie opera bajo tiempo y presion, debe ser utilitaria y muy clara.

---

## 3. Frontend publico en Astro

### Objetivo

Presentar el negocio, mostrar catalogo, guiar al cliente hacia contacto y hacia el cotizador.

### Usuario principal

Cliente potencial o cliente recurrente.

### Lenguaje visual recomendado

- calido
- limpio
- comercial
- artesanal sin caer en exceso decorativo

### Tipografia recomendada

- titulos: `Fraunces` o una serif elegante equivalente
- texto y UI: `Inter`

Si se quiere simplificar mucho la implementacion, `Inter` en todo tambien es aceptable.

### Scaffold recomendado

1. header
2. hero
3. propuesta de valor
4. categorias o productos destacados
5. CTA hacia cotizador
6. seccion de contacto
7. footer

### Controles y patrones correctos

- cards de producto
- category chips
- CTA primario claro
- FAQ o acordeon simple
- formulario corto de contacto si aplica

### Assets correctos

- hero image sobria
- category images
- product thumbnails
- featured image para tortas personalizadas

### Evitar

- parecer ecommerce completo,
- demasiados colores simultaneos,
- tablas o patrones de backoffice a la vista del cliente.

---

## 4. Cotizador de tortas

### Objetivo

Convertir la necesidad del cliente en una solicitud de cotizacion clara, guiada y profesional.

### Usuario principal

Cliente final.

### Lenguaje visual recomendado

- amable
- guiado
- mas estructurado que la landing
- menos editorial y mas utilitario que el home publico

### Tipografia recomendada

- `Inter` para titulos, labels y cuerpo

### Patron UX recomendado

La mejor forma aqui es:

- wizard sencillo por pasos, o
- formulario por bloques con resumen persistente

### Scaffold recomendado

1. encabezado del flujo
2. indicador de progreso
3. bloque de opciones del paso actual
4. resumen de seleccion
5. acciones atras / siguiente / enviar

### Controles correctos

- selectable cards
- radio groups
- selects
- text area para observaciones
- resumen final
- confirmacion fuerte de envio

### Assets correctos

- option images cuando ayudan a elegir
- success illustration ligera

### Evitar

- lenguaje de checkout,
- simulacion de pago,
- ambiguedad sobre lo que se envio,
- y formularios gigantes sin progreso visible.

---

## 5. Frontend administrativo en Angular

### Objetivo

Operar clientes, productos, categorias, pedidos, cotizaciones y consulta interna.

### Usuario principal

Personal administrativo del negocio.

### Lenguaje visual recomendado

- dashboard sobrio
- corporativo ligero
- foco en orden, legibilidad y repetibilidad

### Tipografia recomendada

- `Inter`
- alternativa valida: `IBM Plex Sans` o `Source Sans 3`

### App shell recomendado

1. top bar sobria
2. side nav
3. area de contenido por rutas
4. barra contextual de acciones por modulo

### Plantillas de pantalla correctas

- listado + filtros + tabla + paginacion
- formulario agrupado por secciones
- detalle con tabs o bloques
- dashboard ligero, no saturado

### Controles correctos

- data table
- filtros persistentes
- formularios reactivos
- modales o drawers para edicion ligera
- badges de estado
- snackbars y dialogos de confirmacion

### Assets correctos

- empty states sobrios
- placeholder de producto
- iconografia funcional
- kit visual compartido para headers, cards, formularios y tablas

### Evitar

- visual de marketing dentro del admin,
- cards gigantes sin necesidad,
- y formularios kilométricos sin agrupacion.

---

## 6. Panel de produccion

### Objetivo

Permitir lectura rapida de pedidos, prioridad, observaciones y cambios de estado.

### Usuario principal

Produccion o cocina.

### Lenguaje visual recomendado

- utilitario
- denso con control
- alta legibilidad
- prioridad visible por estado y tiempo

### Tipografia recomendada

- `Inter`
- o `IBM Plex Sans` si se quiere una lectura algo mas tecnica

### Patrones correctos

Segun el volumen de trabajo:

- tablero por columnas de estado, o
- lista/tabla operativa con panel de detalle

### Controles correctos

- chips de estado
- prioridad visible
- observaciones cortas
- acciones inline
- refresco parcial o SSE

### Assets correctos

- casi ninguno
- empty state operativo
- mini referencia visual de pedido solo si aporta de verdad

### Evitar

- estetica pastel demasiado decorativa,
- tarjetas enormes,
- graficas innecesarias,
- y estados dependientes solo del color.

---

## 7. Reglas transversales de UX/UI

1. Contraste suficiente y foco visible.
2. El color no debe ser la unica señal de estado.
3. Un boton debe parecer boton.
4. Los formularios deben diferenciar obligatorio y opcional.
5. Los errores del backend deben traducirse a mensajes humanos.
6. La parte publica puede expresar marca; la interna no debe perder sobriedad por eso.

---

## 8. Regla de referencia inteligente

Si una pantalla, patron o modulo necesita una referencia adicional, se puede revisar como apoyo:

- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Proyecto tienda Electronica promedio`
- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Java\Sistema UE Niñitos Soñadores`

Esa consulta puede servir para:

- estructura documental,
- ideas de shell,
- patrones de tablas y formularios,
- estados de error,
- y ejemplos de handoff entre backend y frontend.

La referencia inspira, pero no reemplaza el tono y el dominio propio de Pasteleria.

---

## 9. Resumen rapido por superficie

| Superficie | Tono | Tipografia | Densidad | Patron dominante |
|---|---|---|---|---|
| Publico Astro | Comercial y calido | Fraunces + Inter | Baja a media | landing + catalogo + CTA |
| Cotizador | Guiado y claro | Inter | Media | wizard o bloques con resumen |
| Admin Angular | Sobrio y sistematico | Inter | Media | shell + tabla + formulario |
| Produccion | Operativo y rapido | Inter / IBM Plex Sans | Media a alta | tablero o lista operativa |

---

## 10. Cierre

La UX/UI de Pasteleria debe verse como una familia de superficies relacionadas, no como una sola interfaz clonada.

La regla correcta es:

- calidez donde se presenta marca,
- claridad donde se guia,
- sobriedad donde se administra,
- y eficiencia donde se produce.
