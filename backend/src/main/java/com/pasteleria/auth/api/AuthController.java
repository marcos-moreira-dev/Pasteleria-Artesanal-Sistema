package com.pasteleria.auth.api;

import com.pasteleria.auth.application.AuthResponse;
import com.pasteleria.auth.application.AuthService;
import com.pasteleria.auth.application.LoginRequest;
import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone el acceso administrativo al backend mediante autenticación basada en
 * JWT.
 */
@Validated
@Tag(name = "Autenticación", description = "Endpoints de acceso al panel administrativo.")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Autenticar usuario administrativo.")
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> login(
      @Valid @RequestBody LoginRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Autenticación completada correctamente.",
        authService.login(body),
        request
    ));
  }
}
