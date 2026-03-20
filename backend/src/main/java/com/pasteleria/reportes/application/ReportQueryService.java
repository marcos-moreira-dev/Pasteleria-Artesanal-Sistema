package com.pasteleria.reportes.application;

import java.util.List;

import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.reportes.application.port.ReportJobRepositoryPort;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import com.pasteleria.reportes.infrastructure.persistence.entity.ReportJobEntity;
import com.pasteleria.reportes.application.mapper.ReportJobDtoMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportQueryService {

  private final ReportJobRepositoryPort reportJobRepository;
  private final AuthenticatedUserService authenticatedUserService;
  private final ReportJobDtoMapper reportJobDtoMapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public ReportQueryService(
      ReportJobRepositoryPort reportJobRepository,
      AuthenticatedUserService authenticatedUserService,
      ReportJobDtoMapper reportJobDtoMapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.reportJobRepository = reportJobRepository;
    this.authenticatedUserService = authenticatedUserService;
    this.reportJobDtoMapper = reportJobDtoMapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<ReportJobSummary> latestJobs(int limit) {
    Long userId = authenticatedUserService.currentUser()
        .orElseThrow(() -> new BusinessRuleException("No hay usuario autenticado para consultar reportes."))
        .getId();
    return reportJobRepository.findByRequestedByUserIdOrderByRequestedAtDesc(userId, PageRequest.of(0, Math.max(1, Math.min(limit, 20))))
        .stream()
        .map(reportJobDtoMapper::toSummary)
        .toList();
  }

  public PageResponseDto<ReportJobSummary> pagedJobs(int page, int size) {
    Long userId = authenticatedUserService.currentUser()
        .orElseThrow(() -> new BusinessRuleException("No hay usuario autenticado para consultar reportes."))
        .getId();

    var pageable = pageRequestFactory.create(page, size, org.springframework.data.domain.Sort.by(
        org.springframework.data.domain.Sort.Order.desc("requestedAt")
    ));

    var jobPage = reportJobRepository.findByRequestedByUserIdOrderByRequestedAtDesc(userId, pageable)
        .map(reportJobDtoMapper::toSummary);

    return pageMapper.toPageResponseDto(jobPage);
  }

  public FileResourceEntity resolveOwnedFile(Long jobId) {
    Long userId = authenticatedUserService.currentUser()
        .orElseThrow(() -> new ResourceNotFoundException("No se pudo resolver el usuario autenticado."))
        .getId();

    ReportJobEntity job = reportJobRepository.findOwnedJobWithFile(jobId, userId)
        .orElseThrow(() -> new ResourceNotFoundException("El job de reporte indicado no existe para esta sesion."));

    if (job.getFileResource() == null) {
      throw new ResourceNotFoundException("El reporte todavia no tiene un archivo disponible para descarga.");
    }
    return job.getFileResource();
  }
}


