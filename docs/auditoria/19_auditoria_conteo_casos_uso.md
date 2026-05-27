# Auditoría de casos de uso por módulo

## Resultado

El módulo de Guía operativa queda actualmente con 42 casos de uso distribuidos en 14 módulos. La distribución es uniforme: 3 casos por módulo.

Esto no significa que el negocio tenga exactamente tres procesos reales por área; significa que la guía fue normalizada como manual operativo mínimo para presentación. Para una versión más formal, lo correcto sería levantar casos de uso reales por rol y flujo antes de ampliar o recortar la guía.

| Código módulo | Nombre visible | Cantidad | Casos |
|---|---:|---:|---|
| `RESUMEN_DIA` | Resumen del día | 3 | CU-GO-001: Revisar el resumen del día<br>CU-GO-002: Usar las notificaciones del resumen<br>CU-GO-003: Interpretar la meta semanal |
| `CLIENTES` | Clientes | 3 | CU-GO-010: Registrar cliente de mostrador o encargo<br>CU-GO-011: Buscar y corregir datos de cliente<br>CU-GO-012: Usar cliente en cotización o pedido |
| `CATALOGO` | Catálogo de productos | 3 | CU-GO-020: Crear producto de catálogo<br>CU-GO-021: Editar producto y descargar receta PDF<br>CU-GO-022: Publicar u ocultar producto de la vitrina |
| `COTIZACIONES` | Cotizaciones | 3 | CU-GO-030: Crear cotización para torta o mesa dulce<br>CU-GO-031: Convertir cotización aprobada en pedido<br>CU-GO-032: Dar seguimiento a cotizaciones pendientes |
| `PEDIDOS` | Pedidos | 3 | CU-GO-040: Registrar pedido confirmado<br>CU-GO-041: Actualizar estado de pedido<br>CU-GO-042: Cerrar pedido entregado |
| `PRODUCCION` | Producción | 3 | CU-GO-050: Organizar cola de producción<br>CU-GO-051: Registrar avance de cocina<br>CU-GO-052: Consultar receta antes de preparar |
| `DECORACION_EMPAQUE` | Decoración y empaque | 3 | CU-GO-060: Verificar decoración final<br>CU-GO-061: Confirmar empaque y salida<br>CU-GO-062: Registrar incidencia de entrega |
| `ABASTECIMIENTO` | Abastecimiento | 3 | CU-GO-070: Revisar insumos críticos<br>CU-GO-071: Registrar movimiento de inventario<br>CU-GO-072: Crear orden de compra |
| `RECETAS` | Recetas técnicas | 3 | CU-GO-080: Consultar receta técnica desde Productos<br>CU-GO-081: Editar receta de producto<br>CU-GO-082: Usar receta para calcular producción |
| `PROVEEDORES` | Proveedores | 3 | CU-GO-090: Registrar proveedor nuevo<br>CU-GO-091: Asociar insumo con proveedor<br>CU-GO-092: Desactivar proveedor problemático |
| `REPORTES` | Reportes | 3 | CU-GO-100: Generar reporte de negocio<br>CU-GO-101: Revisar reportes generados<br>CU-GO-102: Usar reportes para decidir compras |
| `NOTIFICACIONES` | Notificaciones | 3 | CU-GO-110: Leer notificaciones pendientes<br>CU-GO-111: Archivar notificaciones atendidas<br>CU-GO-112: Usar avisos para cambiar de módulo |
| `USUARIOS` | Usuarios y permisos | 3 | CU-GO-120: Crear usuario interno<br>CU-GO-121: Cambiar rol o permiso<br>CU-GO-122: Desactivar usuario |
| `GUIA_OPERATIVA` | Guía operativa | 3 | CU-GO-130: Consultar la guía operativa<br>CU-GO-131: Capacitar a alguien con la guía<br>CU-GO-132: Detectar procedimiento desactualizado |

## Conclusión

- Sí: todos los módulos tienen exactamente 3 casos de uso.
- Es una distribución regular, útil para demo y documentación mínima, pero artificial si se interpreta como modelado formal del negocio.
- En esta tanda no se agregaron ni eliminaron casos; solo se corroboró y documentó el conteo.