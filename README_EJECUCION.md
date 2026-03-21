# 🚀 Guía Rápida de Ejecución - Pastelería

## ⚡ Inicio en 30 segundos

### Requisitos previos

✅ PostgreSQL 17 instalado y corriendo  
✅ Node.js 22.12.0+ instalado  
✅ Java 21 (Eclipse Temurin) instalado  
✅ Base de datos `pasteleria` creada con script `db/V1/DATABASE_MASTER_COMPLETE.sql`

---

## 🎯 Método 1: Script Automático (Recomendado)

1. **Verifica que PostgreSQL esté corriendo**

   - Abre pgAdmin o Servicios de Windows
   - Confirma que PostgreSQL está iniciado

2. **Ejecuta el script de inicio**

   ```
   Doble clic en: INICIAR_SISTEMA.bat
   ```

3. **Espera 20 segundos**

   - Se abrirán 2 ventanas de comandos
   - Backend: mostrará "Started PasteleriaApplication"
   - Frontend: mostrará "Compiled successfully"

4. **Abre tu navegador**
   - Frontend: http://localhost:4200
   - Usuario: admin
   - Password: admin123

---

## 🛠️ Método 2: Manual (Terminal)

### Terminal 1 - Backend:

```cmd
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\backend
.\scripts\start-backend-dev.cmd
```

Espera a que diga: `Started PasteleriaApplication in X.XXX seconds`

### Terminal 2 - Frontend:

```cmd
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-admin-angular
npm start
```

Espera a que diga: `Compiled successfully`

---

## 🔍 Verificación de que todo funciona

1. **Backend:** http://localhost:8080/api/v1/health

   - Debe mostrar: `{"status":"UP"}`

2. **Frontend:** http://localhost:4200

   - Debe mostrar pantalla de login

3. **Login:**

   - Usuario: `admin`
   - Password: `admin123`
   - Debe ingresar al dashboard

4. **Productos:**

   - Ve a "Productos" en el menú
   - Edita "Torta de Chocolate"
   - Verás la sección "Receta del Producto" con campos estructurados

5. **Abastecimiento:**
   - Ve a "Abastecimiento" en el menú
   - Debe mostrar dashboard con datos

---

## 🛑 Para detener el sistema

1. **Frontend:** Presiona `Ctrl+C` en la ventana del frontend
2. **Backend:** Presiona `Ctrl+C` en la ventana del backend
3. **O simplemente:** Cierra las ventanas de comandos

---

## 🔧 Si algo falla

### "Puerto 4200 en uso"

- El sistema preguntará si deseas usar otro puerto
- Selecciona "Yes"
- El frontend iniciará en puerto 4201, 4202, etc.

### "PostgreSQL connection refused"

```
1. Abre pgAdmin
2. Click derecho en el servidor → Connect
3. Verifica que la BD "pasteleria" exista
4. Si no existe, ejecuta: db/V1/DATABASE_MASTER_COMPLETE.sql
```

### "npm modules missing"

```cmd
cd frontend-admin-angular
npm install
cd ..
INICIAR_SISTEMA.bat
```

### "Error compilando tests"

- Esto es normal, los tests están configurados para saltarse automáticamente
- El backend funcionará igual

---

## 📞 Accesos Rápidos

| Servicio     | URL                                   | Descripción             |
| ------------ | ------------------------------------- | ----------------------- |
| Frontend     | http://localhost:4200                 | Interfaz administrativa |
| Backend API  | http://localhost:8080                 | API REST                |
| Swagger Docs | http://localhost:8080/swagger-ui.html | Documentación API       |
| Health Check | http://localhost:8080/api/v1/health   | Estado del sistema      |

---

## ✅ Checklist de Verificación

- [ ] PostgreSQL corriendo
- [ ] Base de datos `pasteleria` creada
- [ ] Script SQL ejecutado
- [ ] Backend iniciado sin errores rojos
- [ ] Frontend compilado exitosamente
- [ ] Login funciona con admin/admin123
- [ ] Módulo Productos carga recetas
- [ ] Módulo Abastecimiento muestra datos

---

## 🎉 ¡Listo!

Si completaste el checklist, el sistema está funcionando correctamente.

Para soporte técnico, revisa:

- `README.md` - Documentación completa
- `db/V1/README.md` - Guía de base de datos
- `docs/` - Documentación técnica extensa

**Disfruta tu sistema de pastelería!** 🧁
