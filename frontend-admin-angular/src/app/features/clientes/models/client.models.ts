export interface ClientSummary {
  id: number;
  fullName: string;
  phone: string | null;
  email: string | null;
  notes: string | null;
  registeredAt: string;
}

export interface CreateClientPayload {
  fullName: string;
  phone: string | null;
  email: string | null;
  notes: string | null;
}

export interface UpdateClientPayload extends CreateClientPayload {
}
