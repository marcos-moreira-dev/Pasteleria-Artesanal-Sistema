# 02 - Checklist release enterprise

## 1. Proposito

Este documento convierte el release en un proceso repetible y no en una memoria personal.

---

## 2. Gate tecnico previo

1. Confirmar version de Java, Node y dependencias congeladas.
2. Confirmar variables y secretos por entorno.
3. Confirmar migraciones Flyway aplicadas.
4. Confirmar seeds requeridos para la demo o entorno objetivo.
5. Confirmar builds de backend, frontend publico y frontend admin.

---

## 3. Gate funcional minimo

1. Validar login administrativo.
2. Validar gestion de clientes.
3. Validar catalogo y productos.
4. Validar cotizador publico.
5. Validar conversion de cotizacion a pedido.
6. Validar panel de produccion.
7. Validar entrega de pedido.
8. Validar reportes o exportaciones criticas.

---

## 4. Gate operativo

1. Verificar rutas de storage y permisos.
2. Verificar health check backend.
3. Verificar logs y request id si ya existen.
4. Verificar estrategia minima de rollback.
5. Verificar que exista responsable de soporte inicial.

---

## 5. Gate documental

1. README alineado con la version real.
2. Si la version ya es publicable, README con logo al inicio y capturas reales del sistema.
3. `.env.example` actualizado.
4. runbooks minimos vigentes.
5. matriz de trazabilidad sin contradicciones conocidas.
6. ADRs actualizadas si hubo cambio estructural real.

---

## 6. Regla final

Si un release no supera este checklist, puede ser una demo improvisada, pero todavia no es una entrega profesional.
