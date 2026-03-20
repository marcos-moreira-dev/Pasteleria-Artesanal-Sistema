package com.pasteleria.reportes.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;

import com.pasteleria.common.config.StorageProperties;
import com.pasteleria.reportes.application.port.FileResourceRepositoryPort;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import com.pasteleria.reportes.domain.model.FileResourceStatus;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalReportStorageService {

  private final StorageProperties storageProperties;
  private final FileResourceRepositoryPort fileResourceRepository;

  public LocalReportStorageService(StorageProperties storageProperties, FileResourceRepositoryPort fileResourceRepository) {
    this.storageProperties = storageProperties;
    this.fileResourceRepository = fileResourceRepository;
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
    Path root = Path.of(storageProperties.reportPath()).toAbsolutePath().normalize();
    Files.createDirectories(root);

    String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(OffsetDateTime.now());
    String physicalName = timestamp + "-" + sanitizeFileName(originalName);
    Path target = root.resolve(physicalName).normalize();
    Files.write(target, content);

    FileResourceEntity file = new FileResourceEntity();
    file.setFileCode("FILE-" + System.currentTimeMillis());
    file.setSourceModule(sourceModule);
    file.setFileType("REPORTE");
    file.setOriginalName(originalName);
    file.setPhysicalName(physicalName);
    file.setMimeType(mimeType);
    file.setExtension(extension);
    file.setSizeBytes(content.length);
    file.setChecksum(checksum(content));
    file.setRelativePath(physicalName);
    file.setStatus(FileResourceStatus.DISPONIBLE);
    file.setCreatedAt(OffsetDateTime.now());
    file.setExpirationAt(OffsetDateTime.now().plusDays(14));
    file.setCreatedByUser(actor);
    return fileResourceRepository.save(file);
  }

  public Path resolvePath(FileResourceEntity fileResource) {
    return Path.of(storageProperties.reportPath())
        .toAbsolutePath()
        .normalize()
        .resolve(fileResource.getRelativePath())
        .normalize();
  }

  /**
   * Borra el archivo fisico sin depender del repositorio git ni dejar basura
   * en la ruta local de trabajo del backend.
   */
  public void deleteStoredReport(FileResourceEntity fileResource) throws IOException {
    Files.deleteIfExists(resolvePath(fileResource));
  }

  private String sanitizeFileName(String originalName) {
    return originalName
        .replaceAll("[^a-zA-Z0-9._-]", "-")
        .replaceAll("-{2,}", "-");
  }

  private String checksum(byte[] bytes) {
    try {
      return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    } catch (Exception exception) {
      throw new IllegalStateException("No se pudo calcular el checksum del reporte.", exception);
    }
  }
}


