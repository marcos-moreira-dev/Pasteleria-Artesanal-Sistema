package com.pasteleria.contabilidad.application;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RegistrarAsientoContableRequest(
    @NotBlank String tipoDiarioCodigo,
    String codigo,
    LocalDateTime fechaAsiento,
    @NotBlank String descripcion,
    String origenTipo,
    String origenId,
    @NotEmpty List<@Valid AsientoContableDetalleRequest> lineas
) {
}
