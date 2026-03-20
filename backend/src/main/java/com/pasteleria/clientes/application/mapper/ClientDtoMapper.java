package com.pasteleria.clientes.application.mapper;

import java.time.OffsetDateTime;

import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.clientes.application.ClientSummary;
import com.pasteleria.clientes.application.CreateClientRequest;
import com.pasteleria.clientes.application.UpdateClientRequest;
import com.pasteleria.common.text.TextSupport;

import org.springframework.stereotype.Component;

/**
 * Centraliza la traducción entre la entidad de cliente y los contratos de la capa
 * de aplicación para no dispersar setters y DTOs dentro de los servicios.
 */
@Component
public class ClientDtoMapper {

  /**
   * Aplica el alta de cliente preservando la política común de timestamps.
   */
  public void applyCreateRequest(ClientEntity client, CreateClientRequest request, OffsetDateTime now) {
    client.setFullName(request.fullName().trim());
    client.setPhone(TextSupport.trimToNull(request.phone()));
    client.setEmail(TextSupport.trimToNull(request.email()));
    client.setNotes(TextSupport.trimToNull(request.notes()));
    client.setRegisteredAt(now);
    client.setCreatedAt(now);
    client.setUpdatedAt(now);
  }

  /**
   * Aplica cambios editables del cliente sin alterar la fecha de registro original.
   */
  public void applyUpdateRequest(ClientEntity client, UpdateClientRequest request, OffsetDateTime now) {
    client.setFullName(request.fullName().trim());
    client.setPhone(TextSupport.trimToNull(request.phone()));
    client.setEmail(TextSupport.trimToNull(request.email()));
    client.setNotes(TextSupport.trimToNull(request.notes()));
    client.setUpdatedAt(now);
  }

  /**
   * Expone una vista compacta del cliente para tablas, selectores y trazabilidad.
   */
  public ClientSummary toSummary(ClientEntity client) {
    return new ClientSummary(
        client.getId(),
        client.getFullName(),
        client.getPhone(),
        client.getEmail(),
        client.getNotes(),
        client.getRegisteredAt()
    );
  }
}


