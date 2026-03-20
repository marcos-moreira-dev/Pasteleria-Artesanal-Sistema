package com.pasteleria.reportes.application.mapper;

import com.pasteleria.reportes.infrastructure.persistence.entity.ReportJobEntity;
import com.pasteleria.reportes.application.ReportJobSummary;

import org.springframework.stereotype.Component;

@Component
public class ReportJobDtoMapper {

  public ReportJobSummary toSummary(ReportJobEntity job) {
    return new ReportJobSummary(
        job.getId(),
        job.getJobCode(),
        job.getReportType(),
        job.getStatus(),
        job.getAttempts(),
        job.getErrorMessage(),
        job.getRequestId(),
        job.getRequestedByUser() != null ? job.getRequestedByUser().getUsername() : null,
        job.getRequestedAt(),
        job.getStartedAt(),
        job.getFinishedAt(),
        job.getFileResource() != null ? job.getFileResource().getId() : null,
        job.getFileResource() != null ? job.getFileResource().getOriginalName() : null
    );
  }
}


