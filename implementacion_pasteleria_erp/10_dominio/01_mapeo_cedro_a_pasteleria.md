# Mapeo Cedro → Pastelería

## Estado

Vigente.

Cedro fue diseñado para restaurante. La pastelería debe adaptar conceptos, no copiarlos literal.

| Cedro | Pastelería |
|---|---|
| Cocina | Producción / obrador |
| Menú | Productos / catálogo |
| Producto menú | Producto vendible / catálogo |
| Pedido restaurante | Pedido pastelero / pedido personalizado |
| Delivery | Opcional |
| Ingrediente | Item maestro tipo ingrediente |
| Insumo | Item maestro tipo insumo, envase o empaque |
| Caja sesión | Turno de caja |
| RestauranteErpBridgeService | Bridges ERP separados |
| Comprobante interno restaurante | Comprobante/nota/documento preparado de pastelería |

## Reglas

- No usar “cocina” como módulo visible si el dominio correcto es producción.
- No usar “menú” como módulo principal si el usuario entiende productos/catálogo.
- No copiar rutas, datos, assets ni nombres Cedro.
- Adaptar lenguaje a negocio de pastelería.
