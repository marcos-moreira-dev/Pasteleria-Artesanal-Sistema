# Tanda 5 — Catálogo visual

## Resultado

Se adoptó una regla explícita: cuando un producto no tiene foto dedicada, se muestra el logo de la pastelería como respaldo visual.

## Cambios realizados

- El backend devuelve el logo cuadrado como fallback de imagen de producto.
- El frontend admin usa el mismo fallback.
- Los assets no publicados se movieron a `backend/storage/assets/catalogo-no-publicado/`.
- Se eliminó el asset temporal `producto-test.png`.

## Productos sin foto dedicada

Estos productos pueden usar el logo hasta que exista una imagen física:

- brigadeiro-box-12
- chocolate-caliente-casa
- cupcake-oreo
- cupcake-red-velvet
- frappe-mocha
- galleta-corporativa-logo
- galletas-mix-mantequilla
- mesa-dulce-80-personas
- naked-cake-boda
- pie-limon-artesanal
- tiramisu-familiar
- torta-zanahoria-nuez
