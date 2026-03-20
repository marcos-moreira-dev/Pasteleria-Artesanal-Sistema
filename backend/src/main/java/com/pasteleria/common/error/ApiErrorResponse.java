package com.pasteleria.common.error;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
    boolean success,
    String message,
    String errorCode,
    String requestId,
    OffsetDateTime timestamp
) {
}


