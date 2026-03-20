# 00 - DB canonica y criterios de moderacion

## 1. Proposito

Este documento fija la linea canonica de la base de datos de Pasteleria para que:

- sea realista,
- sea moderada,
- no se quede corta,
- y no se infle artificialmente.

---

## 2. Criterio de moderacion

La DB de Pasteleria debe sentirse como una base de datos comun de negocio real.

Eso significa:

- suficientes entidades para soportar clientes, productos, pedidos, cotizaciones, produccion, usuarios y reportes
- relaciones naturales de negocio
- catalogos utiles
- constraints reales

No significa:

- multiplicar tablas sin valor operativo
- o convertir cada atributo secundario en una entidad nueva solo por academicismo

---

## 3. Secuencia documental obligatoria

La capa DB debe leerse en este orden:

1. modelo conceptual del dominio
2. modelo conceptual para persistencia
3. segunda forma normal (2FN)
4. tercera forma normal (3FN)
5. modelo logico relacional oficial
6. diccionario de datos
7. reglas de integridad y constraints
8. catalogos y estados
9. indices y consultas clave
10. migraciones
11. datos semilla y escenarios de prueba

---

## 4. Entidades base que justifican una DB realista

Como linea general, las entidades nucleares de Pasteleria deben gravitar alrededor de:

- cliente
- producto
- categoria
- pedido
- detalle de pedido
- cotizacion
- detalle de cotizacion
- produccion
- usuario
- rol o control de acceso
- catalogos de estado
- reportes o solicitudes de reporte si aplica

Ese rango es lo bastante comun para parecerse a sistemas reales y lo bastante controlado para una V1 seria.

---

## 5. Integridad minima obligatoria

La documentacion DB debe dejar claramente definidos:

- PK
- FK
- `UNIQUE`
- `CHECK`
- nulabilidad coherente
- auditoria minima

Ejemplos de integridad relevantes:

- categoria valida para producto
- producto activo para nuevas operaciones
- pedido con detalle coherente
- cotizacion convertible solo una vez
- transiciones de estado controladas por backend y sostenidas documentalmente en DB

---

## 6. Pruebas minimas de consistencia recomendadas

Aunque por ahora sean superficiales, deben anticiparse pruebas como:

- insercion valida de cliente
- insercion de pedido con detalle
- violacion de unicidad
- relacion inexistente
- cotizacion ya convertida
- consulta de pedidos pendientes
- consulta de produccion pendiente

Esas pruebas luego pueden codificarse como `TC-xx`.

---

## 7. Regla de referencia inteligente

La capa DB puede apoyarse en:

- `C:\Users\MARCOS MOREIRA\Downloads\estandar_modelado_db_moderado_y_codigos_documentales.md`

Y como referencia inteligente:

- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Proyecto tienda Electronica promedio`

Eso puede ayudar con:

- diccionario
- constraints
- seeds
- naming
- y checklist de consistencia
