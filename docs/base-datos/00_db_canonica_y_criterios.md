# 00 - DB canonica y criterios de moderacion

## 1. Proposito

Este documento fija la linea canonica de la base de datos de Pasteleria para
que sea:

- realista
- moderada
- coherente con el codigo
- y util para estudio serio

---

## 2. Cobertura funcional actual de la DB

La DB canonica no cubre solo ventas. Hoy soporta estas areas:

- acceso y usuarios
- clientes
- catalogo de productos y categorias
- cotizaciones
- pedidos
- produccion
- ingredientes e insumos
- recetas
- proveedores
- ordenes de compra
- movimientos de inventario
- archivos
- jobs de reporte
- notificaciones
- auditoria

La referencia manual oficial para bootstrap completo es:

- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`

---

## 3. Criterio de moderacion

La DB de Pasteleria debe sentirse como una base de datos de negocio real.

Eso significa:

- suficientes entidades para soportar operacion comercial, produccion y abastecimiento
- relaciones naturales de negocio
- catalogos utiles
- constraints reales

No significa:

- multiplicar tablas sin valor operativo
- convertir cada atributo secundario en entidad por academicismo

---

## 4. Secuencia documental obligatoria

La capa DB debe leerse en este orden:

1. modelo conceptual del dominio
2. modelo conceptual para persistencia
3. segunda forma normal
4. tercera forma normal
5. modelo logico relacional oficial
6. diccionario de datos
7. reglas de integridad y constraints
8. catalogos y estados
9. indices y consultas clave
10. migraciones
11. datos semilla y escenarios de prueba

---

## 5. Integridad minima obligatoria

La documentacion DB debe dejar definidos:

- PK
- FK
- `UNIQUE`
- `CHECK`
- nulabilidad coherente
- auditoria minima

Ejemplos relevantes:

- categoria valida para producto
- producto activo para nuevas operaciones
- cotizacion convertible una sola vez
- pedido con detalle coherente
- orden de compra y recepcion coherentes
- movimientos de inventario referenciables

---

## 6. Temas computacionales que debes dominar aqui

Si quieres estudiar esta capa como profesional, los temas mas importantes son:

- modelado relacional
- normalizacion con criterio
- claves, constraints e integridad referencial
- indices y consultas operativas
- seeds coherentes con el codigo
- migraciones y versionado de schema
- modelado de catalogos y estados
- relacion entre dominio, JPA y PostgreSQL

---

## 7. Pruebas minimas de consistencia recomendadas

Deben anticiparse pruebas como:

- insercion valida de cliente
- insercion de pedido con detalle
- violacion de unicidad
- relacion inexistente
- cotizacion ya convertida
- recepcion de orden de compra
- consulta de produccion pendiente
- consulta de inventario y movimientos
