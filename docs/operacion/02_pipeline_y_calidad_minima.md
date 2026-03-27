# 02 - Pipeline y calidad minima

## 1. Proposito

Este documento define la automatizacion minima y disciplinada del proyecto.

---

## 2. Objetivo del pipeline

El pipeline minimo debe asegurar:

- build backend
- pruebas backend
- build Astro
- build Angular
- validacion de artefactos principales

---

## 3. Etapas recomendadas

### Backend

- compilar
- probar
- empaquetar

### Frontend publico

- instalar dependencias
- verificar build

### Frontend administrativo

- instalar dependencias
- verificar build
- ejecutar pruebas base si existen

---

## 4. Criterio de fallo

El pipeline debe fallar si:

- backend no compila
- pruebas backend fallan
- Astro no construye
- Angular no construye

No conviene tolerar rojo en fases nucleares.

---

## 5. Temas computacionales que debes dominar aqui

- pipeline minimo viable
- gates de calidad
- build reproducible
- criterios de fallo
- artefactos verificables antes de release
