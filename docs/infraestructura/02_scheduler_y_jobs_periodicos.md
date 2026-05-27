# 02 - Scheduler y jobs periodicos

## 1. Proposito

El scheduler resuelve tareas que deben ejecutarse por tiempo y no por accion directa del usuario.

En Spring Boot 4 puede materializarse con `@Scheduled`, pero arquitectonicamente debe leerse como componente propio.

---

## 2. Jobs minimos de V1

### 2.1. Limpieza de temporales

- elimina archivos temporales expirados
- limpia registros de descarga vencidos

### 2.2. Recordatorio de cotizaciones pendientes

- identifica cotizaciones sin atencion dentro del umbral definido
- dispara notificacion interna

### 2.3. Consolidacion nocturna simple

- recalcula metricas resumidas ligeras
- actualiza tablas o vistas de apoyo cuando aplique

### 2.4. Expiracion de enlaces o tokens operativos

- invalida recursos temporales vencidos
- deja trazabilidad de ejecucion

### 2.5. Limpieza de jobs viejos

- archiva o expira jobs de reporte ya descargados fuera de la ventana de retencion

---

## 3. Modelo de trazabilidad recomendado

Tablas o registros sugeridos:

- `scheduler_job_definicion`
- `scheduler_job_ejecucion`

Campos utiles:

- codigo del job
- ultima ejecucion
- siguiente ejecucion calculada
- resultado
- duracion
- mensaje resumido

En V1 puede simplificarse a una sola tabla de ejecucion si no se necesita administrar jobs desde UI.

---

## 4. Reglas de diseno

- cada job debe ser idempotente
- cada job debe tener nombre y proposito claro
- no debe mezclar demasiadas responsabilidades
- si un job afecta mucho volumen, debe delegar trabajo pesado a la cola
- la configuracion de frecuencia debe poder moverse a `application.yml` o parametros

---

## 5. Control de concurrencia del scheduler

Si solo hay una instancia, basta una disciplina simple.

Si el sistema escala a varias instancias, se debe introducir:

- lock por BD
- o tabla de reclamo de ejecucion

para evitar doble corrida del mismo job.

---

## 6. Observabilidad minima

Cada corrida debe registrar:

- inicio
- fin
- resultado
- duracion
- cantidad procesada
- errores resumidos

---

## 7. Pruebas superficiales que deben existir

- el job se ejecuta segun la frecuencia esperada
- no reprocesa elementos ya expirados
- registra ejecucion exitosa y fallida
- no genera duplicados cuando se reintenta
