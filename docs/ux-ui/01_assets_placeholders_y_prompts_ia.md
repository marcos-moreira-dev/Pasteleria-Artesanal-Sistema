# 01 - Assets, placeholders y prompts IA

## 1. Proposito

Este documento fija la politica visual del proyecto mientras las imagenes finales todavia no existan o deban regenerarse despues.

La regla obligatoria es:

**todo asset visual debe poder vivir primero como placeholder.**

---

## 2. Regla placeholder-first

En Pasteleria no se debe bloquear implementacion por falta de imagen final.

Por eso:

- cada imagen prevista debe tener identificador estable
- cada espacio visual debe aceptar placeholder
- cada prompt de generacion debe quedar documentado
- y el frontend debe tolerar que el asset final llegue despues

---

## 3. Alcance de esta regla

Aplica a:

- hero image
- imagenes de categoria
- thumbnails de producto
- imagen destacada del cotizador
- empty states ilustrados
- placeholders de producto en admin
- imagenes de fondo o apoyo visual si se usan
- logos de marca
- iconos descargados o propios
- tipografias locales usadas por la landing o admin

No aplica a:

- iconografia tecnica basica del framework
- logos oficiales ya definidos por el negocio, si existen como recurso real

---

## 4. Estructura recomendada de assets

La estructura minima sugerida es:

```text
assets/
  branding/
  icons/
  fonts/
  placeholders/
  generated/
  source-prompts/
```

Regla:

- `branding/` contiene logos, favicons y recursos de marca
- `icons/` contiene iconos locales de uso real
- `fonts/` contiene tipografias descargadas para uso local
- `placeholders/` contiene versiones iniciales neutras y usables
- `generated/` contiene versiones finales o candidatas producidas por IA
- `source-prompts/` conserva prompts aprobados y notas de variacion

Ademas, cada placeholder debe poder tener un sidecar corto como:

```text
assets/
  placeholders/
    pub-hero-home-placeholder.webp
    pub-hero-home-placeholder.md
```

Ese sidecar conserva:

- `asset_id`
- descripcion breve
- prompt canonico
- uso
- dimensiones o proporcion esperada
- estado del asset

---

## 5. Convencion de nombres

Formato recomendado:

- `pub-hero-home-placeholder.webp`
- `pub-cat-tortas-placeholder.webp`
- `pub-prod-cheesecake-placeholder.webp`
- `pub-quote-feature-placeholder.webp`
- `adm-product-placeholder.webp`
- `shared-empty-state-placeholder.webp`

Si luego se genera una variante final:

- `pub-hero-home-v1.webp`
- `pub-hero-home-v2.webp`

---

## 6. Reglas de implementacion

1. El componente no debe romper si falta la imagen final.
2. Todo `img` o equivalente debe tener fallback al placeholder.
3. El placeholder debe respetar dimensiones y proporcion esperadas.
4. El prompt aprobado debe vivir en documentacion o archivo de prompts, no en chats perdidos.
5. Si la imagen final cambia, el identificador funcional del asset no cambia.
6. Cada placeholder debe tener sidecar con descripcion y prompt, o una referencia directa a ese sidecar.
7. Si aporta valor visual, el placeholder puede mostrar una leyenda corta con el concepto del asset.
8. Logos, iconos y tipografias deben vivir localmente en el proyecto.
9. Si un icono o fuente fue descargado, debe tener nota minima de origen o licencia.

---

## 7. Registro minimo de assets y prompts

| Asset | Superficie | Placeholder inicial | Uso | Prompt canonico base | Estado |
|---|---|---|---|---|---|
| `PUB-ASSET-01` | Home publica | `pub-hero-home-placeholder.webp` | hero principal | mesa de pasteleria artesanal elegante, tonos calidos, luz suave, composicion limpia, enfoque comercial sobrio, sin texto incrustado | placeholder |
| `PUB-ASSET-02` | Catalogo | `pub-cat-tortas-placeholder.webp` | categoria tortas | fotografia editorial de tortas artesanales variadas, fondo limpio, estilo comercial premium, colores calidos controlados, sin manos ni texto | placeholder |
| `PUB-ASSET-03` | Catalogo | `pub-cat-bocaditos-placeholder.webp` | categoria bocaditos | bandeja de bocaditos finos para eventos, iluminacion natural suave, fondo neutro, estilo realista, sin texto | placeholder |
| `PUB-ASSET-04` | Cotizador | `pub-quote-feature-placeholder.webp` | apoyo visual del cotizador | torta personalizada elegante sobre fondo claro, composicion frontal limpia, estilo realista comercial, sin texto ni marcas | placeholder |
| `ADM-ASSET-01` | Admin | `adm-product-placeholder.webp` | placeholder de producto sin foto | ilustracion o foto neutra de producto de pasteleria, fondo claro, minimalista, apta para card administrativa | placeholder |
| `SHARED-ASSET-01` | Shared | `shared-empty-state-placeholder.webp` | empty state | ilustracion minima de vitrina o caja pastelera vacia, estilo sobrio, lineas limpias, sin texto | placeholder |

---

## 8. Prompting rules

Los prompts deben evitar:

- texto incrustado
- logos falsos
- manos deformes si no hacen falta
- fondos recargados
- estetica de poster generico
- composiciones que parezcan ecommerce gigante si el alcance no lo es

Conviene pedir:

- estilo realista o editorial sobrio
- fondo limpio
- espacio visual util para recorte responsivo
- coherencia de color con la marca

Para logos e iconos:

- preferir `SVG`
- evitar raster innecesario
- y conservar variantes para favicon o small sizes

---

## 9. Formato sugerido del sidecar

Plantilla minima recomendada:

```md
# PUB-ASSET-01

- archivo_placeholder: `pub-hero-home-placeholder.webp`
- descripcion: Hero principal con mesa de pasteleria artesanal elegante y tono comercial calido.
- prompt_canonico: mesa de pasteleria artesanal elegante, tonos calidos, luz suave, composicion limpia, enfoque comercial sobrio, sin texto incrustado
- uso: home publica
- proporcion: 16:9
- estado: placeholder
```

Regla:

- la descripcion debe sonar humana y corta
- el prompt conserva la instruccion util para regeneracion
- no se oculta la intencion visual dentro del nombre del archivo solamente

---

## 10. Implementacion por superficie

### Frontend publico

Debe quedar listo para:

- cargar placeholder por defecto
- reemplazar por imagen final cuando exista
- mantener `alt` correcto aunque el recurso sea temporal
- y enlazar facilmente el asset con su sidecar documental
- usar tipografias locales y branding local para no depender de servicios externos

### Cotizador

Las imagenes de apoyo son opcionales. Si no existen, el flujo sigue funcionando con placeholder o sin imagen decorativa.

### Frontend admin

No debe depender de fotos reales para operar. El placeholder de producto es suficiente mientras llega la imagen final.

---

## 11. Regla profesional

En este proyecto una imagen faltante no es excusa para detener una pantalla.

La pantalla debe nacer lista con:

- layout estable
- placeholder funcional
- descripcion recuperable
- assets locales de marca
- y prompt documentado para evolucion posterior

---

## 12. Tipografias locales activas

Mientras el negocio no entregue tipografia definitiva, el proyecto queda cerrado con esta pareja local:

- `Cormorant Garamond` para titulares, marca y jerarquia editorial
- `Source Sans 3` para lectura, formularios y operacion diaria

Ambas fuentes viven dentro del proyecto, sin dependencia de CDN, con nota minima de origen/licencia en:

- `frontend-publico-astro/public/assets/fonts/README.md`
- `frontend-admin-angular/src/assets/fonts/README.md`

---

## 13. Regla adicional de centralizacion simple

Para esta V1 no se usa storage externo ni servicios complejos de assets.

La solucion oficial es:

- branding y productos viven en `backend/src/main/resources/static/assets`
- el backend sirve esas imagenes como recursos HTTP
- el nombre canónico de imagen de producto es el `slug`

Ejemplo:

- producto: `cheesecake-frutos-rojos`
- archivo esperado: `static/assets/products/cheesecake-frutos-rojos.png`

Si existe:

- landing y admin la consumen automaticamente

Si no existe:

- backend devuelve placeholder oficial
