package com.pasteleria.common.assets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pasteleria.common.config.StorageProperties;
import com.pasteleria.common.error.ResourceNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PublicAssetServiceTest {

  @TempDir
  Path tempDir;

  @Test
  void shouldResolveWhitelistedAsset() throws Exception {
    Files.createDirectories(tempDir.resolve("assets/branding"));
    Files.write(tempDir.resolve("assets/branding/logo.png"), new byte[] {1, 2, 3});
    PublicAssetService service = new PublicAssetService(new StorageProperties(tempDir.toString(), null));

    PublicAssetResource asset = service.resolve("branding", "logo.png");

    assertThat(asset.contentType()).isEqualTo("image/png");
    assertThat(asset.path()).exists();
  }

  @Test
  void shouldRejectUnknownAssetType() {
    PublicAssetService service = new PublicAssetService(new StorageProperties(tempDir.toString(), null));

    assertThatThrownBy(() -> service.resolve("private", "logo.png"))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldRejectTraversalFilename() {
    PublicAssetService service = new PublicAssetService(new StorageProperties(tempDir.toString(), null));

    assertThatThrownBy(() -> service.resolve("branding", "../logo.png"))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
