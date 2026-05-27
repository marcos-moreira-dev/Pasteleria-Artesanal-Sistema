package com.pasteleria.common.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pasteleria.common.config.StorageProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LocalStorageServiceTest {

  @TempDir
  Path tempDir;

  @Test
  void shouldStoreFileInsideConfiguredRoot() throws Exception {
    LocalStorageService service = new LocalStorageService(new StorageProperties(tempDir.toString(), null));

    StoredFile stored = service.store(new StoreFileCommand(
        "reportes",
        "Resumen negocio.pdf",
        "contenido".getBytes(),
        "application/pdf",
        ".pdf"
    ));

    assertThat(stored.relativePath()).startsWith("reportes/");
    assertThat(stored.physicalName()).endsWith(".pdf");
    assertThat(stored.checksum()).hasSize(64);
    assertThat(Files.exists(service.resolveExisting(stored.relativePath()))).isTrue();
  }

  @Test
  void shouldRejectPathTraversalDirectory() {
    LocalStorageService service = new LocalStorageService(new StorageProperties(tempDir.toString(), null));

    assertThatThrownBy(() -> service.store(new StoreFileCommand(
        "../secreto",
        "archivo.pdf",
        new byte[] {1},
        "application/pdf",
        ".pdf"
    ))).isInstanceOf(FileStorageException.class);
  }

  @Test
  void shouldRejectPathTraversalWhenResolving() {
    LocalStorageService service = new LocalStorageService(new StorageProperties(tempDir.toString(), null));

    assertThatThrownBy(() -> service.resolveExisting("../archivo.pdf"))
        .isInstanceOf(FileStorageException.class);
  }
}
