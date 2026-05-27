# Tandas 3 y 4 — validación post-assets y alineación README

## Objetivo

Cerrar la alineación posterior a la integración del catálogo visual, reforzando la validación local y eliminando referencias visuales rotas en el README.

## Tanda 3 — Validación post-assets

### Cambios aplicados

- Se reforzó `scripts/audit-assets.ps1` para revisar también:
  - productos del seed canónico;
  - branding;
  - placeholders;
  - assets/iconos referenciados por Angular;
  - láminas del README en `readme/`;
  - capturas heredadas en `assets-readme/capturas/`;
  - `assets-readme/logo/logo-principal.png`.

- Se actualizó `scripts/validate-all.bat` para incluir una etapa adicional:
  - `[6/6] Imagenes README`.

- Se creó:
  - `scripts/validate-readme-assets.ps1`
  - `scripts/validate-readme-assets.bat`

### Comando recomendado

```bat
scripts\validate-all.bat
```

Si se quiere revisar solo assets visuales del README:

```bat
scripts\validate-readme-assets.bat
```

## Tanda 4 — Láminas visuales README

### Cambios aplicados

Se creó la carpeta:

```text
readme/
```

Con láminas visuales para:

- `readme-hero.png`
- `public-home.png`
- `public-catalogo.png`
- `public-contacto.png`
- `public-en.png`
- `admin-dashboard-general.png`
- `admin-clientes.png`
- `admin-productos.png`
- `admin-cotizaciones.png`
- `admin-pedidos.png`
- `admin-produccion.png`
- `admin-reportes.png`
- `admin-guia-operativa.png`
- `admin-abastecimiento-dashboard.png`
- `admin-abastecimiento-inventario.png`
- `admin-abastecimiento-compras.png`
- `admin-abastecimiento-proveedores.png`
- `admin-abastecimiento-movimientos.png`

También se generaron equivalentes heredados en:

```text
assets-readme/capturas/
assets-readme/logo/
```

## Ajuste de README

Se actualizó el README raíz para usar:

```text
readme/
```

en lugar de rutas con espacios como:

```text
images readme/
images%20readme/
```

Esto evita rutas frágiles en Markdown y en GitHub.

## Nota de alcance

Estas imágenes son láminas visuales de presentación para README y portafolio. No sustituyen una prueba funcional real del sistema en Windows. Para cierre operativo, debe correrse:

```bat
scripts\validate-all.bat
```

y, si falla, revisar los logs en:

```text
.diagnostics/logs/
```

## Verificación estática realizada

- El README raíz ya no referencia imágenes inexistentes.
- Existen 18 láminas visuales en `readme/`.
- Existen las 7 capturas base heredadas en `assets-readme/capturas/`.
- Existe `assets-readme/logo/logo-principal.png`.
- Los 26 productos del seed canónico tienen imagen física dedicada.
- Las imágenes de producto conservan relación cuadrada.
