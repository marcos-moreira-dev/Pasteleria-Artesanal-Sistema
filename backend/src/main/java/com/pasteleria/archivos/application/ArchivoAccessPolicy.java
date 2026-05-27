package com.pasteleria.archivos.application;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.reportes.domain.model.FileResourceStatus;
import com.pasteleria.reportes.infrastructure.persistence.entity.FileResourceEntity;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

/**
 * Politica minima para descarga de archivos internos.
 *
 * <p>La autorizacion fina por permiso/usuario se endurecera en T09. En esta
 * tanda se bloquean estados no descargables y expiraciones para no servir
 * basura o archivos eliminados.</p>
 */
@Component
public class ArchivoAccessPolicy {

  public void ensureDownloadable(FileResourceEntity file) {
    if (file.getStatus() != FileResourceStatus.DISPONIBLE) {
      throw new BusinessRuleException("El archivo no esta disponible para descarga.");
    }
    if (file.getExpirationAt() != null && file.getExpirationAt().isBefore(OffsetDateTime.now())) {
      throw new BusinessRuleException("El archivo solicitado ya expiro.");
    }
  }
}
