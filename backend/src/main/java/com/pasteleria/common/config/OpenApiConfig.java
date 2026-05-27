package com.pasteleria.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI pasteleriaOpenApi() {
    return new OpenAPI().info(
        new Info()
            .title("Pasteleria API")
            .version("v1")
            .description("Contrato base del backend central de Pasteleria.")
    );
  }
}


