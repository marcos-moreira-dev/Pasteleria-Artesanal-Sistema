package com.pasteleria.abastecimiento.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProveedorRequest(
    @NotBlank @Size(max = 40) String codigo,
    @NotBlank @Size(max = 160) String nombre,
    @Size(max = 30) String telefono,
    @Email @Size(max = 120) String correo,
    @Size(max = 500) String direccion,
    @Size(max = 1000) String observaciones
) {
}