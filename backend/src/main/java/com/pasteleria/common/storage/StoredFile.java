package com.pasteleria.common.storage;

/**
 * Metadata fisica calculada despues de almacenar un archivo.
 */
public record StoredFile(
    String originalName,
    String physicalName,
    String relativePath,
    String mimeType,
    String extension,
    long sizeBytes,
    String checksum
) {
}
