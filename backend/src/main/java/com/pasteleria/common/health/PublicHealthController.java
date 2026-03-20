package com.pasteleria.common.health;

import com.pasteleria.common.api.ApiResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/health")
public class PublicHealthController {

  @GetMapping
  public ApiResponse<Map<String, String>> health(
      @RequestHeader(value = "X-Request-Id", required = false) String requestId
  ) {
    return ApiResponse.ok("Backend de Pasteleria disponible.", Map.of("status", "UP"), requestId);
  }
}


