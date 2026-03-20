package com.pasteleria.clientes.application;

import java.time.OffsetDateTime;

public record ClientSummary(
    Long id,
    String fullName,
    String phone,
    String email,
    String notes,
    OffsetDateTime registeredAt
) {
}


