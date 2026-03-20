# 10 - Convenciones de codificacion y trazabilidad

## 1. Proposito

Este documento fija una convencion profesional de codigos documentales para que Pasteleria tenga trazabilidad real entre:

- negocio
- producto
- DB
- backend
- frontend
- operacion
- y pruebas

---

## 2. Codigos oficiales recomendados

### 2.1. Negocio y producto

- `RF-01` Requerimiento funcional
- `RN-01` Regla de negocio
- `CU-01` Caso de uso
- `DA-01` Decision de arquitectura
- `CFS-01` Contrato funcional del sistema

### 2.2. DB

- `ENT-01` Entidad principal
- `CAT-01` Catalogo
- `REL-01` Relacion critica
- `RI-01` Regla de integridad
- `IDX-01` Indice importante

### 2.3. Backend, frontend y operacion

- `API-01` Operacion o endpoint importante
- `PUB-01` Ruta, pantalla o flujo publico relevante
- `ADM-01` Pantalla o flujo administrativo relevante
- `REP-01` Reporte o consulta relevante
- `OPS-01` Regla operativa o runbook
- `TC-01` Caso de prueba
- `ADR-01` Decision arquitectonica registrada formalmente

---

## 3. Regla de uso

1. Los codigos deben mantenerse estables.
2. No se crean codigos por decorar.
3. Un mismo concepto no debe cambiar de codigo entre documentos sin razon.
4. La matriz de trazabilidad debe poder conectar un `RF` con `RN`, `CU`, `ENT`, `API` y `TC` cuando aplique.
5. Se permiten subseries como `TC-BE-01`, `TC-FE-01` y `TC-E2E-01` siempre que conserven el prefijo `TC`.

---

## 4. Aplicacion practica en Pasteleria

Ejemplos razonables:

- `RF-01` Registrar cliente
- `RF-02` Registrar pedido
- `RN-05` Una cotizacion no puede convertirse dos veces
- `CU-03` Convertir cotizacion en pedido
- `ENT-04` Pedido
- `REL-03` Pedido a detalle de pedido
- `RI-02` Producto activo obligatorio para pedido nuevo
- `API-07` POST crear pedido
- `PUB-02` Cotizador publico
- `ADM-04` Pantalla de pedidos
- `REP-02` Pedidos pendientes
- `TC-08` Rechazar transicion invalida de estado

---

## 5. Referencia de apoyo

Para profundizar este criterio, puede tomarse como apoyo:

- `C:\Users\MARCOS MOREIRA\Downloads\estandar_modelado_db_moderado_y_codigos_documentales.md`

Y como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

---

## 6. Regla de comentarios, Javadoc y OpenAPI

Cuando exista codigo implementado, se espera:

- Javadocs utiles en casos de uso importantes, componentes de seguridad y piezas de infraestructura
- comentarios no triviales sobre reglas de negocio, concurrencia, integridad o decisiones tecnicas
- OpenAPI/Swagger alineado al contrato real del backend

No se esperan comentarios decorativos que solo describan lo evidente.
