package com.pasteleria.reportes.application;

import com.pasteleria.common.config.StorageProperties;
import com.pasteleria.common.storage.StoreFileCommand;
import com.pasteleria.common.storage.StorageService;
import com.pasteleria.common.storage.StoredFile;
import com.pasteleria.reportes.application.port.FileResourceRepositoryPort;
import com.pasteleria.reportes.domain.model.FileResourceStatus;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador de reportes sobre la capa generica de storage.
 *
 * <p>Se conserva para no romper el modulo de reportes, pero ahora delega el
 * almacenamiento fisico a {@link StorageService}. Los archivos nuevos se guardan
 * bajo {@code reportes/}; la resolucion conserva compatibilidad con archivos
 * historicos que solo almacenaban el nombre fisico dentro de report-path.</p>
 */
@Service
public class LocalReportStorageService {

  private static final String REPORTS_DIRECTORY = "reportes";

  private final StorageProperties storageProperties;
  private final FileResourceRepositoryPort fileResourceRepository;
  private final StorageService storageService;

  public LocalReportStorageService(
      StorageProperties storageProperties,
      FileResourceRepositoryPort fileResourceRepository,
      StorageService storageService
  ) {
    this.storageProperties = storageProperties;
    this.fileResourceRepository = fileResourceRepository;
    this.storageService = storageService;
  }

  @Transactional
  public FileResourceEntity storeGeneratedReport(
      String originalName,
      byte[] content,
      String mimeType,
      String extension,
      String sourceModule,
      UserEntity actor
  ) throws IOException {
    StoredFile storedFile = storageService.store(new StoreFileCommand(
        REPORTS_DIRECTORY,
        originalName,
        content,
        mimeType,
        extension
    ));

    FileResourceEntity file = new FileResourceEntity();
    file.setFileCode("FILE-" + System.currentTimeMillis());
    file.setSourceModule(sourceModule);
    file.setFileType("REPORTE_PDF");
    file.setOriginalName(storedFile.originalName());
    file.setPhysicalName(storedFile.physicalName());
    file.setMimeType(storedFile.mimeType());
    file.setExtension(storedFile.extension());
    file.setSizeBytes(storedFile.sizeBytes());
    file.setChecksum(storedFile.checksum());
    file.setRelativePath(storedFile.relativePath());
    file.setStatus(FileResourceStatus.DISPONIBLE);
    file.setCreatedAt(OffsetDateTime.now());
    file.setExpirationAt(OffsetDateTime.now().plusDays(14));
    file.setCreatedByUser(actor);
    return fileResourceRepository.save(file);
  }

  public Path resolvePath(FileResourceEntity fileResource) {
    try {
      return storageService.resolveExisting(fileResource.getRelativePath());
    } catch (RuntimeException exception) {
      Path legacyPath = Path.of(storageProperties.reportPath())
          .toAbsolutePath()
          .normalize()
          .resolve(fileResource.getRelativePath())
          .normalize();
      if (Files.exists(legacyPath) && !Files.isDirectory(legacyPath)) {
        return legacyPath;
      }
      throw exception;
    }
  }

  /**
   * Borra el archivo fisico sin depender del repositorio git ni dejar basura
   * en la ruta local de trabajo del backend.
   */
  public void deleteStoredReport(FileResourceEntity fileResource) throws IOException {
    Files.deleteIfExists(resolvePath(fileResource));
  }
}
