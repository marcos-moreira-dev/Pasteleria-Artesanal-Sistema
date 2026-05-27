# 00 - Indice de documentacion canonica

## 1. Proposito

Este documento ordena la documentacion del proyecto de Pasteleria en una ruta canonica util para:

- leer el dominio sin perder la investigacion profunda
- congelar las decisiones del sistema
- separar claramente la documentacion general de la ingenieria por componente
- y dejar el proyecto listo para implementacion asistida por IA con menos ambiguedad

La regla base es esta:

**el levantamiento profundo se conserva; la implementacion se apoya en documentos canonicos derivados de ese levantamiento.**

---

## 2. Productos de software acordados

Las superficies y componentes del proyecto quedan interpretados asi:

1. Base de datos
2. Backend central
3. Frontend publico en Astro
4. Frontend administrativo en Angular
5. Panel de produccion como superficie operativa interna
6. Cotizador de tortas como flujo guiado de cara publica

Notas de alcance:

- el proyecto no debe presentarse como ecommerce completo en V1
- el cotizador es una capacidad importante, no un formulario decorativo
- y el panel de produccion puede vivir dentro del frontend interno si eso simplifica la arquitectura final

---

## 3. Stack congelado a nivel general

Para este proyecto se asume como linea base:

- Java 21 con Eclipse Temurin 21
- Spring Boot 4
- PostgreSQL
- Flyway
- Node.js 22.12.0+ para los frontends
- Astro 5.x para la superficie publica
- Angular 21.x para la superficie interna
- TypeScript 5.x en Astro y 5.9.x en Angular
- RxJS 7.4+ en Angular
- monolito modular como estilo general

---

## 4. Ruta canonica de lectura

### 4.1. Capa de investigacion de dominio

Orden recomendado:

1. `docs/negocio/00_descripcion_pasteleria_y_contexto_inicial.md`
2. `docs/negocio/01_levantamiento_informacion_negocio.md`
3. `docs/negocio/02_levantamiento_requerimientos.md`
4. `docs/negocio/02a_catalogo_requerimientos_codificados.md`
5. `docs/negocio/03_modelo_conceptual_dominio.md`
6. `docs/negocio/04_reglas_negocio_y_supuestos.md`
7. `docs/negocio/04a_catalogo_reglas_codificadas.md`
8. `docs/negocio/05_glosario_alcance_y_limites.md`

### 4.2. Capa de producto y alcance general

Orden recomendado:

1. `docs/proyecto/00_mapa_productos_software_y_alcance_global.md`
2. `docs/proyecto/01_procesos_clave_del_negocio.md`
3. `docs/proyecto/02_catalogo_estados_y_transiciones.md`
4. `docs/proyecto/03_actores_roles_y_responsabilidades.md`
5. `docs/proyecto/04_casos_de_uso_prioritarios.md`
6. `docs/proyecto/05_roadmap_fases_mvp_v_2.md`
7. `docs/proyecto/06_decisiones_arquitectonicas_iniciales.md`
8. `docs/proyecto/07_catalogo_reportes_y_consultas.md`
9. `docs/proyecto/08_riesgos_preguntas_abiertas_y_supuestos.md`
10. `docs/proyecto/09_baseline_enterprise_del_sistema.md`
11. `docs/proyecto/10_convenciones_de_codificacion_y_trazabilidad.md`
12. `docs/proyecto/11_matriz_trazabilidad_final_por_componente.md`
13. `docs/proyecto/12_registro_minimo_de_adrs.md`
14. `docs/proyecto/13_mapa_conceptos_computacionales_y_de_ingenieria.md`

### 4.3. Capa UX/UI y sistema visual

Orden recomendado:

1. `docs/ux-ui/00_ux_ui_por_producto.md`
2. `docs/ux-ui/01_assets_placeholders_y_prompts_ia.md`

### 4.4. Capa DB

Orden recomendado:

1. `docs/base-datos/00_db_canonica_y_criterios.md`
2. `docs/negocio/03_modelo_conceptual_dominio.md`
3. `docs/base-datos/base_datos_00a_modelo_conceptual_para_persistencia.md`
4. `docs/base-datos/base_datos_01_normalizacion_2_fn.md`
5. `docs/base-datos/base_datos_01_normalizacion_3_fn.md`
6. `docs/base-datos/base_datos_00_modelo_logico_relacional.md`
7. `docs/base-datos/base_datos_02_diccionario_de_datos.md`
8. `docs/base-datos/base_datos_03_reglas_de_integridad_y_constraints.md`
9. `docs/base-datos/base_datos_04_catalogos_enums_y_estados.md`
10. `docs/base-datos/base_datos_05_indices_y_consultas_clave.md`
11. `docs/base-datos/base_datos_06_convenciones_nombres_y_migraciones.md`
12. `docs/base-datos/base_datos_07_datos_semilla_y_escenarios_prueba_v_2.md`

### 4.5. Capa backend

Orden recomendado:

1. `docs/backend/backend_implementacion_contexto_general.md`
2. `docs/backend/backend_00_vision_backend_y_modulos.md`
3. `docs/backend/backend_01_modelo_de_dominios_y_agregados.md`
4. `docs/backend/backend_02_diseno_de_api_rest.md`
5. `docs/backend/backend_03_dtos_y_contratos_api.md`
6. `docs/backend/backend_04_seguridad_testing_y_operacion.md`

### 4.6. Capa infraestructura enterprise

Orden recomendado:

1. `docs/infraestructura/00_infraestructura_enterprise_canonica.md`
2. `docs/infraestructura/01_reportes_async_worker_y_cola.md`
3. `docs/infraestructura/02_scheduler_y_jobs_periodicos.md`
4. `docs/infraestructura/03_auditoria_de_negocio.md`
5. `docs/infraestructura/04_notificaciones_internas.md`
6. `docs/infraestructura/05_storage_y_archivos.md`
7. `docs/infraestructura/06_concurrencia_idempotencia_y_eventos_internos.md`
8. `docs/infraestructura/07_componentes_diferidos_y_extensiones.md`

### 4.7. Capa frontend publico

Orden recomendado:

1. `docs/frontend-publico-astro/00_frontend_publico_canonico.md`
2. `docs/frontend-publico-astro/frontend_publico_astro_implementacion_contexto_general.md`
3. `docs/frontend-publico-astro/01_frontend_publico_rutas_componentes_y_contenido.md`
4. `docs/frontend-publico-astro/02_frontend_publico_api_formularios_y_cotizador.md`
5. `docs/frontend-publico-astro/03_frontend_publico_build_seo_y_operacion.md`
6. `docs/frontend-publico-astro/04_frontend_publico_i18n_branding_y_assets_locales.md`
7. `docs/ux-ui/00_ux_ui_por_producto.md`
8. `docs/ux-ui/01_assets_placeholders_y_prompts_ia.md`

### 4.8. Capa frontend interno

Orden recomendado:

1. `docs/frontend-admin-angular/00_frontend_admin_canonico.md`
2. `docs/frontend-admin-angular/frontend_admin_angular_implementacion_contexto_general.md`
3. `docs/frontend-admin-angular/01_frontend_admin_shell_rutas_y_modulos.md`
4. `docs/frontend-admin-angular/02_frontend_admin_servicios_estado_y_auth.md`
5. `docs/frontend-admin-angular/03_frontend_admin_tablas_formularios_produccion_y_reportes.md`
6. `docs/frontend-admin-angular/04_frontend_admin_build_testing_y_operacion.md`
7. `docs/ux-ui/00_ux_ui_por_producto.md`

### 4.9. Capa operacion y calidad

Orden recomendado:

1. `docs/operacion/00_operacion_calidad_despliegue_y_referencia.md`
2. `docs/operacion/01_entornos_variables_y_perfiles.md`
3. `docs/operacion/02_pipeline_y_calidad_minima.md`
4. `docs/operacion/03_bootstrap_local_despliegue_y_storage.md`
5. `docs/operacion/04_runbooks_y_release_checklist.md`

### 4.10. Capa de trazabilidad final y calidad

Orden recomendado:

1. `docs/proyecto/11_matriz_trazabilidad_final_por_componente.md`
2. `docs/proyecto/12_registro_minimo_de_adrs.md`
3. `docs/calidad/00_calidad_y_pruebas_canonicas.md`
4. `docs/calidad/01_checklist_qa_por_componente.md`
5. `docs/calidad/02_checklist_release_enterprise.md`

### 4.11. Documentos auxiliares consolidados

Estos documentos no reemplazan la ruta canonica, pero pueden servir como
atajos de operacion, presentacion o auditoria:

1. `docs/operacion/06_inicio_rapido.md`
2. `docs/operacion/07_ejecucion_local_rapida.md`
3. `docs/operacion/08_ejecucion_con_docker.md`
4. `docs/calidad/03_auditoria_tecnica_v1.md`
5. `docs/ux-ui/02_guia_configuracion_imagenes.md`
6. `docs/presentacion/01_resumen_entrega_final.md`
7. `docs/presentacion/02_readme_propagandistico_github.md`

Nota de orden:

- los `.md` que siguen en la raiz de `docs/` funcionan como dossiers de apoyo,
  auditoria puntual o levantamiento preservado
- la ruta oficial para estudiar arquitectura e implementacion sigue siendo la
  de las capas canonicas listadas arriba

---

## 5. Ingenieria por componente que debe quedar explicita

### 5.1. Base de datos

La DB debe seguir siendo un componente especifico y de precision.

Debe dejar claro:

- modelo relacional final
- diccionario de datos
- constraints
- catalogos
- seeds
- migraciones
- y consultas operativas

### 5.2. Backend

El backend debe quedar especificado como componente con identidad propia.

Debe dejar claro:

- modulos de dominio
- organizacion DDD-lite pragmatica
- contratos API
- `ApiResponse` y errores
- auth y autorizacion
- validaciones
- auditoria
- SSE donde aplique
- y testing minimo

### 5.3. Frontend publico

Debe dejar claro:

- objetivo comercial y de captacion
- version de runtime y framework
- rutas y secciones
- papel del catalogo
- papel del cotizador
- contenido minimo
- estados de formulario
- build, SEO y accesibilidad
- y assets visuales

### 5.4. Frontend administrativo

Debe dejar claro:

- version de runtime y framework
- shell de aplicacion
- navegacion
- CRUDs y consultas
- formularios y tablas
- feedback
- auth
- manejo de `ApiResponse`
- y reglas de operacion

### 5.5. Panel de produccion

Debe dejar claro:

- estados operativos
- prioridad
- observaciones
- flujo de actualizacion
- densidad visual
- y consumo de actualizaciones en tiempo real

### 5.6. Infraestructura enterprise

Debe dejar claro:

- worker y cola de reportes
- scheduler y jobs periodicos
- auditoria de negocio
- notificaciones internas
- storage de archivos
- concurrencia e idempotencia
- y criterio oficial sobre extensiones diferidas del sistema

---

## 6. Regla de fuente de verdad

Cuando existan temas repetidos, se aplica esta regla:

- negocio y proyecto definen alcance y reglas de alto nivel
- DB define schema, integridad y catalogos
- backend define contratos, errores, seguridad y consumo tecnico
- UX/UI define presentacion, jerarquia y patrones de interaccion

No deben convivir dos verdades funcionales contradictorias.

---

## 7. Regla de referencia inteligente

Si este proyecto necesita resolver una duda de arquitectura, documentacion, seguridad, operacion o incluso un fragmento puntual de implementacion, se puede consultar como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

Esas referencias pueden servir para:

- patrones de componente
- estructura documental
- ideas de runbooks
- pruebas
- observabilidad
- seguridad
- y trozos de implementacion

Regla importante:

- se toma la referencia
- pero siempre manda el dominio y el alcance de Pasteleria

---

## 8. Artefactos no codigo que tambien deben tratarse como ingenieria

En este proyecto tambien deben documentarse y cuidarse como parte formal del sistema:

- `README`
- `.gitignore`
- `.env.example`
- `docker-compose.yml`
- `Dockerfile`
- guias de despliegue
- checklists de release
- runbooks

No son anexos decorativos. Son parte de la mantenibilidad, la seguridad y la reproducibilidad del proyecto.

---

## 9. Vacios que conviene cerrar despues

La base documental del proyecto ya es fuerte, pero para dejarlo listo para piloto automatico con IA conviene agregar luego:

- auditoria formal de negocio mas detallada
- observabilidad mas profunda cuando exista codigo real
- automatizacion de pruebas una vez arranque la implementacion
- y politicas de backup o restore mas concretas si aparecen archivos persistidos

---

## 10. Resumen ejecutivo

La documentacion de Pasteleria ya no debe verse solo como una coleccion de markdowns, sino como una ruta canonica:

- primero se entiende el negocio
- luego se congela el sistema
- despues se baja a DB, backend y frontends
- y finalmente se completa la capa operativa

Ese orden reduce contradicciones, ensena mejor la arquitectura y deja el proyecto mucho mas listo para implementacion asistida por IA.

---

## 11. Actualización de cierre T12

Documento incorporado a la ruta de producto:

- `docs/proyecto/14_guia_operativa_integrada.md`

Este documento explica la integración completa de la guía operativa entre base de datos, backend, Angular y datos semilla.

Estado de cierre: las tandas técnicas 0 a 12 quedaron aplicadas. No hay tandas pendientes dentro de `tandas-pendientes/`.
