package com.pasteleria.reportes.application;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.notificaciones.application.NotificationCreateCommand;
import com.pasteleria.notificaciones.application.NotificationPublishService;
import com.pasteleria.reportes.application.port.FileResourceRepositoryPort;
import com.pasteleria.reportes.application.port.ReportJobRepositoryPort;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import com.pasteleria.reportes.infrastructure.persistence.entity.ReportJobEntity;
import com.pasteleria.reportes.domain.model.ReportJobStatus;
import com.pasteleria.reportes.application.mapper.ReportJobDtoMapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReportCommandService {

  private static final Set<ReportJobStatus> TERMINAL_STATUSES = Set.of(
      ReportJobStatus.COMPLETADO,
      ReportJobStatus.ERROR,
      ReportJobStatus.CANCELADO,
      ReportJobStatus.EXPIRADO
  );

  private final ReportJobRepositoryPort reportJobRepository;
  private final FileResourceRepositoryPort fileResourceRepository;
  private final AuthenticatedUserService authenticatedUserService;
  private final ReportJobDtoMapper reportJobDtoMapper;
  private final NotificationPublishService notificationPublishService;
  private final ReportWorkerService reportWorkerService;
  private final TaskExecutor reportWorkerTaskExecutor;

  public ReportCommandService(
      ReportJobRepositoryPort reportJobRepository,
      FileResourceRepositoryPort fileResourceRepository,
      AuthenticatedUserService authenticatedUserService,
      ReportJobDtoMapper reportJobDtoMapper,
      NotificationPublishService notificationPublishService,
      ReportWorkerService reportWorkerService,
      @Qualifier("reportWorkerTaskExecutor") TaskExecutor reportWorkerTaskExecutor
  ) {
    this.reportJobRepository = reportJobRepository;
    this.fileResourceRepository = fileResourceRepository;
    this.authenticatedUserService = authenticatedUserService;
    this.reportJobDtoMapper = reportJobDtoMapper;
    this.notificationPublishService = notificationPublishService;
    this.reportWorkerService = reportWorkerService;
    this.reportWorkerTaskExecutor = reportWorkerTaskExecutor;
  }

  @Transactional
  public ReportJobSummary requestReport(CreateReportJobRequest request, HttpServletRequest httpRequest) {
    var user = authenticatedUserService.currentUser()
        .orElseThrow(() -> new BusinessRuleException("No hay usuario autenticado para solicitar reportes."));

    ReportJobEntity job = new ReportJobEntity();
    job.setJobCode("REP-" + System.currentTimeMillis());
    job.setReportType(request.reportType());
    job.setParametersJson("{}");
    job.setStatus(ReportJobStatus.PENDIENTE);
    job.setRequestedByUser(user);
    job.setRequestedAt(OffsetDateTime.now());
    job.setAttempts((short) 0);
    job.setRequestId(httpRequest != null ? httpRequest.getHeader("X-Request-Id") : null);
    ReportJobEntity saved = reportJobRepository.save(job);
    notificationPublishService.notifyUser(
        user,
        new NotificationCreateCommand(
            "REPORTE_SOLICITADO",
            "Reporte en cola",
            "El job " + saved.getJobCode() + " fue enviado a la cola de procesamiento.",
            "REPORTES",
            "job_reporte",
            saved.getId().toString(),
            NotificationPriority.MEDIA,
            "{}"
        )
    );

    // Dispara un intento inmediato despues del commit y deja al scheduler como
    // red de seguridad si el worker no alcanza a completar el job en ese
    // primer intento.
    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
      @Override
      public void afterCommit() {
        reportWorkerTaskExecutor.execute(() -> reportWorkerService.processOne(saved.getId()));
      }
    });

    return reportJobDtoMapper.toSummary(saved);
  }

  @Transactional
  public ReportActionResult deleteSelectedJobs(ReportJobSelectionRequest request) {
    Long userId = currentUserId();
    List<ReportJobEntity> ownedJobs = reportJobRepository.findByIdInAndRequestedByUserIdOrderByRequestedAtDesc(
        Set.copyOf(request.jobIds()),
        userId
    );
    return new ReportActionResult(deleteJobs(ownedJobs.stream()
        .filter(job -> TERMINAL_STATUSES.contains(job.getStatus()))
        .toList()));
  }

  @Transactional
  public ReportActionResult cleanupTerminalHistory(int keepLatest) {
    Long userId = currentUserId();
    List<ReportJobEntity> ownedTerminalJobs = reportJobRepository.findByRequestedByUserIdAndStatusInOrderByRequestedAtDesc(
        userId,
        TERMINAL_STATUSES
    );

    if (ownedTerminalJobs.size() <= keepLatest) {
      return new ReportActionResult(0);
    }

    return new ReportActionResult(deleteJobs(ownedTerminalJobs.subList(keepLatest, ownedTerminalJobs.size())));
  }

  private int deleteJobs(List<ReportJobEntity> jobs) {
    if (jobs.isEmpty()) {
      return 0;
    }

    List<FileResourceEntity> filesToDelete = new ArrayList<>();
    for (ReportJobEntity job : jobs) {
      if (job.getFileResource() != null) {
        filesToDelete.add(job.getFileResource());
        job.setFileResource(null);
        reportJobRepository.saveAndFlush(job);
      }
      reportJobRepository.delete(job);
    }

    filesToDelete.forEach(fileResourceRepository::delete);

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
      @Override
      public void afterCommit() {
        filesToDelete.forEach(file -> {
          try {
            reportWorkerService.deleteStoredFileIfExists(file);
          } catch (Exception ignored) {
            // El sistema privilegia consistencia transaccional. Si la limpieza
            // fisica falla, el archivo queda huerfano pero no rompe el dominio.
          }
        });
      }
    });

    return jobs.size();
  }

  private Long currentUserId() {
    return authenticatedUserService.currentUser()
        .orElseThrow(() -> new BusinessRuleException("No hay usuario autenticado para gestionar reportes."))
        .getId();
  }
}


