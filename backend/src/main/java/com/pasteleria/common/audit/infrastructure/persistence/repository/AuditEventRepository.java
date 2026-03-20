package com.pasteleria.common.audit.infrastructure.persistence.repository;

import com.pasteleria.common.audit.application.port.AuditEventRepositoryPort;
import com.pasteleria.common.audit.infrastructure.persistence.entity.AuditEventEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventRepository extends JpaRepository<AuditEventEntity, Long>, AuditEventRepositoryPort {
}



