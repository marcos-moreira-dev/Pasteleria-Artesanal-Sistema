package com.pasteleria.common.assets;

import com.pasteleria.common.config.StorageProperties;
import com.pasteleria.common.error.ResourceNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Resuelve assets publicos desde storage/assets con whitelist de carpetas y
 * extensiones. No permite rutas arbitrarias ni traversal.
 */
@Service
public class PublicAssetService {

  private static final Set<String> ALLOWED_TYPES = Set.of(
      "branding",
      "products",
      "categories",
      "branches",
      "placeholders"
  );

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "webp", "svg");

  private static final Map<String, String> CONTENT_TYPES = Map.of(
      "png", "image/png",
      "jpg", "image/jpeg",
      "jpeg", "image/jpeg",
      "webp", "image/webp",
      "svg", "image/svg+xml"
  );

  private final StorageProperties storageProperties;

  public PublicAssetService(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  public PublicAssetResource resolve(String type, String filename) {
    String safeType = sanitizeType(type);
    String safeFilename = sanitizeFilename(filename);
    String extension = extensionOf(safeFilename);

    Path assetsRoot = storageProperties.assetsPath();
    Path target = assetsRoot.resolve(safeType).resolve(safeFilename).normalize();
    if (!target.startsWith(assetsRoot) || !Files.exists(target) || Files.isDirectory(target)) {
      throw new ResourceNotFoundException("El asset solicitado no existe.");
    }

    return new PublicAssetResource(target, safeFilename, CONTENT_TYPES.getOrDefault(extension, "application/octet-stream"));
  }

  private String sanitizeType(String type) {
    if (type == null || type.isBlank()) {
      throw new ResourceNotFoundException("Tipo de asset no encontrado.");
    }
    String normalized = type.trim().toLowerCase(Locale.ROOT);
    if (!ALLOWED_TYPES.contains(normalized)) {
      throw new ResourceNotFoundException("Tipo de asset no permitido.");
    }
    return normalized;
  }

  private String sanitizeFilename(String filename) {
    if (filename == null || filename.isBlank()) {
      throw new ResourceNotFoundException("Nombre de asset no encontrado.");
    }
    String normalized = filename.trim();
    if (normalized.contains("..") || normalized.contains("/") || normalized.contains("\\") || normalized.contains(":") || normalized.contains("%")) {
      throw new ResourceNotFoundException("Nombre de asset no permitido.");
    }
    String extension = extensionOf(normalized);
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new ResourceNotFoundException("Extension de asset no permitida.");
    }
    return normalized;
  }

  private String extensionOf(String filename) {
    int dot = filename.lastIndexOf('.');
    if (dot < 0 || dot == filename.length() - 1) {
      throw new ResourceNotFoundException("El asset debe tener extension.");
    }
    return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
  }
}
