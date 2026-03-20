package com.pasteleria.reportes.application;

import com.pasteleria.common.config.ReportProperties;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Orquesta la ejecución periódica del worker de reportes sin mezclar
 * responsabilidades de scheduling y lógica transaccional.
 */
@Component
public class ReportWorkerScheduler {

  private final ReportWorkerService reportWorkerService;
  private final ReportProperties reportProperties;

  public ReportWorkerScheduler(ReportWorkerService reportWorkerService, ReportProperties reportProperties) {
    this.reportWorkerService = reportWorkerService;
    this.reportProperties = reportProperties;
  }

  /**
   * Reintenta jobs pendientes como red de seguridad si la ejecución inmediata falla.
   */
  @Scheduled(fixedDelayString = "${app.report.worker-delay-ms:15000}")
  public void processPendingJobs() {
    if (!reportProperties.workerEnabled()) {
      return;
    }

    for (Long jobId : reportWorkerService.pendingJobIds()) {
      reportWorkerService.processOne(jobId);
    }
  }

  /**
   * Expira archivos generados fuera de la ventana de retención configurada.
   */
  @Scheduled(cron = "${app.report.cleanup-cron:0 15 3 * * *}")
  public void expireOldGeneratedFiles() {
    reportWorkerService.expireOldGeneratedFiles();
  }
}


