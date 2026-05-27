package com.pasteleria.reportes.application.port;

import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import java.util.Optional;

public interface FileResourceRepositoryPort {

  FileResourceEntity save(FileResourceEntity fileResource);

  Optional<FileResourceEntity> findById(Long id);

  void delete(FileResourceEntity fileResource);
}
