package com.pasteleria.reportes.application;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

/**
 * Representa un lote de jobs elegidos desde el panel para limpieza manual.
 */
public record ReportJobSelectionRequest(
    @NotEmpty(message = "Debe seleccionar al menos un job de reporte.")
    List<@Positive(message = "Cada identificador de job debe ser positivo.") Long> jobIds
) {
}
