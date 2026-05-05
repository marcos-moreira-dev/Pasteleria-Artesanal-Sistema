# Scripts backend

Scripts locales para ejecutar y validar el backend Spring Boot. Esta carpeta sigue el patrón de Cedro Damasco: los scripts específicos de backend delegan en los scripts estabilizados de la raíz cuando corresponde.

- `run-backend-dev.bat`: delega en `scripts\dev-backend.bat` de la raíz.
- `run-backend-presentation.bat`: levanta PostgreSQL dev, inicializa la base canónica y ejecuta el backend con perfil `dev` preparado para presentación local.
- `test-backend.bat`: compila el backend y ejecuta pruebas si existen.
- `package-backend.bat`: empaqueta el backend.
- `seed-presentation.bat`: delega en la inicialización canónica de la base.
- `reset-presentation-data.bat`: recrea la base canónica local.
- `clean-generated-files.bat`: limpia artefactos generados de backend.

Los scripts de infraestructura general viven en `scripts/` en la raíz del repositorio.

Evita usar `mvn spring-boot:run` directo como primer intento en Windows. Los scripts estabilizados fijan credenciales locales, levantan PostgreSQL dev y evitan el choque entre SQL canónico y Flyway dejando `SPRING_FLYWAY_ENABLED=false`.

PostgreSQL dev se publica en `localhost:5436`. El backend local usa `localhost:8080`.
