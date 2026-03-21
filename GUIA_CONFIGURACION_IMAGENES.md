# 📚 Guía de Configuración - Imágenes de Productos

## ¿Qué es esto?

Este archivo explica cómo funciona el sistema de imágenes de productos y qué configuraciones importantes debes conocer.

---

## 🖼️ Sistema de Imágenes

### ¿Cómo funciona?

1. **Subida de imágenes**: Cuando subes una foto de un producto, el backend la guarda automáticamente
2. **Renombrado**: La imagen se renombra al "slug" del producto (nombre normalizado)
   - Ejemplo: Producto "Guagua de Pan" → slug: `guagua-de-pan` → archivo: `guagua-de-pan.png`
3. **Ubicación**: Las imágenes se guardan en: `backend/storage/assets/products/`
4. **Visualización**: El sistema busca la imagen por el slug del producto

### Flujo completo:
```
1. Creas un producto → Se genera el slug automáticamente
2. Editas el producto → Subes la imagen
3. Backend renombra y guarda: {slug}.png
4. Producto muestra la imagen correcta
```

---

## ⚙️ Configuración Técnica (application-docker.yml)

### Límite de tamaño de imágenes

**Ubicación**: `backend/src/main/resources/application-docker.yml`

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 25MB      # Tamaño máximo de cada imagen
      max-request-size: 25MB   # Tamaño máximo total de la petición
```

**También en**: `backend/src/main/java/com/pasteleria/productos/application/ProductImageService.java`

```java
private static final long MAX_FILE_SIZE = 25 * 1024 * 1024; // 25MB
```

**⚠️ IMPORTANTE**: Ambos valores DEBEN coincidir. Si cambias uno, cambia el otro también.

**¿Qué significa?**
- `max-file-size`: Tamaño máximo de UNA imagen (25MB)
- `max-request-size`: Tamaño máximo de TODO lo que envías junto (25MB)
- `MAX_FILE_SIZE`: Validación en Java (debe ser igual al de application-docker.yml)

**¿Por qué 25MB?**
- Fotos de celular modernas: 3-8MB
- Fotos profesionales/cámaras: 10-20MB
- Margen de seguridad: 25MB

**Formatos soportados:**
- PNG (.png, .PNG)
- JPEG (.jpg, .jpeg, .JPG, .JPEG)
- WebP (.webp, .WEBP)

### ⚠️ Si necesitas cambiar el límite:

1. Edita el archivo: `backend/src/main/resources/application-docker.yml`
2. Cambia los valores (ej: a 50MB)
3. Reconstruye el backend:
   ```bash
   cd backend
   mvn clean package -DskipTests
   ```
4. Reinicia Docker:
   ```bash
   docker-compose stop backend
   docker-compose rm -f backend
   docker-compose up --build -d backend
   ```

---

## 🐳 Docker - ¿Qué hace cada contenedor?

### PostgreSQL (Base de datos)
- **Nombre**: `pasteleria-postgres`
- **Puerto**: `5436` (en tu máquina) → `5432` (dentro del contenedor)
- **Función**: Guarda todos los datos: productos, pedidos, usuarios, etc.
- **Persistencia**: Los datos se guardan en un volumen Docker (no se pierden al reiniciar)

### Spring Boot (Backend API)
- **Nombre**: `pasteleria-backend`
- **Puerto**: `8081` (en tu máquina) → `8080` (dentro del contenedor)
- **Función**: Procesa las peticiones del frontend, guarda imágenes, valida datos
- **Almacenamiento**: Las imágenes se guardan en `./storage/` (carpeta compartida con tu máquina)

### ¿Por qué puertos diferentes?
```
Tu máquina          Docker
8081       ←→      8080 (backend)
5436       ←→      5432 (postgres)
```
Esto permite tener múltiples proyectos sin conflictos.

---

## 📁 Estructura de Archivos Importante

```
Pastelería/
├── backend/
│   ├── storage/                    # 📦 Archivos guardados (imágenes, etc.)
│   │   └── assets/
│   │       └── products/          # 🖼️ Imágenes de productos
│   │           ├── guagua-de-pan.png
│   │           ├── torta-chocolate.png
│   │           └── ...
│   └── src/main/resources/
│       └── application-docker.yml  # ⚙️ Configuración
├── frontend-admin-angular/         # 🖥️ Panel de administración
└── frontend-landing-astro/         # 🌐 Página pública
```

---

## 🚀 Comandos Docker Útiles

### Ver si todo está funcionando:
```bash
docker-compose ps
```

### Ver logs (errores):
```bash
# Backend
docker-compose logs backend --tail=50

# Base de datos
docker-compose logs postgres --tail=20
```

### Reiniciar todo:
```bash
docker-compose restart
```

### Reconstruir (si cambias código Java):
```bash
cd backend
mvn clean package -DskipTests
cd ..
docker-compose stop backend
docker-compose rm -f backend
docker-compose up --build -d backend
```

### Ver imágenes guardadas:
```bash
# Listar archivos en el contenedor
docker-compose exec backend ls -la /app/storage/assets/products/

# O en tu máquina:
ls backend/storage/assets/products/
```

---

## ❌ Errores Comunes

### "El campo imagen exceeds its maximum permitted size"
**Significado**: La imagen es muy grande (>25MB)
**Solución**: Comprime la imagen en tinypng.com o similar

### "Formato no soportado. Use: PNG, JPG, JPEG o WebP"
**Significado**: La extensión del archivo no es válida o está en mayúsculas
**Solución**: Asegúrate de que el archivo termine en: .png, .jpg, .jpeg o .webp (en minúsculas o mayúsculas)

### "No se pudo autenticar"
**Significado**: El password se perdió (pasa al reiniciar con ddl-auto: create)
**Solución**: Ejecutar el comando de reset de password (ver README principal)

### "No se pudo registrar el producto"
**Posibles causas**:
- Código duplicado
- Categoría no existe
- Error de validación
**Solución**: Revisar logs del backend

---

## 🔐 Credenciales de Acceso

**Admin Panel**: http://localhost:4200
- Usuario: `admin`
- Password: `admin12345`

**Backend API**: http://localhost:8081
- Documentación: http://localhost:8081/swagger-ui.html

---

## 📞 ¿Necesitas ayuda?

Si algo no funciona:
1. Revisa los logs: `docker-compose logs backend --tail=50`
2. Verifica que los contenedores estén corriendo: `docker-compose ps`
3. Comprueba que los puertos no estén ocupados
4. Si todo falla, reinicia: `docker-compose restart`

---

**Nota**: Este archivo es de referencia. Para instrucciones de instalación completas, ver `README_EJECUCION_DOCKER.md`
