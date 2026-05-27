# Scripts y perfiles objetivo

La operación local de Pastelería adopta el criterio de Cedro: pocos comandos visibles para humanos y complejidad encapsulada.

## Comandos principales

```bat
scripts\dev.bat
scripts\pasteleria-demo.bat
scripts\test-backend.bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

## Infraestructura

```bat
scripts\up-infra.bat
scripts\down-infra.bat
scripts\reset-infra.bat
```

## Puertos oficiales

```text
PostgreSQL Docker: localhost:5436
Backend:           http://localhost:8080
Admin Angular:     http://localhost:4200
Storefront Astro:  http://localhost:4321
```

## Perfiles

| Perfil | Uso |
|---|---|
| dev | Desarrollo técnico local. |
| presentation | Presentación/SIT local con `storage-sit`. |
| staging | Ambiente cercano a producción, sin seeds de presentación. |
| prod | Producción endurecida. |

## Regla local temporal sobre Flyway

Desde T24, dev y presentation usan la línea SQL ejecutable actual del backend con Flyway apagado en el arranque local. El modo presentation agrega seeds SIT sobre `storage-sit`.

```text
SPRING_FLYWAY_ENABLED=false
```

## Storage

```text
storage/      -> desarrollo normal
storage-sit/  -> presentación/SIT
```
