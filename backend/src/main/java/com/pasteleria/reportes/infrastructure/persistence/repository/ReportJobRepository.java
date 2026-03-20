package com.pasteleria.reportes.infrastructure.persistence.repository;

import com.pasteleria.reportes.application.port.ReportJobRepositoryPort;
import com.pasteleria.reportes.domain.model.ReportJobStatus;
import com.pasteleria.reportes.infrastructure.persistence.entity.ReportJobEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportJobRepository extends JpaRepository<ReportJobEntity, Long>, ReportJobRepositoryPort {

  Page<ReportJobEntity> findByRequestedByUserIdOrderByRequestedAtDesc(Long userId, Pageable pageable);

  Page<ReportJobEntity> findByStatusOrderByRequestedAtAsc(ReportJobStatus status, Pageable pageable);

  @Query("""
      select job.id
        from ReportJobEntity job
       where job.status = :status
       order by job.requestedAt asc
      """)
  Page<Long> findIdsByStatusOrderByRequestedAtAsc(@Param("status") ReportJobStatus status, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      select job
        from ReportJobEntity job
        left join fetch job.requestedByUser
       where job.id = :jobId
      """)
  Optional<ReportJobEntity> findByIdForUpdate(@Param("jobId") Long jobId);

  @Query("""
      select job
        from ReportJobEntity job
        join fetch job.requestedByUser
        left join fetch job.fileResource
       where job.id = :jobId
         and job.requestedByUser.id = :userId
      """)
  Optional<ReportJobEntity> findOwnedJobWithFile(
      @Param("jobId") Long jobId,
      @Param("userId") Long userId
  );

  @Query("""
      select distinct job
        from ReportJobEntity job
        join fetch job.requestedByUser
        left join fetch job.fileResource
       where job.requestedByUser.id = :userId
         and job.id in :jobIds
       order by job.requestedAt desc
      """)
  List<ReportJobEntity> findByIdInAndRequestedByUserIdOrderByRequestedAtDesc(
      @Param("jobIds") Set<Long> jobIds,
      @Param("userId") Long userId
  );

  @Query("""
      select distinct job
        from ReportJobEntity job
        join fetch job.requestedByUser
        left join fetch job.fileResource
       where job.requestedByUser.id = :userId
         and job.status in :statuses
       order by job.requestedAt desc
      """)
  List<ReportJobEntity> findByRequestedByUserIdAndStatusInOrderByRequestedAtDesc(
      @Param("userId") Long userId,
      @Param("statuses") Set<ReportJobStatus> statuses
  );
}



