package com.pasteleria.caja.application;

import com.pasteleria.caja.infrastructure.persistence.entity.CajaOperativaEntity;
import com.pasteleria.caja.infrastructure.persistence.entity.MovimientoCajaEntity;
import com.pasteleria.caja.infrastructure.persistence.entity.TurnoCajaEntity;

import org.springframework.stereotype.Component;

@Component
public class CajaMapper {

  public CajaEstadoSummary toEstado(CajaOperativaEntity caja, TurnoCajaEntity turnoAbierto) {
    return new CajaEstadoSummary(
        caja.getId(),
        caja.getCodigo(),
        caja.getNombre(),
        caja.getSucursalCodigo(),
        caja.getMoneda(),
        caja.getSaldoActual(),
        caja.isActiva(),
        turnoAbierto != null,
        turnoAbierto == null ? null : toTurno(turnoAbierto)
    );
  }

  public TurnoCajaSummary toTurno(TurnoCajaEntity turno) {
    return new TurnoCajaSummary(
        turno.getId(),
        turno.getCaja().getId(),
        turno.getCaja().getCodigo(),
        turno.getCaja().getNombre(),
        turno.getEstado(),
        turno.getMontoApertura(),
        turno.getMontoCierreSistema(),
        turno.getMontoCierreDeclarado(),
        turno.getDiferenciaCierre(),
        turno.getFechaApertura(),
        turno.getFechaCierre(),
        displayName(turno.getUsuarioApertura()),
        displayName(turno.getUsuarioCierre()),
        turno.getObservacionesApertura(),
        turno.getObservacionesCierre()
    );
  }

  public MovimientoCajaSummary toMovimiento(MovimientoCajaEntity movimiento) {
    return new MovimientoCajaSummary(
        movimiento.getId(),
        movimiento.getTurno().getId(),
        movimiento.getTurno().getCaja().getCodigo(),
        movimiento.getTipoMovimiento(),
        movimiento.getNaturaleza(),
        movimiento.getMonto(),
        movimiento.getMoneda(),
        movimiento.getReferenciaTipo(),
        movimiento.getReferenciaId(),
        movimiento.getDescripcion(),
        movimiento.getEstado(),
        movimiento.getFechaMovimiento(),
        displayName(movimiento.getCreadoPor())
    );
  }

  private String displayName(com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity user) {
    if (user == null) {
      return null;
    }
    String first = user.getFirstName() == null ? "" : user.getFirstName().trim();
    String last = user.getLastName() == null ? "" : user.getLastName().trim();
    String full = (first + " " + last).trim();
    return full.isBlank() ? user.getUsername() : full;
  }
}
