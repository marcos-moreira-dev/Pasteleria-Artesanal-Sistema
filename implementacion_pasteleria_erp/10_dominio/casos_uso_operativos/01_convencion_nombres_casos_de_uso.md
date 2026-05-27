# Convención de nombres de casos de uso

## Código

Formato actual:

```text
CU-GO-000
```

Donde:

- `CU` = caso de uso;
- `GO` = guía operativa;
- `000` = número de orden.

## Recomendación para V2 ERP

Para los nuevos módulos ERP conviene evolucionar a prefijos por dominio:

```text
CU-CLI-001   Clientes
CU-PROD-001  Productos
CU-COT-001   Cotizaciones
CU-PED-001   Pedidos
CU-PRO-001   Producción
CU-REC-001   Recetas técnicas
CU-INV-001   Inventario
CU-COM-001   Compras
CU-CAJ-001   Caja
CU-CAR-001   Cartera
CU-CPG-001   Cuentas por pagar
CU-TES-001   Tesorería
CU-CON-001   Contabilidad
CU-FIS-001   Fiscalidad preparada
CU-REP-001   Reportes
CU-AUD-001   Auditoría
CU-SOP-001   Soporte
```

## Título

El título debe ser operativo y entendible para el negocio:

- bien: `Registrar pedido pastelero`;
- bien: `Cerrar caja del turno`;
- mal: `Crear entidad PedidoDTO`;
- mal: `Ejecutar endpoint POST`.

## Pasos

Cada paso debe decir qué hace el usuario y qué debe verificar.

No usar jerga interna de desarrollo en pasos visibles.

## Resultado esperado

En V2 se debe agregar o documentar para cada caso:

- qué mensaje aparece;
- qué estado cambia;
- qué registro queda creado;
- qué módulo se actualiza;
- qué auditoría o reporte queda disponible si aplica.
