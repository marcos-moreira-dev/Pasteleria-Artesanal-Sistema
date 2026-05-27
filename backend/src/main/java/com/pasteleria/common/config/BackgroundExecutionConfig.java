package com.pasteleria.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Define ejecutores livianos para tareas internas que no deben bloquear el
 * request principal del panel administrativo.
 */
@Configuration
public class BackgroundExecutionConfig {

  @Bean(name = "reportWorkerTaskExecutor")
  public TaskExecutor reportWorkerTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("pasteleria-report-worker-");
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(16);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.initialize();
    return executor;
  }
}
