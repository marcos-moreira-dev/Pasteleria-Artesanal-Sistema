package com.pasteleria.contabilidad.application;

public record CuentaContableSummary(
    Long id,
    String codigo,
    String nombre,
    String tipoCuenta,
    String naturaleza,
    Integer nivel,
    Long cuentaPadreId,
    Boolean imputable,
    Boolean activa
) {
}
