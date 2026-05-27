package com.pasteleria.contabilidad.application;

import java.util.List;

import com.pasteleria.contabilidad.infrastructure.persistence.entity.AsientoContableDetalleEntity;
import com.pasteleria.contabilidad.infrastructure.persistence.entity.AsientoContableEntity;
import com.pasteleria.contabilidad.infrastructure.persistence.entity.CuentaContableEntity;
import com.pasteleria.contabilidad.infrastructure.persistence.entity.TipoDiarioContableEntity;

import org.springframework.stereotype.Component;

@Component
public class ContabilidadMapper {

  public CuentaContableSummary toCuenta(CuentaContableEntity entity) {
    return new CuentaContableSummary(
        entity.getId(),
        entity.getCodigo(),
        entity.getNombre(),
        entity.getTipoCuenta(),
        entity.getNaturaleza(),
        entity.getNivel(),
        entity.getCuentaPadre() == null ? null : entity.getCuentaPadre().getId(),
        entity.getImputable(),
        entity.getActiva()
    );
  }

  public TipoDiarioSummary toDiario(TipoDiarioContableEntity entity) {
    return new TipoDiarioSummary(
        entity.getId(),
        entity.getCodigo(),
        entity.getNombre(),
        entity.getDescripcion(),
        entity.getActivo()
    );
  }

  public AsientoContableSummary toAsiento(AsientoContableEntity entity, List<AsientoContableDetalleEntity> lineas) {
    return new AsientoContableSummary(
        entity.getId(),
        entity.getCodigo(),
        entity.getFechaAsiento(),
        entity.getTipoDiario().getCodigo(),
        entity.getDescripcion(),
        entity.getOrigenTipo(),
        entity.getOrigenId(),
        entity.getEstado(),
        entity.getTotalDebe(),
        entity.getTotalHaber(),
        lineas.stream().map(this::toDetalle).toList()
    );
  }

  private AsientoContableDetalleSummary toDetalle(AsientoContableDetalleEntity entity) {
    return new AsientoContableDetalleSummary(
        entity.getId(),
        entity.getCuenta().getCodigo(),
        entity.getCuenta().getNombre(),
        entity.getDescripcion(),
        entity.getDebe(),
        entity.getHaber()
    );
  }
}
