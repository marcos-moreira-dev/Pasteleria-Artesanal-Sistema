-- =============================================================================
-- Pasteleria — Bootstrap: crea la base de datos y el esquema inicial
-- Tools: 00_database_bootstrap.sql
-- Ejecutar ANTES de los seeds
-- Requiere conexion como superusuario de PostgreSQL (postgres)
-- =============================================================================

-- Crear la base de datos si no existe
SELECT 'CREATE DATABASE pasteleria'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pasteleria')\gexec

-- Conectar a pasteleria antes de ejecutar el DDL
-- En psql: \c pasteleria
-- Luego ejecutar: db/V1/docs/Diagramas y query de creacion/V1_3FN.sql

-- Secuencia de ejecucion:
--   1. 00_database_bootstrap.sql  (este archivo)
--   2. db/V1/docs/Diagramas y query de creacion/V1_3FN.sql
--   3. db/V1/seeds/01_seed_base.sql
--   4. db/V1/seeds/02_seed_demo.sql
--   5. db/V1/seeds/03_seed_enterprise.sql
