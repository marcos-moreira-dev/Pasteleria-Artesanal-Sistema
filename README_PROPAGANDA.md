# 🧁 Pastelería Artesanal - Sistema de Gestión Integral

[![Stack](https://img.shields.io/badge/Spring%20Boot-4.0-success?style=flat-square&logo=spring)](https://spring.io/)
[![Angular](https://img.shields.io/badge/Angular-21-dd0031?style=flat-square&logo=angular)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ed?style=flat-square&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-AGPL--3.0-blue?style=flat-square)](LICENSE)

**Sistema completo para gestión de pastelería:** catálogo público, administración de pedidos, control de producción y gestión de inventario. Diseñado para operaciones reales del norte de Guayaquil.

<p align="center">
  <img src="assets-readme/logo/logo-principal-placeholder.md" alt="Logo Pastelería" width="200"/>
</p>

## ✨ ¿Qué hace este sistema?

Pastelería es un **producto de software empresarial** que conecta todos los puntos de operación de una pastelería artesanal:

- 🌐 **Landing Pública** - Catálogo bilingüe (es/en) con cotizador integrado
- 🔐 **Panel Administrativo** - Gestión de pedidos, clientes y producción en tiempo real  
- 📦 **Control de Inventario** - Abastecimiento, recetas y trazabilidad
- 🏭 **Flujo de Producción** - Pipeline visual desde pedido hasta entrega

No es una demo aislada. Es una **referencia de arquitectura completa** para estudiar cómo conectar negocio, datos, API y experiencia de usuario.

---

## 🎯 Características Destacadas

### 🌍 Experiencia Pública
- Catálogo de productos con imágenes y descripciones
- Solicitud de cotizaciones personalizadas
- Información de contacto y ubicación
- Soporte bilingüe español/inglés

### 📊 Panel Administrativo
- Dashboard con métricas de ventas y producción
- Gestión completa de clientes y pedidos
- Control de producción con etapas visuales
- Subida de imágenes de productos
- Descarga de recetas en PDF

### 🏭 Producción Inteligente
- Pipeline visual: Pendiente → Preparación → Decoración → Empaque → Finalizado
- Fijado de pedidos prioritarios
- Retroceso de estados (correcciones)
- Notificaciones automáticas

### 📦 Abastecimiento
- Inventario de ingredientes e insumos
- Recetas vinculadas a productos
- Órdenes de compra a proveedores
- Alertas de stock bajo

---

## 🖼️ Galería Visual

### Landing Page - Home

![Landing Home](assets-readme/capturas/01-landing-home-placeholder.md)

Página de inicio pública con catálogo destacado y acceso rápido a cotizaciones.

### Catálogo de Productos

![Catálogo](assets-readme/capturas/02-catalogo-placeholder.md)

Vitrina digital con filtros, imágenes y solicitud de cotización integrada.

### Contacto y Cotizaciones

![Contacto](assets-readme/capturas/03-contacto-placeholder.md)

Formulario de contacto y cotizador público para clientes potenciales.

### Admin Dashboard

![Dashboard](assets-readme/capturas/04-admin-dashboard-placeholder.md)

Panel de control con métricas: caja del día, ritmo semanal, ticket promedio y metas.

### Gestión de Clientes

![Clientes](assets-readme/capturas/05-admin-clientes-placeholder.md)

CRUD completo de clientes con historial de pedidos y notas.

### Pedidos y Cotizaciones

![Pedidos](assets-readme/capturas/06-admin-pedidos-placeholder.md)

Listado de pedidos con filtros, estados y transiciones de flujo.

### Control de Producción

![Producción](assets-readme/capturas/07-admin-produccion-placeholder.md)

Pipeline visual de producción con seguimiento de etapas y pedidos fijados.

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    PASTELERÍA APP                       │
├─────────────────────────────────────────────────────────┤
│  🌐 Landing (Astro)    │  🔐 Admin (Angular)            │
│  - Público             │  - Autenticado                │
│  - Catálogo            │  - Operaciones                │
│  - Cotizador           │  - Reportes                   │
└──────────┬─────────────┴──────────────┬────────────────┘
           │                            │
           └──────────┬─────────────────┘
                      │
           ┌──────────▼──────────────────┐
           │    Backend API (Spring)     │
           │  - REST API                 │
           │  - JWT Security             │
           │  - Business Logic           │
           └──────────┬──────────────────┘
                      │
           ┌──────────▼──────────────────┐
           │    PostgreSQL Database      │
           │  - Relational Model         │
           │  - Flyway Migrations        │
           └─────────────────────────────┘
```

### Stack Tecnológico

| Capa | Tecnología | Versión |
|------|-----------|---------|
| **Backend** | Spring Boot | 4.0 |
| **Frontend Admin** | Angular | 21.x |
| **Frontend Público** | Astro | 5.x |
| **Base de Datos** | PostgreSQL | 17 |
| **Migraciones** | Flyway | Latest |
| **Contenerización** | Docker | 20.x+ |
| **Java** | Eclipse Temurin | 21 |
| **Node.js** | LTS | 22.12+ |

---

## 📚 Documentación Técnica

### 📖 Negocio y Análisis
- [Levantamiento de información](docs/negocio/01_levantamiento_informacion_negocio.md)
- [Requerimientos funcionales](docs/negocio/02_levantamiento_requerimientos.md)
- [Modelo de datos](docs/base-datos/base_datos_00_modelo_logico_relacional.md)
- [Reglas de negocio](docs/negocio/04_reglas_negocio_y_supuestos.md)

### 💻 Backend
- [Arquitectura y módulos](docs/backend/backend_00_vision_backend_y_modulos.md)
- [Diseño de API REST](docs/backend/backend_02_diseno_de_api_rest.md)
- [DTOs y contratos](docs/backend/backend_03_dtos_y_contratos_api.md)
- [Seguridad y JWT](docs/backend/backend_04_seguridad_testing_y_operacion.md)

### 🎨 Frontend
- [Arquitectura Admin Angular](docs/frontend-admin-angular/00_frontend_admin_canonico.md)
- [Rutas y módulos](docs/frontend-admin-angular/01_frontend_admin_shell_rutas_y_modulos.md)
- [Landing Astro](docs/frontend-publico-astro/00_frontend_publico_canonico.md)

### 📊 API Reference
- [Documentación completa de API](docs/API_DOCUMENTACION_COMPLETA.md)
- Swagger UI: `http://localhost:8081/swagger-ui.html`

---

## 🚀 Cómo Empezar

### ⚡ Opción Rápida (Docker)

```bash
# 1. Clonar repositorio
git clone <repo-url>
cd Pastelería

# 2. Iniciar con Docker Compose
docker-compose up -d

# 3. Acceder
# Admin: http://localhost:4200
# Landing: http://localhost:4321
# API Docs: http://localhost:8081/swagger-ui.html
```

**Credenciales por defecto:**
- Usuario: `admin`
- Password: `admin12345`

### 🔧 Desarrollo Local

**Requisitos:**
- Java 21 (Eclipse Temurin)
- Node.js 22.12+
- PostgreSQL 17
- Maven 4.0+

**Backend:**
```bash
cd backend
mvn clean package -DskipTests
java -jar target/pasteleria-backend-0.0.1-SNAPSHOT.jar
```

**Frontend Admin:**
```bash
cd frontend-admin-angular
npm install
npm start
```

**Frontend Landing:**
```bash
cd frontend-landing-astro
npm install
npm run dev
```

📖 **Guía detallada:** [README_EJECUCION_DOCKER.md](README_EJECUCION_DOCKER.md)

---

## 🧪 Testing

### Backend (JUnit 5)
```bash
cd backend
mvn test
```

### Frontend (Jasmine/Karma)
```bash
cd frontend-admin-angular
npm test
```

### E2E (Cypress)
```bash
cd frontend-admin-angular
npm run e2e
```

---

## 📦 Estructura del Proyecto

```text
Pastelería/
├── 📁 backend/                    # Spring Boot API
│   ├── src/main/java/            # Código fuente
│   ├── src/main/resources/       # Configuración
│   └── src/test/                 # Tests unitarios
│
├── 📁 frontend-admin-angular/     # Panel administrativo
│   ├── src/app/                  # Módulos Angular
│   ├── src/assets/               # Recursos estáticos
│   └── src/environments/         # Configuración
│
├── 📁 frontend-landing-astro/     # Landing page pública
│   ├── src/                      # Páginas Astro
│   └── public/                   # Assets públicos
│
├── 📁 db/                         # Base de datos
│   ├── V1/migrations/            # Migraciones Flyway
│   └── V1/seeds/                 # Datos de demo
│
├── 📁 docs/                       # Documentación
│   ├── negocio/                  # Análisis de negocio
│   ├── backend/                  # Docs backend
│   ├── frontend-admin-angular/   # Docs admin
│   └── frontend-publico-astro/   # Docs landing
│
├── 📁 assets-readme/              # Imágenes para README
│
├── docker-compose.yml             # Orquestación Docker
└── README.md                      # Este archivo
```

---

## 🔐 Configuración de Secretos

### Desarrollo Local

1. Copiar archivo de ejemplo:
   ```bash
   cp backend/.env.example backend/.env
   ```

2. Editar `backend/.env` con valores locales:
   ```env
   DB_PASSWORD=tu_password_seguro
   JWT_SECRET=tu_clave_secreta_jwt
   ```

3. **Nunca** subir `.env` al repositorio.

### Producción

Usar variables de entorno del sistema o gestor de secretos:
- Docker Secrets
- Kubernetes Secrets
- AWS Secrets Manager
- Azure Key Vault

---

## 🌟 Características Premium

- ✅ **Arquitectura Limpia** - Separación de responsabilidades clara
- ✅ **API RESTful** - Contratos bien definidos con Swagger
- ✅ **Seguridad JWT** - Autenticación stateless con tokens renovables
- ✅ **Responsive Design** - Adaptable a móviles y tablets
- ✅ **Trazabilidad Completa** - Auditoría de todas las operaciones
- ✅ **Reportes Asíncronos** - Generación de reportes sin bloquear UI
- ✅ **Notificaciones** - Sistema interno de notificaciones por rol
- ✅ **Dockerizado** - Listo para despliegue con un comando

---

## 🤝 Contribuir

1. Fork el repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

### Estándares de Código
- Java: [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- TypeScript: [Angular Style Guide](https://angular.io/guide/styleguide)
- Commits: [Conventional Commits](https://www.conventionalcommits.org/)

---

## 📄 Licencia

Este proyecto está licenciado bajo **AGPL-3.0-or-later**.

- [LICENSE](LICENSE) - Licencia principal
- [LICENSE Backend](backend/LICENSE)
- [LICENSE Frontend Admin](frontend-admin-angular/LICENSE)

## 🎓 Uso Educativo

Este sistema fue desarrollado con fines educativos y como referencia de portafolio técnico. Su valor principal es mostrar un flujo completo de construcción de producto software empresarial.

**Aprendizajes clave:**
- Análisis de requerimientos desde cero
- Modelado de datos relacional
- Arquitectura de microservicios/modular
- Desarrollo full-stack moderno
- Docker y orquestación
- Testing y calidad de código

---

## 📞 Contacto y Soporte

**Proyecto Académico** - Referencia técnica para estudio

Para preguntas técnicas o sugerencias:
- 📧 Email: [tu-email@ejemplo.com]
- 💼 LinkedIn: [tu-perfil]
- 🐙 GitHub: [tu-usuario]

---

<p align="center">
  <strong>Desarrollado con ❤️ y mucho ☕</strong>
</p>

<p align="center">
  <sub>Hecho en Guayaquil, Ecuador 🇪🇨</sub>
</p>
