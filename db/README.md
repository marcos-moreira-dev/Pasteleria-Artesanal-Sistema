# Base de datos — Pasteleria

Carpetilla de documentacion SQL y scripts de la base de datos.

## Estructura

```
db/
├── LICENSE                         # Aviso de licencia del codigo SQL
└── V1/
    ├── README.md                   # Documentacion de esta version
    ├── docs/
    │   └── Diagramas y query de creacion/
    │       └── V1_3FN.sql         # DDL completo en tercera forma normal
    ├── seeds/
    │   ├── 01_seed_base.sql       # Catalogo base (roles, usuarios, categorias)
    │   ├── 02_seed_demo.sql       # Datos demo operativos
    │   └── 03_seed_enterprise.sql # Datos enriquecidos para pruebas completas
    └── tools/
        ├── 00_database_bootstrap.sql  # Creacion de base pasteleria
        └── 99_reset_demo.sql          # TRUNCATE para reiniciar seeds
```

## Version actual

**V1** — Esquema con 14 tablas, optimistic locking y workflow de produccion
expandido (PENDIENTE > PREPARACION > DECORACION > EMPAQUE > FINALIZADO).

## Referencia rapida

| Recurso                                                                | Descripcion                            |
| ---------------------------------------------------------------------- | -------------------------------------- |
| [V1_3FN.sql](V1/docs/Diagramas%20y%20query%20de%20creacion/V1_3FN.sql) | DDL completo del esquema               |
| [V1/README.md](V1/README.md)                                           | Documentacion y secuencia de ejecucion |

## Documentacion extendida

La documentacion analitica (modelo conceptual, normalizacion, diccionario de
datos, reglas de integridad, indices y seeds) vive en:

```
docs/base-datos/
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
  00_db_canonica_y_criterios.md
```
