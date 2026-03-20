UPDATE job_reporte
SET tipo_reporte = 'RESUMEN_NEGOCIO'
WHERE tipo_reporte NOT IN ('RESUMEN_NEGOCIO', 'COLA_PRODUCCION');
