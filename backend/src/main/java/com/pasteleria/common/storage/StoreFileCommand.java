package com.pasteleria.common.storage;

/**
 * Solicitud de almacenamiento bajo una carpeta relativa segura del storage root.
 */
public record StoreFileCommand(
    String relativeDirectory,
    String originalName,
    byte[] content,
    String mimeType,
    String extension
) {
}
