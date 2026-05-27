package com.pasteleria.soporte.application;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Fotografia de evidencia tecnica y operativa para soporte.
 */
public record SupportEvidenceSnapshot(
    OffsetDateTime generatedAt,
    String storageRoot,
    boolean storageRootExists,
    List<EvidenceCounter> operationalCounters,
    List<String> migrationMarkers,
    List<String> validationScripts,
    List<String> criticalEndpoints
) {
}
