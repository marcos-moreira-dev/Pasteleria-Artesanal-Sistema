package com.pasteleria.archivos.api;

import com.pasteleria.archivos.application.ArchivoDownload;
import com.pasteleria.archivos.application.DescargarArchivoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/archivos")
public class ArchivoController {

  private final DescargarArchivoService descargarArchivoService;

  public ArchivoController(DescargarArchivoService descargarArchivoService) {
    this.descargarArchivoService = descargarArchivoService;
  }

  @GetMapping("/{archivoId}/descargar")
  public ResponseEntity<FileSystemResource> descargar(@PathVariable Long archivoId) {
    ArchivoDownload download = descargarArchivoService.prepareDownload(archivoId);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(download.mimeType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
            .filename(download.filename())
            .build()
            .toString())
        .contentLength(download.sizeBytes())
        .body(new FileSystemResource(download.path()));
  }
}
