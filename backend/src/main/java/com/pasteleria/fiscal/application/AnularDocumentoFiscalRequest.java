package com.pasteleria.fiscal.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnularDocumentoFiscalRequest(
    @NotBlank(message = "El motivo de anulacion es obligatorio.")
    @Size(max = 500, message = "El motivo de anulacion no puede superar 500 caracteres.")
    String motivo
) {
}
