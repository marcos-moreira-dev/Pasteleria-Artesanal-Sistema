# Validación demo/SIT

T24 agrega validación SQL de presentación:

```text
db/validation/15_validate_presentation_sit.sql
```

## Qué valida

Cuando existe el marcador `T24_PRESENTATION_SIT_SEED`, comprueba que existan:

- pedidos SIT;
- documentos por cobrar SIT;
- cuenta por pagar SIT;
- asientos contables SIT;
- documentos fiscales internos SIT;
- asientos cuadrados.

## Comportamiento condicional

Si el marcador no existe, la validación no falla. Esto permite que la base canónica de tests backend siga pasando sin cargar presentación.

## Pruebas recomendadas

```bat
scripts\test-backend.bat
```

Para la demo visual completa:

```bat
scripts\pasteleria-demo.bat
```
