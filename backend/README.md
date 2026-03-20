# Backend Pastelería

Backend central en `Spring Boot 4` para catálogo, clientes, cotizaciones, pedidos y producción.

## Qué ya cubre

- seguridad con `JWT`
- `ApiResponse` uniforme
- Flyway y esquema base PostgreSQL
- módulos de negocio iniciales
- auditoría básica de eventos operativos
- pruebas web iniciales

## Módulos actuales

- `auth`
- `usuarios`
- `clientes`
- `catalogos`
- `productos`
- `cotizaciones`
- `pedidos`
- `produccion`
- `reportes`
- `common`

## Assets estáticos (storage externo)

Las imágenes del catálogo, branding y placeholders viven en `storage/assets/` fuera del JAR.

```
backend/
├── storage/
│   └── assets/
│       ├── products/       ← imágenes de productos (slug.png)
│       ├── branding/       ← logo, banner
│       └── placeholders/   ← fallback cuando no hay imagen
├── src/
└── target/
    └── pasteleria-backend.jar  ← sin imágenes dentro (ligero)
```

**¿Por qué fuera del JAR?**

- Agregar/actualizar imágenes → solo copiar archivo, sin rebuild
- El repo versiona las imágenes junto con el código
- Evitar que el JAR crezca con binary assets

**Convención de nombres:**

- Imágenes de producto = slug del producto + extensión (`.png`, `.jpg`, `.webp`)
- Ejemplo: `torta-chocolate-mediana.png` → producto con `slug = "torta-chocolate-mediana"`

**URLs públicas:**

```
GET /assets/products/torta-chocolate-mediana.png
GET /assets/branding/logo-cuadrado.png
GET /assets/placeholders/product-placeholder.png
```

## Comando principal

`mvnw.cmd spring-boot:run`

## Arranque recomendado en desarrollo

`.\scripts\start-backend-dev.cmd`

## Arranque manual

```bash
java -jar target/pasteleria-backend-0.0.1-SNAPSHOT.jar
```

> Importante: ejecutar desde la carpeta `backend/` para que `./storage/` se resuelva correctamente.

## Nota de entorno

Procura ejecutar el wrapper con `Temurin 21`, no con un Java global distinto.
El script `start-backend-dev.cmd` ya fija `JAVA_HOME`, `DB_PORT` y `JWT_SECRET` de desarrollo para no depender del Java global de Windows.
Ademas, el `pom.xml` ya usa `maven-enforcer-plugin` para rechazar cualquier build fuera de `Java 21`.
