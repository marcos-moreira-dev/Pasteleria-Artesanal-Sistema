# db/V1/docs/ — Indice de documentacion de base de datos

Este folder contiene la documentacion canonica de la base de datos.

## Archivo principal

| Archivo                                                        | Contenido                                                  |
| -------------------------------------------------------------- | ---------------------------------------------------------- |
| [V1_3FN.sql](Diagramas%20y%20query%20de%20creacion/V1_3FN.sql) | DDL completo del esquema (14 tablas, indices, constraints) |

## Documentacion analitica

La documentacion de analisis y diseño (modelo conceptual, normalizacion,
diccionario de datos, reglas de integridad, catalogos y seeds) vive en la carpeta
del proyecto:

```
docs/base-datos/
```

Para estudiarla, consulta los archivos directamente en esa ruta. A continuacion
el indice completo:

```
docs/base-datos/
  00_db_canonica_y_criterios.md
  base_datos_00_modelo_conceptual_para_persistencia.md
  base_datos_00_modelo_logico_relacional.md
  base_datos_01_normalizacion_2_fn.md
  base_datos_01_normalizacion_3_fn.md
  base_datos_02_diccionario_de_datos.md
  base_datos_03_reglas_de_integridad_y_constraints.md
  base_datos_04_catalogos_y_estados.md
  base_datos_05_indices_y_consultas_clave.md
  base_datos_06_convenciones_nombres_y_migraciones.md
  base_datos_07_datos_semilla_y_escenarios_prueba_v_2.md
```

## Versionado

| Version | Descripcion                                                              |
| ------- | ------------------------------------------------------------------------ |
| V1      | Esquema actual con optimistic locking y workflow de produccion expandido |
