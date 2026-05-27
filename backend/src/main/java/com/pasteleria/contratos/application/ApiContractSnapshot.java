package com.pasteleria.contratos.application;

import java.util.List;

/**
 * Foto completa del contrato API administrativo de Pasteleria.
 */
public record ApiContractSnapshot(
    List<EndpointContract> endpoints,
    List<PermissionContract> permissions,
    List<EnumContract> enums,
    PaginationContract pagination
) {
}
