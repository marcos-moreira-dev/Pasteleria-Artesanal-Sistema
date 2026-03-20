import type { ClientSummary } from "../../features/clientes/models/client.models";

/**
 * Centraliza el filtrado incremental de clientes para formularios y selectores
 * sin duplicar logica en cada pantalla.
 */
export function filterClientsByQuery(clients: ClientSummary[], query: string): ClientSummary[] {
  const normalizedQuery = query.trim().toLocaleLowerCase();
  if (!normalizedQuery) {
    return clients;
  }

  return clients.filter((client) => {
    const haystack = [
      client.fullName,
      client.email ?? "",
      client.phone ?? ""
    ].join(" ").toLocaleLowerCase();

    return haystack.includes(normalizedQuery);
  });
}
