package com.pasteleria.common.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ApiResponseContractTest {

  @Test
  void shouldExposeNewAndLegacySuccessShapeDuringTransition() {
    ApiResponse<String> response = ApiResponse.ok("Operación completada.", "ok", "req-123");

    assertThat(response.ok()).isTrue();
    assertThat(response.data()).isEqualTo("ok");
    assertThat(response.error()).isNull();
    assertThat(response.meta().requestId()).isEqualTo("req-123");

    assertThat(response.success()).isTrue();
    assertThat(response.message()).isEqualTo("Operación completada.");
    assertThat(response.errorCode()).isNull();
    assertThat(response.requestId()).isEqualTo("req-123");
  }

  @Test
  void shouldExposeNewAndLegacyErrorShapeDuringTransition() {
    ApiResponse<Void> response = ApiResponse.error(
        "La solicitud no cumple las validaciones requeridas.",
        "VALIDACION_INVALIDA",
        "req-456",
        List.of(new ApiErrorDetail("nombre", "no debe estar vacío"))
    );

    assertThat(response.ok()).isFalse();
    assertThat(response.data()).isNull();
    assertThat(response.error()).isNotNull();
    assertThat(response.error().code()).isEqualTo("VALIDACION_INVALIDA");
    assertThat(response.error().details()).hasSize(1);
    assertThat(response.meta().requestId()).isEqualTo("req-456");

    assertThat(response.success()).isFalse();
    assertThat(response.message()).isEqualTo("La solicitud no cumple las validaciones requeridas.");
    assertThat(response.errorCode()).isEqualTo("VALIDACION_INVALIDA");
    assertThat(response.requestId()).isEqualTo("req-456");
  }
}
