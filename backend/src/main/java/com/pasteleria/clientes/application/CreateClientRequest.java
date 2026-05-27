package com.pasteleria.clientes.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequest(
    @NotBlank @Size(max = 160) String fullName,
    @Size(max = 30) String phone,
    @Email @Size(max = 120) String email,
    @Size(max = 1000) String notes
) {
}


