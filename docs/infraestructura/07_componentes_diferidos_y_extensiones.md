# 07 - Componentes diferidos y extensiones

## 1. Proposito

Este documento deja por escrito que componentes se difieren con criterio en Pasteleria sin abrir lineas de complejidad innecesarias.

---

## 2. Componentes diferidos pero validos

Se dejan previstos para una fase posterior:

- cache para catalogos y parametros
- outbox pattern
- importacion y exportacion masiva
- read models o tablas resumen
- motor de reglas mas parametrizable

Todos son validos para estudiar, pero no bloquean la V1.

---

## 3. Regla recomendada

Pasteleria debe mantenerse en una arquitectura relacional, modular y contenida.

Eso significa:

- PostgreSQL como unica fuente de verdad
- extensiones solo cuando un problema real lo justifique
- y sin introducir subsistemas de soporte/tickets ni persistencias paralelas en este proyecto
