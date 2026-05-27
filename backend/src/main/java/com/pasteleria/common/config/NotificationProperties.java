package com.pasteleria.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.notification")
public record NotificationProperties(
    int defaultLimit,
    int archiveReadAfterDays
) {
  public NotificationProperties {
    defaultLimit = defaultLimit <= 0 ? 8 : defaultLimit;
    archiveReadAfterDays = archiveReadAfterDays <= 0 ? 10 : archiveReadAfterDays;
  }
}


