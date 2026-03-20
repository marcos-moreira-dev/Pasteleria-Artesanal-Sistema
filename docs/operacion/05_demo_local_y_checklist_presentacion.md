# Demo Local Y Checklist De Presentacion

## Objetivo

Dejar una ruta simple para mostrar `Pasteleria` como producto funcional antes de entrar a una demo mas propagandistica con capturas, logo y README final de exhibicion.

## Orden sugerido de demo

1. `.\scripts\init-db.ps1`
2. `cd backend && .\scripts\start-backend-dev.cmd`
3. `cd frontend-publico-astro && npm run dev`
4. `cd frontend-admin-angular && npm start`

## Recorrido sugerido

### 1. Landing publica

- abrir `/`
- mostrar mensaje comercial principal
- navegar a `Catalogo`
- navegar a `Contacto`
- enviar una solicitud publica de cotizacion

### 2. Panel administrativo

- abrir `/login`
- entrar con `admin`
- revisar resumen general
- registrar un cliente
- registrar una cotizacion
- registrar un pedido
- mover pedido a preparacion o entrega
- mover produccion por `PREPARACION`, `DECORACION`, `EMPAQUE` y `FINALIZADO`

### 3. Backend

- mostrar Swagger
- mostrar endpoint de health
- explicar modulos principales
- explicar JWT, auditoria y estructura modular

## Credenciales demo

- usuario: `admin` / contrasena: `admin12345`
- usuario: `atencion1`
- usuario: `produccion1`

Nota:
La cuenta estable de arranque para demo es `admin`.

## Checklist previo a capturas

- landing sin textos rotos ni mojibake
- espanol con acentos correctos donde corresponda
- rutas `es/en` operativas en el publico
- backend arriba y respondiendo con Java 21
- catalogo visible
- login funcional
- flujo basico de cliente, cotizacion, pedido y produccion probado
- README raiz actualizado
- variables de entorno revisadas

## Checklist para README propagandistico final

- logo real del negocio
- capturas de landing
- capturas de catalogo
- capturas del admin
- stack resumido
- arquitectura resumida
- enlace a documentacion canonica
- nota de alcance V1
