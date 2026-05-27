package com.pasteleria.common.storage;

/**
 * Error tecnico controlado de almacenamiento local.
 *
 * <p>Se mantiene separado del dominio de reportes para que archivos, assets,
 * comprobantes, guias y futuros documentos fiscales no dependan de una clase
 * especifica de reportes.</p>
 */
public class FileStorageException extends RuntimeException {

  public FileStorageException(String message) {
    super(message);
  }

  public FileStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
