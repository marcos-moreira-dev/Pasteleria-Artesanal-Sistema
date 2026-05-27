# 02 - Checklist release enterprise

## 1. Proposito

Este documento convierte el release en un proceso repetible y no en una memoria personal.

---

## 2. Gate tecnico previo

1. Confirmar version de Java, Node y dependencias congeladas.
2. Confirmar variables y secretos por entorno.
3. Confirmar migraciones Flyway aplicadas.
4. Confirmar seeds requeridos para la presentación funcional o entorno objetivo.
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

Si un release no supera este checklist, puede ser una entrega improvisada, pero todavia no es una entrega profesional.

---

## 7. Gate agregado T11/T12

Antes de considerar cerrada esta versión, validar también:

1. La ruta `/guia-operativa` carga desde el admin.
2. El endpoint `GET /api/v1/casos-uso/hub` responde con `ApiResponse.success = true`.
3. Los seeds canónicos incluyen módulos, guías y pasos.
4. Astro compila con `npm run build`.
5. El README menciona guía operativa, abastecimiento, notificaciones y scripts de validación.
6. La carpeta `tandas-pendientes/` no conserva tareas pendientes ya aplicadas.
7. No quedan archivos temporales de validación dentro de carpetas que deban publicarse.
