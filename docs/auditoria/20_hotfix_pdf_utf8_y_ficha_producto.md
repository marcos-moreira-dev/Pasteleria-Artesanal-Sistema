# Hotfix — PDF de recetas, UTF-8 y ficha visual de producto

## Motivo
El PDF de receta se generaba correctamente, pero algunos textos provenientes de la base aparecían con caracteres dañados, por ejemplo `T?CNICA`, `Az?car`, `PREPARACI?N` o viñetas como `?`. Además, la zona de imagen del producto aparecía dentro de un recuadro crema que no aportaba al diseño.

## Causa principal
El problema no era Spring Boot ni el endpoint PDF. El origen principal estaba en la forma en que los scripts de PowerShell pasaban archivos SQL con acentos hacia `docker exec -i psql`: en Windows PowerShell, el pipe hacia comandos nativos puede convertir texto a una codificación de consola y dañar caracteres UTF-8.

## Cambios aplicados

### Scripts de base de datos
Se cambió la aplicación de SQL para usar `docker cp` y `psql -f` dentro del contenedor. Así PostgreSQL lee el archivo físico en UTF-8 sin pasar por el pipe textual de PowerShell.

Archivos actualizados:

- `scripts/init-db.ps1`
- `scripts/repair-guia-operativa-db.ps1`

### Parche SQL nuevo
Se agregó:

- `backend/src/main/resources/db/migration/V15__correccion_utf8_recetas_pdf.sql`

Este parche reescribe las recetas del catálogo con texto UTF-8 correcto y normaliza listas con guion ASCII para evitar problemas de renderizado PDF.

También se anexó el mismo bloque al seed canónico:

- `db/V1/DATABASE_SEED_CANONICO.sql`

### PDF visual
Se actualizó:

- `backend/src/main/java/com/pasteleria/abastecimiento/application/RecetaPdfService.java`

Cambios:

- la celda de imagen ya no usa fondo crema;
- la celda de imagen ya no usa borde;
- la ficha de producto queda más limpia sobre fondo blanco;
- se normaliza el texto de secciones para reemplazar viñetas por guiones en el PDF.

## Cómo aplicar en una base ya existente
Ejecutar desde la raíz del proyecto:

```powershell
cd scripts
.\repair-guia-operativa-db.bat
```

Luego reiniciar backend y volver a descargar la receta PDF.
