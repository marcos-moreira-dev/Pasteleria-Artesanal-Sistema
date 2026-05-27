package com.pasteleria.produccion.application;

import java.math.BigDecimal;

public record ProductionFinalizationResult(
    int lotesGenerados,
    int consumosGenerados,
    BigDecimal costoTotalEstimado
) {
}
