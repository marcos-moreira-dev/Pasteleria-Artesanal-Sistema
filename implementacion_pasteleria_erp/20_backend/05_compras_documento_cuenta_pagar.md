# Compras: documento de compra y cuenta por pagar

## Objetivo

El backend debe separar claramente:

```text
orden de compra = intención / solicitud operativa
a recepción = hecho físico de entrada
documento de compra = respaldo financiero/documental
cuenta por pagar = obligación pendiente con proveedor
```

## Regla principal

No se debe generar cuenta por pagar hasta que exista al menos una recepción de mercadería.

## Flujo actual transicional

```text
1. Crear orden de compra en BORRADOR.
2. Enviar orden.
3. Recibir parcial o completamente.
4. Registrar documento de compra.
5. Crear cuenta por pagar pendiente.
```

## Fuera de alcance

Pagos, aplicación de pagos y contabilidad quedan para T17-T19.
