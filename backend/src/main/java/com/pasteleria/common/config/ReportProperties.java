package com.pasteleria.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.report")
public record ReportProperties(
    boolean workerEnabled,
    long workerDelayMs,
    int workerBatchSize,
    int retentionDays
) {
  public ReportProperties {
    workerDelayMs = workerDelayMs <= 0 ? 2500L : workerDelayMs;
    workerBatchSize = workerBatchSize <= 0 ? 3 : workerBatchSize;
    retentionDays = retentionDays <= 0 ? 14 : retentionDays;
  }
}


