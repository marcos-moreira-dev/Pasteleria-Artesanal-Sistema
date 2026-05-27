package com.pasteleria.common.storage;

import com.pasteleria.common.config.StorageProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Implementacion local y segura de storage.
 *
 * <p>Solo trabaja con rutas relativas normalizadas bajo {@code app.storage.root}.
 * No expone rutas fisicas al frontend y valida que ninguna resolucion pueda
 * escapar del directorio raiz mediante path traversal.</p>
 */
@Service
public class LocalStorageService implements StorageService {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

  private final StorageProperties storageProperties;

  public LocalStorageService(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  @Override
  public StoredFile store(StoreFileCommand command) {
    if (command == null) {
      throw new FileStorageException("La solicitud de almacenamiento no puede ser nula.");
    }
    byte[] content = command.content();
    if (content == null) {
      throw new FileStorageException("El contenido del archivo no puede ser nulo.");
    }

    String originalName = sanitizeFileName(command.originalName());
    String extension = normalizeExtension(command.extension(), originalName);
    String physicalName = buildPhysicalName(originalName, extension);
    String relativeDirectory = sanitizeRelativeDirectory(command.relativeDirectory());

    Path root = storageProperties.rootPath();
    Path targetDirectory = ensureInsideRoot(root.resolve(relativeDirectory).normalize());
    Path target = ensureInsideRoot(targetDirectory.resolve(physicalName).normalize());

    try {
      Files.createDirectories(targetDirectory);
      Files.write(target, content);
    } catch (IOException exception) {
      throw new FileStorageException("No se pudo guardar el archivo en storage local.", exception);
    }

    String relativePath = root.relativize(target).toString().replace('\\', '/');
    return new StoredFile(
        originalName,
        physicalName,
        relativePath,
        normalizeMimeType(command.mimeType()),
        extension,
        content.length,
        checksum(content)
    );
  }

  @Override
  public Path resolveExisting(String relativePath) {
    String normalized = sanitizeRelativePath(relativePath);
    Path target = ensureInsideRoot(storageProperties.rootPath().resolve(normalized).normalize());
    if (!Files.exists(target) || Files.isDirectory(target)) {
      throw new FileStorageException("El archivo solicitado no existe en el storage local.");
    }
    return target;
  }

  public Path rootPath() {
    return storageProperties.rootPath();
  }

  private Path ensureInsideRoot(Path candidate) {
    Path root = storageProperties.rootPath();
    if (!candidate.startsWith(root)) {
      throw new FileStorageException("Ruta de archivo fuera del storage permitido.");
    }
    return candidate;
  }

  private String sanitizeRelativeDirectory(String value) {
    String directory = value == null || value.isBlank() ? "generated" : value.trim().replace('\\', '/');
    if (directory.startsWith("/")) {
      directory = directory.substring(1);
    }
    if (directory.contains("..") || directory.contains(":")) {
      throw new FileStorageException("Directorio de almacenamiento no permitido.");
    }
    return directory.replaceAll("/{2,}", "/");
  }

  private String sanitizeRelativePath(String value) {
    if (value == null || value.isBlank()) {
      throw new FileStorageException("La ruta relativa del archivo es obligatoria.");
    }
    String normalized = value.trim().replace('\\', '/');
    if (normalized.startsWith("/")) {
      normalized = normalized.substring(1);
    }
    if (normalized.contains("..") || normalized.contains(":")) {
      throw new FileStorageException("Ruta relativa de archivo no permitida.");
    }
    return normalized.replaceAll("/{2,}", "/");
  }

  private String sanitizeFileName(String originalName) {
    String fallback = "archivo";
    String safe = originalName == null || originalName.isBlank() ? fallback : originalName.trim();
    safe = safe.replaceAll("[\\\\/:%*?\"<>|]", "-")
        .replaceAll("[^a-zA-Z0-9._ -]", "-")
        .replaceAll("-{2,}", "-")
        .trim();
    if (safe.isBlank() || ".".equals(safe) || "..".equals(safe)) {
      return fallback;
    }
    return safe.length() > 140 ? safe.substring(0, 140) : safe;
  }

  private String normalizeExtension(String explicitExtension, String originalName) {
    String extension = explicitExtension;
    if (extension == null || extension.isBlank()) {
      int dot = originalName.lastIndexOf('.');
      extension = dot >= 0 ? originalName.substring(dot) : "";
    }
    extension = extension == null ? "" : extension.trim().toLowerCase(Locale.ROOT);
    if (!extension.isBlank() && !extension.startsWith(".")) {
      extension = "." + extension;
    }
    if (extension.length() > 20) {
      throw new FileStorageException("La extension del archivo es demasiado larga.");
    }
    return extension;
  }

  private String buildPhysicalName(String originalName, String extension) {
    String base = originalName;
    if (!extension.isBlank() && base.toLowerCase(Locale.ROOT).endsWith(extension)) {
      base = base.substring(0, base.length() - extension.length());
    }
    base = base.replaceAll("[^a-zA-Z0-9._-]", "-").replaceAll("-{2,}", "-");
    if (base.isBlank()) {
      base = "archivo";
    }
    String timestamp = TIMESTAMP_FORMATTER.format(OffsetDateTime.now());
    String suffix = UUID.randomUUID().toString().substring(0, 8);
    return timestamp + "-" + suffix + "-" + base + extension;
  }

  private String normalizeMimeType(String mimeType) {
    return mimeType == null || mimeType.isBlank() ? "application/octet-stream" : mimeType.trim();
  }

  private String checksum(byte[] bytes) {
    try {
      return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    } catch (Exception exception) {
      throw new FileStorageException("No se pudo calcular el checksum del archivo.", exception);
    }
  }
}
