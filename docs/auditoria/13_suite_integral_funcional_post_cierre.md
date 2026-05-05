# Auditoría: suite integral funcional posterior al cierre

## Motivo

Después de las tandas T7-T12 y la alineación posterior de scripts estilo Cedro Damasco, se incorporó una suite más fuerte para validar el sistema de forma integral.

## Archivos agregados

- `scripts/validate-system-full.bat`
- `scripts/validate-system-full.ps1`
- `scripts/validate-functional-suite.bat`
- `scripts/run-backend-validation.ps1`
- `docs/calidad/15_suite_integral_validacion_funcional.md`

## Cobertura

La suite cubre entorno dev, PostgreSQL Docker dev, SQL canónico, tablas nuevas y anteriores, datos mínimos por módulo, package backend, arranque real de Spring Boot, login JWT, endpoints públicos, endpoints administrativos principales, endpoints de guía operativa, build Angular, build Astro y auditoría de assets.

## Criterio técnico

La prueba es principalmente funcional y de integración local. No reemplaza pruebas unitarias futuras, pero evita que un cierre visual o documental oculte fallos graves como tabla faltante, seed incompleto, backend que compila pero no arranca, credenciales rotas, endpoint principal caído, guía operativa sin datos o frontend que no compila.
