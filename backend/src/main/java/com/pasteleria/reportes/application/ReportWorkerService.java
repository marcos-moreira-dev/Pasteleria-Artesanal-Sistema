package com.pasteleria.reportes.application;

import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.config.ReportProperties;
import com.pasteleria.cotizaciones.application.port.QuotationRepositoryPort;
import com.pasteleria.notificaciones.application.NotificationCreateCommand;
import com.pasteleria.notificaciones.application.NotificationPublishService;
import com.pasteleria.notificaciones.domain.model.NotificationPriority;
import com.pasteleria.pedidos.application.port.OrderRepositoryPort;
import com.pasteleria.pedidos.domain.model.OrderStatus;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.produccion.application.port.ProductionRepositoryPort;
import com.pasteleria.produccion.domain.model.ProductionStatus;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.reportes.application.port.ReportJobRepositoryPort;
import com.pasteleria.reportes.domain.model.FileResourceStatus;
import com.pasteleria.reportes.domain.model.ReportJobStatus;
import com.pasteleria.reportes.domain.model.ReportType;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import com.pasteleria.reportes.infrastructure.persistence.entity.ReportJobEntity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Encapsula el procesamiento diferido de reportes para que el scheduler no
 * dependa de auto-invocaciones transaccionales y el worker pueda reclamar jobs
 * de forma consistente.
 */
@Service
public class ReportWorkerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportWorkerService.class);

  private final ReportJobRepositoryPort reportJobRepository;
  private final LocalReportStorageService localReportStorageService;
  private final ClientRepositoryPort clientRepository;
  private final ProductRepositoryPort productRepository;
  private final QuotationRepositoryPort quotationRepository;
  private final OrderRepositoryPort orderRepository;
  private final ProductionRepositoryPort productionRepository;
  private final NotificationPublishService notificationPublishService;
  private final AuditTrailService auditTrailService;
  private final ReportProperties reportProperties;
  private final ReportPdfDocumentService reportPdfDocumentService;

  public ReportWorkerService(
      ReportJobRepositoryPort reportJobRepository,
      LocalReportStorageService localReportStorageService,
      ClientRepositoryPort clientRepository,
      ProductRepositoryPort productRepository,
      QuotationRepositoryPort quotationRepository,
      OrderRepositoryPort orderRepository,
      ProductionRepositoryPort productionRepository,
      NotificationPublishService notificationPublishService,
      AuditTrailService auditTrailService,
      ReportProperties reportProperties,
      ReportPdfDocumentService reportPdfDocumentService
  ) {
    this.reportJobRepository = reportJobRepository;
    this.localReportStorageService = localReportStorageService;
    this.clientRepository = clientRepository;
    this.productRepository = productRepository;
    this.quotationRepository = quotationRepository;
    this.orderRepository = orderRepository;
    this.productionRepository = productionRepository;
    this.notificationPublishService = notificationPublishService;
    this.auditTrailService = auditTrailService;
    this.reportProperties = reportProperties;
    this.reportPdfDocumentService = reportPdfDocumentService;
  }

  @Transactional(readOnly = true)
  public List<Long> pendingJobIds() {
    return reportJobRepository.findIdsByStatusOrderByRequestedAtAsc(
            ReportJobStatus.PENDIENTE,
            PageRequest.of(0, reportProperties.workerBatchSize())
        )
        .getContent();
  }

  @Transactional
  public void processOne(Long jobId) {
    ReportJobEntity job = reportJobRepository.findByIdForUpdate(jobId).orElse(null);
    if (job == null || job.getStatus() != ReportJobStatus.PENDIENTE) {
      return;
    }

    job.setStatus(ReportJobStatus.EN_PROCESO);
    job.setStartedAt(OffsetDateTime.now());
    job.setAttempts((short) (job.getAttempts() + 1));
    job.setErrorMessage(null);
    reportJobRepository.saveAndFlush(job);

    try {
      GeneratedReport generatedReport = buildReport(job.getReportType(), job.getJobCode());

      FileResourceEntity file = localReportStorageService.storeGeneratedReport(
          generatedReport.originalName(),
          generatedReport.content(),
          generatedReport.mimeType(),
          generatedReport.extension(),
          "REPORTES",
          job.getRequestedByUser()
      );

      job.setFileResource(file);
      job.setStatus(ReportJobStatus.COMPLETADO);
      job.setFinishedAt(OffsetDateTime.now());
      reportJobRepository.save(job);

      publishSuccessSideEffects(job, file);
    } catch (Exception exception) {
      job.setStatus(ReportJobStatus.ERROR);
      job.setFinishedAt(OffsetDateTime.now());
      job.setErrorMessage(exception.getMessage());
      reportJobRepository.save(job);
      publishFailureSideEffects(job, exception);
    }
  }

  @Transactional
  public int expireOldGeneratedFiles() {
    int updatedJobs = 0;
    OffsetDateTime now = OffsetDateTime.now();
    for (ReportJobEntity job : reportJobRepository.findAll()) {
      FileResourceEntity file = job.getFileResource();
      if (file != null
          && file.getStatus() == FileResourceStatus.DISPONIBLE
          && file.getExpirationAt() != null
          && file.getExpirationAt().isBefore(now)) {
        file.setStatus(FileResourceStatus.EXPIRADO);
        job.setStatus(ReportJobStatus.EXPIRADO);
        updatedJobs++;
      }
    }
    return updatedJobs;
  }

  /**
   * Permite que comandos administrativos limpien el archivo fisico despues del
   * commit sin exponer detalles de storage a los controladores.
   */
  public void deleteStoredFileIfExists(FileResourceEntity file) throws java.io.IOException {
    localReportStorageService.deleteStoredReport(file);
  }

  private void publishSuccessSideEffects(ReportJobEntity job, FileResourceEntity file) {
    try {
      notificationPublishService.notifyUser(
          job.getRequestedByUser(),
          new NotificationCreateCommand(
              "REPORTE_LISTO",
              "Reporte listo para descarga",
              "El job " + job.getJobCode() + " ya genero el archivo " + file.getOriginalName() + ".",
              "REPORTES",
              "job_reporte",
              job.getId().toString(),
              NotificationPriority.MEDIA,
              "{\"fileId\":" + file.getId() + "}"
          )
      );
      auditTrailService.record(
          "REPORTE_COMPLETADO",
          "REPORTES",
          "job_reporte",
          job.getId().toString(),
          "COMPLETAR_REPORTE",
          "Worker de reportes finalizado correctamente.",
          null
      );
    } catch (Exception sideEffectException) {
      LOGGER.warn("No se pudieron publicar efectos secundarios del job {}: {}", job.getJobCode(), sideEffectException.getMessage());
    }
  }

  private void publishFailureSideEffects(ReportJobEntity job, Exception exception) {
    if (job.getRequestedByUser() == null) {
      return;
    }

    try {
      notificationPublishService.notifyUser(
          job.getRequestedByUser(),
          new NotificationCreateCommand(
              "REPORTE_ERROR",
              "No se pudo generar el reporte",
              "El job " + job.getJobCode() + " termino con error: " + exception.getMessage(),
              "REPORTES",
              "job_reporte",
              job.getId().toString(),
              NotificationPriority.ALTA,
              "{}"
          )
      );
    } catch (Exception sideEffectException) {
      LOGGER.warn("No se pudo notificar el error del job {}: {}", job.getJobCode(), sideEffectException.getMessage());
    }
  }

  private GeneratedReport buildReport(ReportType reportType, String jobCode) {
    return switch (reportType) {
      case RESUMEN_NEGOCIO -> buildBusinessSummary(jobCode);
      case COLA_PRODUCCION -> buildProductionQueueSummary(jobCode);
    };
  }

  private GeneratedReport buildBusinessSummary(String jobCode) {
    List<OrderEntity> orders = orderRepository.findAllByOrderByCreatedAtDesc();
    List<?> quotations = quotationRepository.findAllByOrderByCreatedAtDesc();
    List<ProductionEntity> productionItems = productionRepository.findAll();
    OffsetDateTime generatedAt = OffsetDateTime.now();

    BigDecimal deliveredTotal = orders.stream()
        .filter(order -> order.getStatus() == OrderStatus.ENTREGADO)
        .map(order -> order.getEstimatedTotal() == null ? BigDecimal.ZERO : order.getEstimatedTotal())
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    byte[] pdf = reportPdfDocumentService.buildBusinessSummaryPdf(
        new ReportPdfDocumentService.BusinessSummaryData(
            generatedAt,
            clientRepository.count(),
            productRepository.findByPublishedTrueAndActiveTrueOrderByNameAsc().size(),
            quotations.size(),
            orders.size(),
            productionItems.stream().filter(item -> item.getStatus() != ProductionStatus.FINALIZADO).count(),
            deliveredTotal,
            orders.stream()
                .limit(8)
                .map(order -> new ReportPdfDocumentService.RecentOrderItem(
                    order.getCode(),
                    order.getClient().getFullName(),
                    order.getStatus().name(),
                    order.getEstimatedTotal() == null ? BigDecimal.ZERO : order.getEstimatedTotal()
                ))
                .toList()
        )
    );

    return new GeneratedReport("resumen-negocio-" + jobCode + ".pdf", pdf, "application/pdf", "pdf");
  }

  private GeneratedReport buildProductionQueueSummary(String jobCode) {
    byte[] pdf = reportPdfDocumentService.buildProductionQueuePdf(
        new ReportPdfDocumentService.ProductionQueueData(
            OffsetDateTime.now(),
            productionRepository.findAll().stream()
                .filter(item -> item.getStatus() != ProductionStatus.FINALIZADO)
                .sorted(java.util.Comparator.comparing(ProductionEntity::getCreatedAt))
                .map(item -> new ReportPdfDocumentService.ProductionQueueItem(
                    item.getOrder().getCode(),
                    item.getOrder().getClient().getFullName(),
                    item.getStatus().name(),
                    item.getPriority().name(),
                    item.getProductionNotes() == null || item.getProductionNotes().isBlank()
                        ? "Sin observacion adicional."
                        : item.getProductionNotes().trim()
                ))
                .toList()
        )
    );

    return new GeneratedReport("cola-produccion-" + jobCode + ".pdf", pdf, "application/pdf", "pdf");
  }

  private record GeneratedReport(
      String originalName,
      byte[] content,
      String mimeType,
      String extension
  ) {
  }
}
