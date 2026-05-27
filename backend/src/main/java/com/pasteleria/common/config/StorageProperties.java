package com.pasteleria.common.config;

import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(String root, String reportPath) {

  public String root() {
    return root == null || root.isBlank() ? "./storage" : root;
  }

  public String reportPath() {
    if (reportPath == null || reportPath.isBlank()) {
      return rootPath().resolve("reportes").toString();
    }
    return reportPath;
  }

  public Path rootPath() {
    return Path.of(root()).toAbsolutePath().normalize();
  }

  public Path assetsPath() {
    return rootPath().resolve("assets").normalize();
  }

  public Path publicPath(String publicPath) {
    String normalized = publicPath == null ? "" : publicPath.replaceFirst("^/", "");
    return rootPath().resolve(normalized).normalize();
  }
}
