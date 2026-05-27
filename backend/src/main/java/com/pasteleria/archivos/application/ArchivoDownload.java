package com.pasteleria.archivos.application;

import java.nio.file.Path;

public record ArchivoDownload(
    Path path,
    String filename,
    String mimeType,
    long sizeBytes
) {
}
