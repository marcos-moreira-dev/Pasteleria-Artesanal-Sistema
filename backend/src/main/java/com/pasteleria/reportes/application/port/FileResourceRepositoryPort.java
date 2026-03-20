package com.pasteleria.reportes.application.port;

import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;

public interface FileResourceRepositoryPort {

  FileResourceEntity save(FileResourceEntity fileResource);

  void delete(FileResourceEntity fileResource);
}
