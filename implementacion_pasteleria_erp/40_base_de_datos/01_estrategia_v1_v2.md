# Estrategia V1/V2

## V1

V1 representa la pastelería actual limpia y compacta.

Incluye:

- usuarios y roles actuales;
- clientes;
- productos;
- cotizaciones;
- pedidos;
- producción simple;
- ingredientes;
- insumos;
- proveedores;
- órdenes de compra;
- archivos;
- reportes;
- guía operativa;
- auditoría.

## V2

V2 representa la transición hacia ERP completo.

En T07 solo crea scaffolding controlado:

- schemas ERP objetivo;
- marcador de migración;
- tabla de mapeo legacy.

Las tandas futuras expanden V2 por dominio.

## Reglas

- V2 no borra tablas V1.
- V2 es aditiva/transicional.
- V2 debe permitir migrar por fases.
- Mientras no haya producción real, V2 puede refinarse como baseline.
- Después de producción real, la línea debe evolucionar con migraciones incrementales.
