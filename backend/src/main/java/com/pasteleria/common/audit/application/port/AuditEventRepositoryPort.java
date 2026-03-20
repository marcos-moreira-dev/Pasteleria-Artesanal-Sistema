package com.pasteleria.common.audit.application.port;

import com.pasteleria.common.audit.infrastructure.persistence.entity.AuditEventEntity;

public interface AuditEventRepositoryPort {

  AuditEventEntity save(AuditEventEntity event);
}
