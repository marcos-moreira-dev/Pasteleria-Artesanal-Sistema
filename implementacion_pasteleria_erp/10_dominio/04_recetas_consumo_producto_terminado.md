# Recetas técnicas, consumo y producto terminado

La producción de pastelería debe distinguir entre:

```text
receta textual/comercial
receta técnica operativa
consumo de ingredientes
lote producido
entrada documental de producto terminado
```

## Flujo operativo

```text
pedido confirmado
→ producción en proceso
→ producción finalizada
→ consumo de ingredientes según receta activa
→ salida de inventario
→ lote producido
→ entrada documental de producto terminado
```

## Regla transicional

Si el producto no tiene receta activa, el sistema no bloquea la finalización. Genera un lote documental sin consumo automático y deja evidencia para completar la receta técnica después.

## Regla futura

Cuando el catálogo de recetas esté completo, se podrá endurecer la política para exigir receta activa antes de finalizar producción.
