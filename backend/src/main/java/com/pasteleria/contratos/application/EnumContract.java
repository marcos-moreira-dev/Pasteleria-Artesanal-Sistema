package com.pasteleria.contratos.application;

import java.util.List;

/**
 * Enum o catalogo tecnico expuesto como contrato para alinear frontend/backend.
 */
public record EnumContract(
    String name,
    List<String> values
) {
}
