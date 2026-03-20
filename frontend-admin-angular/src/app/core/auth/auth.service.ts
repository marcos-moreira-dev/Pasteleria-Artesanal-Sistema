import { HttpClient } from "@angular/common/http";
import { Injectable, computed, inject, signal } from "@angular/core";
import { map, tap } from "rxjs";
import { apiConfig } from "../config/api.config";
import type { ApiResponse, AuthResponse } from "../contracts/api-contracts";

interface SessionState {
  token: string | null;
  username: string | null;
  role: string | null;
}

const SESSION_KEY = "pasteleria.admin.session";

@Injectable({ providedIn: "root" })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly session = signal<SessionState>(this.restoreSession());

  readonly token = computed(() => this.session().token);
  readonly username = computed(() => this.session().username);
  readonly role = computed(() => this.session().role);
  readonly isAuthenticated = computed(() => !!this.session().token);

  login(username: string, password: string) {
    return this.http.post<ApiResponse<AuthResponse>>(`${apiConfig.baseUrl}/auth/login`, { username, password }).pipe(
      map((response) => response.data),
      tap((data) => {
        const nextState: SessionState = {
          token: data.accessToken,
          username: data.username,
          role: data.role
        };
        this.session.set(nextState);
        localStorage.setItem(SESSION_KEY, JSON.stringify(nextState));
      })
    );
  }

  logout() {
    this.session.set({ token: null, username: null, role: null });
    localStorage.removeItem(SESSION_KEY);
  }

  private restoreSession(): SessionState {
    const raw = localStorage.getItem(SESSION_KEY);
    if (!raw) {
      return { token: null, username: null, role: null };
    }

    try {
      return JSON.parse(raw) as SessionState;
    } catch {
      localStorage.removeItem(SESSION_KEY);
      return { token: null, username: null, role: null };
    }
  }
}
