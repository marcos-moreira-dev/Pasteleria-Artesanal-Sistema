package com.pasteleria.reportes.infrastructure.persistence.repository;

import com.pasteleria.reportes.application.port.FileResourceRepositoryPort;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResourceRepository extends JpaRepository<FileResourceEntity, Long>, FileResourceRepositoryPort {
}



