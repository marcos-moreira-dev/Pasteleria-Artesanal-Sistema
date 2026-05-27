package com.pasteleria.archivos.application;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.storage.FileStorageException;
import com.pasteleria.common.storage.StorageService;
import com.pasteleria.reportes.application.LocalReportStorageService;
import com.pasteleria.reportes.application.port.FileResourceRepositoryPort;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import java.nio.file.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DescargarArchivoService {

  private final FileResourceRepositoryPort fileResourceRepository;
  private final ArchivoAccessPolicy accessPolicy;
  private final StorageService storageService;
  private final LocalReportStorageService legacyReportStorageService;

  public DescargarArchivoService(
      FileResourceRepositoryPort fileResourceRepository,
      ArchivoAccessPolicy accessPolicy,
      StorageService storageService,
      LocalReportStorageService legacyReportStorageService
  ) {
    this.fileResourceRepository = fileResourceRepository;
    this.accessPolicy = accessPolicy;
    this.storageService = storageService;
    this.legacyReportStorageService = legacyReportStorageService;
  }

  @Transactional(readOnly = true)
  public ArchivoDownload prepareDownload(Long archivoId) {
    FileResourceEntity file = fileResourceRepository.findById(archivoId)
        .orElseThrow(() -> new ResourceNotFoundException("El archivo solicitado no existe."));
    accessPolicy.ensureDownloadable(file);
    Path path = resolvePath(file);
    return new ArchivoDownload(path, file.getOriginalName(), file.getMimeType(), file.getSizeBytes());
  }

  private Path resolvePath(FileResourceEntity file) {
    try {
      return storageService.resolveExisting(file.getRelativePath());
    } catch (FileStorageException exception) {
      // Compatibilidad con archivos historicos de reportes que guardaban solo
      // el nombre fisico dentro de app.storage.report-path.
      return legacyReportStorageService.resolvePath(file);
    }
  }
}
