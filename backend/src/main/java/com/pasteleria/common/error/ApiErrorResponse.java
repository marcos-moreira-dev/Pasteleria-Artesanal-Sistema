package com.pasteleria.common.error;

import java.time.OffsetDateTime;

/**
 * @deprecated Desde T03 los errores deben exponerse con
 * {@link com.pasteleria.common.api.ApiResponse}. Se conserva temporalmente por
 * compatibilidad historica y para facilitar refactors graduales.
 */
@Deprecated(forRemoval = false)
public record ApiErrorResponse(
    boolean success,
    String message,
    String errorCode,
    String requestId,
    OffsetDateTime timestamp
) {
}
