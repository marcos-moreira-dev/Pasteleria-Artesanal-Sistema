package com.pasteleria.common.assets;

import java.time.Duration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sirve assets publicos desde storage sin exponer rutas fisicas.
 */
@RestController
@RequestMapping({"/api/v1/assets", "/assets"})
public class PublicAssetController {

  private final PublicAssetService publicAssetService;

  public PublicAssetController(PublicAssetService publicAssetService) {
    this.publicAssetService = publicAssetService;
  }

  @GetMapping("/{type}/{filename:.+}")
  public ResponseEntity<FileSystemResource> getAsset(
      @PathVariable String type,
      @PathVariable String filename
  ) {
    PublicAssetResource asset = publicAssetService.resolve(type, filename);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(asset.contentType()))
        .cacheControl(CacheControl.maxAge(Duration.ofHours(6)).cachePublic())
        .body(new FileSystemResource(asset.path()));
  }
}
