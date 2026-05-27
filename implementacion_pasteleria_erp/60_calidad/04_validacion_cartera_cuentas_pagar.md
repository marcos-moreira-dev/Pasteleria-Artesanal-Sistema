# Calidad — Validación de cartera y cuentas por pagar

T17 agrega validaciones SQL para asegurar que cartera y cuentas por pagar no queden con saldos imposibles.

## Validaciones

- documentos por cobrar con saldo válido;
- documentos por cobrar con estado coherente;
- aplicaciones de cobranza coherentes;
- documentos por pagar con saldo válido;
- documentos por pagar con estado coherente;
- aplicaciones de pago proveedor coherentes.

## Pruebas manuales sugeridas

1. Crear documento por cobrar.
2. Registrar cobranza parcial.
3. Verificar que el documento quede en `PARCIAL`.
4. Registrar cobranza final.
5. Verificar que el documento quede en `PAGADO`.
6. Registrar pago parcial a proveedor.
7. Verificar que el documento por pagar quede en `PAGADO_PARCIAL`.
8. Registrar pago final.
9. Verificar que quede en `PAGADO`.
