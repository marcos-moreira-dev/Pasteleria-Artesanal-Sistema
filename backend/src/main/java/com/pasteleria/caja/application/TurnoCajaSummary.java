package com.pasteleria.caja.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TurnoCajaSummary(
    Long id,
    Long cajaId,
    String cajaCodigo,
    String cajaNombre,
    String estado,
    BigDecimal montoApertura,
    BigDecimal montoCierreSistema,
    BigDecimal montoCierreDeclarado,
    BigDecimal diferenciaCierre,
    OffsetDateTime fechaApertura,
    OffsetDateTime fechaCierre,
    String usuarioApertura,
    String usuarioCierre,
    String observacionesApertura,
    String observacionesCierre
) {
}
