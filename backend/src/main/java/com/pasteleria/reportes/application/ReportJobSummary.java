package com.pasteleria.reportes.application;

import java.time.OffsetDateTime;

import com.pasteleria.reportes.domain.model.ReportJobStatus;
import com.pasteleria.reportes.domain.model.ReportType;

public record ReportJobSummary(
    Long id,
    String jobCode,
    ReportType reportType,
    ReportJobStatus status,
    short attempts,
    String errorMessage,
    String requestId,
    String requestedByUsername,
    OffsetDateTime requestedAt,
    OffsetDateTime startedAt,
    OffsetDateTime finishedAt,
    Long fileId,
    String fileName
) {
}


