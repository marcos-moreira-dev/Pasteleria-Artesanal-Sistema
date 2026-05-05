# Tanda 0 — Auditoría inicial y corte técnico

## Estado de referencia

Pastelería Artesanal conserva una estética visual cuidada, especialmente en el admin Angular y en el catálogo público. La alineación no debe rediseñar esa identidad; debe reforzar disciplina operativa, arranque confiable, trazabilidad humana y parametrización de la carcasa.

Cedro Damasco se toma como referencia de disciplina técnica: scripts, módulos vivos, validación y documentación. No se toma como molde visual.

## Riesgos detectados

- Existían textos de presentación provisional en documentación, scripts, seeds y login.
- El arranque de base mezclaba migraciones, SQL canónico y `data.sql`.
- Docker tenía configuración que podía recrear esquema con Hibernate en vez de respetar migraciones.
- El storage de imágenes dependía de `./storage` en clases Java.
- Algunos productos no tienen fotografía dedicada; por decisión de producto pueden usar el logo de la pastelería como respaldo visual.
- El admin Angular tenía textos de marca quemados dentro del shell.

## Reglas permanentes

1. No usar lenguaje provisional como concepto visible.
2. Mantener tildes correctas y evitar mojibake.
3. Usar jerga de pastelería: vitrina, pedido, cotización, producción, decoración, empaque, receta, insumo, ingrediente, proveedor, merma, mostrador, entrega y retiro.
4. Evitar jerga técnica en pantallas visibles de negocio.
5. Mantener responsabilidad única: cada módulo debe resolver su propio propósito sin invadir otros.
6. Documentar cada tanda aplicada.
