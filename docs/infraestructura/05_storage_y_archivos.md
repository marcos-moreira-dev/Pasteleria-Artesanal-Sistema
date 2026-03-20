# 05 - Storage y archivos

## 1. Proposito

Este documento formaliza el manejo serio de archivos del sistema.

La regla es separar siempre:

- metadatos en BD
- archivo fisico en storage

---

## 2. Alcance de V1

Aplica a:

- reportes generados
- exportaciones
- adjuntos futuros si el sistema crece

No aplica a assets publicos del sitio como logos, iconos o tipografias; esos viven versionados dentro del frontend correspondiente.

---

## 3. Modelo recomendado

Tabla sugerida: `archivo_recurso`

Campos recomendados:

- `id`
- `codigo_archivo`
- `origen_modulo`
- `tipo_archivo`
- `nombre_original`
- `nombre_fisico`
- `mime_type`
- `extension`
- `tamano_bytes`
- `checksum`
- `ruta_relativa`
- `estado`
- `fecha_creacion`
- `fecha_expiracion`
- `creado_por_usuario_id`

Estados recomendados:

- `DISPONIBLE`
- `EXPIRADO`
- `ELIMINADO`

---

## 4. Estrategia de rutas

Se recomienda una raiz como:

- `storage/reports`
- `storage/exports`
- `storage/temp`

El nombre fisico debe ser estable y no depender solo del nombre subido por el usuario.

---

## 5. Reglas de seguridad

- no exponer rutas fisicas reales al cliente
- descargar siempre via endpoint autorizado
- validar permisos antes de entregar el archivo
- validar expiracion si el archivo es temporal
- registrar auditoria en descargas sensibles cuando aplique

---

## 6. Retencion y limpieza

La retencion debe ser configurable por tipo:

- temporales cortos
- reportes medianos
- archivos historicos segun politica del sistema

La limpieza automatica la ejecuta el scheduler.

---

## 7. Pruebas superficiales que deben existir

- registrar metadato al generar reporte
- impedir descarga sin permiso
- invalidar descarga cuando expira
- limpiar temporales segun politica configurada
