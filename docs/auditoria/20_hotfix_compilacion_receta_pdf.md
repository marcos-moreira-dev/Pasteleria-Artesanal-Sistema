# Hotfix 20 — compilación de RecetaPdfService

## Problema detectado
Al arrancar el backend, Maven fallaba en `RecetaPdfService.java` con errores `unclosed string literal` cerca de las líneas 261–266.

## Causa
La normalización de saltos de línea del método `normalizarTextoPdf` quedó escrita con literales de cadena abiertos por un reemplazo defectuoso.

## Corrección aplicada
Se reemplazó el bloque defectuoso por una normalización segura:

- reemplazo de viñeta `•` por guion `-`;
- eliminación del caracter de reemplazo Unicode `\uFFFD`;
- normalización de `\r\n` y `\r` hacia `\n`.

## Archivo corregido
- `backend/src/main/java/com/pasteleria/abastecimiento/application/RecetaPdfService.java`

## Verificación local en entorno de generación
Se verificó estáticamente que:

- no quedan literales multilínea rotos en el método corregido;
- el balance de llaves del archivo queda correcto;
- el zip resultante no tiene errores de compresión.

La compilación Maven real debe correrse en Windows con `scripts\\dev-backend.bat` o `scripts\\validate-backend.bat` porque el entorno de generación no tiene Maven instalado.
