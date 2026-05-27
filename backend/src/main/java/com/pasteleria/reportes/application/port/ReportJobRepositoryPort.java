package com.pasteleria.reportes.application.port;

import com.pasteleria.reportes.domain.model.ReportJobStatus;
import com.pasteleria.reportes.infrastructure.persistence.entity.ReportJobEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportJobRepositoryPort {

  Page<ReportJobEntity> findByRequestedByUserIdOrderByRequestedAtDesc(Long userId, Pageable pageable);

  Page<ReportJobEntity> findByStatusOrderByRequestedAtAsc(ReportJobStatus status, Pageable pageable);

  Page<Long> findIdsByStatusOrderByRequestedAtAsc(ReportJobStatus status, Pageable pageable);

  Optional<ReportJobEntity> findByIdForUpdate(Long jobId);

  Optional<ReportJobEntity> findOwnedJobWithFile(Long jobId, Long userId);

  List<ReportJobEntity> findByIdInAndRequestedByUserIdOrderByRequestedAtDesc(Set<Long> jobIds, Long userId);

  List<ReportJobEntity> findByRequestedByUserIdAndStatusInOrderByRequestedAtDesc(
      Long userId,
      Set<ReportJobStatus> statuses
  );

  Optional<ReportJobEntity> findById(Long jobId);

  ReportJobEntity save(ReportJobEntity job);

  ReportJobEntity saveAndFlush(ReportJobEntity job);

  void delete(ReportJobEntity job);

  List<ReportJobEntity> findAll();
}
