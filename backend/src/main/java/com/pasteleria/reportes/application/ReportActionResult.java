package com.pasteleria.reportes.application;

/**
 * Resume cuantas filas fueron afectadas por una operacion administrativa sobre
 * jobs de reporte.
 */
public record ReportActionResult(int affectedCount) {
}
