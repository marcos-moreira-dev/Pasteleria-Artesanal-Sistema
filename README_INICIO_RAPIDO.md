# 🚀 Guía de Inicio Rápido - Pastelería System

## ✅ Estado Actual: LISTO PARA USAR

**Última actualización:** 2026-03-20
**Versión DB:** 4.0 JPA
**Estado:** Schema 100% alineado con entidades Java

---

## 📋 Pre-requisitos

- [ ] PostgreSQL 14+ instalado (puerto 5432)
- [ ] Java 21 (Eclipse Temurin) - Configurado en Maven Toolchain
- [ ] Node.js 18+
- [ ] Maven (wrapper incluido)

---

## 🗄️ Paso 1: Base de Datos (¡Importante!)

### Eliminar y recrear la base de datos:

```bash
# En pgAdmin o psql:
DROP DATABASE IF EXISTS pasteleria;
CREATE DATABASE pasteleria;

# Ejecutar el script maestro (¡NUEVO!):
psql -h localhost -U postgres -d pasteleria -f "db/V1/DATABASE_MASTER_V4_JPA.sql"
```

**Archivo a usar:** `db/V1/DATABASE_MASTER_V4_JPA.sql` (NO usar el archivo viejo)

---

## 🔧 Paso 2: Backend

```bash
cd backend

# Compilar (usa Java 21 automáticamente via Maven Toolchain)
mvnw.cmd clean compile -q

# Iniciar servidor
.\scripts\start-backend-dev.cmd
```

**Verificación:** Debe decir `Started PasteleriaApplication` sin errores.

**Swagger UI:** http://localhost:8080/swagger-ui.html

---

## 🎨 Paso 3: Frontend

```bash
cd frontend-admin-angular

# Instalar dependencias (si es primera vez)
npm install

# Iniciar servidor de desarrollo
npm start
```

**Aplicación:** http://localhost:4200

**Login:**

- Usuario: `admin`
- Password: `admin123`

---

## 📁 Estructura de Archivos Importantes

```
Pastelería/
├── db/V1/
│   └── DATABASE_MASTER_V4_JPA.sql    ✅ Schema actualizado (USAR ESTE)
├── backend/
│   ├── scripts/start-backend-dev.cmd  ✅ Configurado para puerto 5432
│   └── src/main/resources/
│       └── application.yml            ✅ Flyway disabled, validate mode
└── frontend-admin-angular/
    └── src/app/
        └── features/                  ✅ Interfaces limpias
```

---

## 🔐 Configuración de Conexión

**PostgreSQL:**

- Host: `localhost`
- Puerto: `5432`
- Database: `pasteleria`
- User: `postgres`
- Password: `XXXX` password de postgres

**Backend (.env implícito):**
Todas las variables están en `scripts/start-backend-dev.cmd`

---

## ✅ Verificación Post-Inicio

1. **Backend:** Sin errores en consola
2. **Frontend:** Login carga correctamente
3. **Login:** admin/admin123 funciona
4. **Módulos:** Productos y Abastecimiento cargan datos

---

## 🛠️ Solución de Problemas

### Error: "Schema-validation: missing column"

**Solución:** Recrear la base de datos con el script V4.

### Error: "Rule 0: org.apache.maven.enforcer.rules.version.RequireJavaVersion"

**Solución:** El enforcer acepta Java 21+. Si tienes otra versión, instala Eclipse Temurin 21.

### Error: "column X does not exist"

**Solución:** La base de datos está desactualizada. Recrearla con el script V4.

---

## 📝 Notas Técnicas

- **Flyway:** Deshabilitado (`enabled: false`) para evitar conflictos
- **JPA:** Modo `validate` (solo valida, no modifica schema)
- **Schema:** Generado automáticamente desde entidades JPA
- **Seeds:** Incluye datos iniciales (roles, admin, unidades de medida, etc.)

---

## 🎯 Estado de Módulos

| Módulo         | Estado       | Notas                              |
| -------------- | ------------ | ---------------------------------- |
| Autenticación  | ✅ Funcional | JWT implementado                   |
| Productos      | ✅ Funcional | Con recetas JSON                   |
| Abastecimiento | ✅ Funcional | Insumos, ingredientes, proveedores |
| Pedidos        | ✅ Listo     | Estructura completa                |
| Producción     | ✅ Listo     | Estructura completa                |
| Reportes       | ✅ Listo     | Jobs asíncronos configurados       |

---

## 🧹 Limpieza Realizada

✅ Schema DB 100% alineado con JPA Entities
✅ Eliminados comentarios y código temporal
✅ Script único maestro con seeds
✅ Maven Toolchain configurado
✅ Application.yml limpio (validate mode)
✅ Frontend interfaces sin duplicados

---

**¡Sistema listo para desarrollo!** 🎉
