package com.pasteleria.contratos.application;

/**
 * Contrato visible de un endpoint backend.
 */
public record EndpointContract(
    String method,
    String path,
    String module,
    String summary,
    String requiredPermission,
    String scope,
    boolean publicEndpoint,
    boolean paginated
) {
}
