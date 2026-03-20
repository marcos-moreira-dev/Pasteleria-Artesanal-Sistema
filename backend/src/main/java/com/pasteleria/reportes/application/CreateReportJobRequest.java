package com.pasteleria.reportes.application;

import com.pasteleria.reportes.domain.model.ReportType;

import jakarta.validation.constraints.NotNull;

public record CreateReportJobRequest(
    @NotNull ReportType reportType
) {
}


