import { HttpClient } from "@angular/common/http";
import { Injectable, computed, inject, signal } from "@angular/core";
import { map, tap } from "rxjs";
import { apiConfig } from "../config/api.config";
import type { ApiResponse, AuthenticatedUserContext, AuthResponse, SucursalOperable } from "../contracts/api-contracts";

interface SessionState {
  token: string | null;
  username: string | null;
  role: string | null;
  displayName: string | null;
  rolesGlobales: string[];
  roles: string[];
  permisosGlobales: string[];
  permisos: string[];
  sucursalesOperables: SucursalOperable[];
  selectedSucursalId: string | null;
}

const SESSION_KEY = "pasteleria.admin.session";

@Injectable({ providedIn: "root" })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly session = signal<SessionState>(this.restoreSession());

  readonly token = computed(() => this.session().token);
  readonly username = computed(() => this.session().username);
  readonly role = computed(() => this.session().role);
  readonly displayName = computed(() => this.session().displayName ?? this.session().username);
  readonly rolesGlobales = computed(() => this.session().rolesGlobales);
  readonly roles = computed(() => this.session().roles);
  readonly permisosGlobales = computed(() => this.session().permisosGlobales);
  readonly permisos = computed(() => this.session().permisos);
  readonly sucursalesOperables = computed(() => this.session().sucursalesOperables);
  readonly selectedSucursalId = computed(() => this.session().selectedSucursalId);
  readonly isAuthenticated = computed(() => !!this.session().token);

  login(username: string, password: string) {
    return this.http.post<ApiResponse<AuthResponse>>(`${apiConfig.baseUrl}/auth/login`, { username, password }).pipe(
      map((response) => response.data),
      tap((data) => this.applyAuthData(data))
    );
  }

  loadCurrentUser() {
    return this.http.get<ApiResponse<AuthenticatedUserContext>>(`${apiConfig.baseUrl}/auth/me`).pipe(
      map((response) => response.data),
      tap((data) => this.applyContext(data))
    );
  }

  selectSucursal(sucursalId: string | null) {
    const current = this.session();
    const next = { ...current, selectedSucursalId: sucursalId };
    this.session.set(next);
    this.persist(next);
  }

  hasPermission(permission: string): boolean {
    return this.session().permisos.includes(permission) || this.session().permisosGlobales.includes(permission);
  }

  logout() {
    this.session.set(this.emptySession());
    localStorage.removeItem(SESSION_KEY);
  }

  private applyAuthData(data: AuthResponse) {
    const nextState: SessionState = {
      token: data.accessToken,
      username: data.username,
      role: data.role,
      displayName: data.displayName ?? data.username,
      rolesGlobales: data.rolesGlobales ?? [],
      roles: data.roles ?? [data.role],
      permisosGlobales: data.permisosGlobales ?? [],
      permisos: data.permisos ?? [],
      sucursalesOperables: data.sucursalesOperables ?? [],
      selectedSucursalId: this.resolveSelectedSucursal(data.sucursalesOperables ?? [], null),
    };
    this.session.set(nextState);
    this.persist(nextState);
  }

  private applyContext(data: AuthenticatedUserContext) {
    const current = this.session();
    const nextState: SessionState = {
      ...current,
      username: data.username,
      role: data.role,
      displayName: data.displayName ?? data.username,
      rolesGlobales: data.rolesGlobales ?? [],
      roles: data.roles ?? [data.role],
      permisosGlobales: data.permisosGlobales ?? [],
      permisos: data.permisos ?? [],
      sucursalesOperables: data.sucursalesOperables ?? [],
      selectedSucursalId: this.resolveSelectedSucursal(data.sucursalesOperables ?? [], current.selectedSucursalId),
    };
    this.session.set(nextState);
    this.persist(nextState);
  }

  private restoreSession(): SessionState {
    const raw = localStorage.getItem(SESSION_KEY);
    if (!raw) {
      return this.emptySession();
    }

    try {
      const restored = JSON.parse(raw) as Partial<SessionState>;
      if (!restored.token || this.isExpiredToken(restored.token)) {
        localStorage.removeItem(SESSION_KEY);
        return this.emptySession();
      }

      return {
        token: restored.token,
        username: restored.username ?? null,
        role: restored.role ?? null,
        displayName: restored.displayName ?? restored.username ?? null,
        rolesGlobales: restored.rolesGlobales ?? [],
        roles: restored.roles ?? (restored.role ? [restored.role] : []),
        permisosGlobales: restored.permisosGlobales ?? [],
        permisos: restored.permisos ?? [],
        sucursalesOperables: restored.sucursalesOperables ?? [],
        selectedSucursalId: restored.selectedSucursalId ?? this.resolveSelectedSucursal(restored.sucursalesOperables ?? [], null),
      };
    } catch {
      localStorage.removeItem(SESSION_KEY);
      return this.emptySession();
    }
  }

  private resolveSelectedSucursal(sucursales: SucursalOperable[], preferred: string | null): string | null {
    if (preferred && sucursales.some((sucursal) => sucursal.id === preferred || sucursal.codigo === preferred)) {
      return preferred;
    }
    return sucursales.find((sucursal) => sucursal.principal)?.id ?? sucursales[0]?.id ?? null;
  }

  private emptySession(): SessionState {
    return {
      token: null,
      username: null,
      role: null,
      displayName: null,
      rolesGlobales: [],
      roles: [],
      permisosGlobales: [],
      permisos: [],
      sucursalesOperables: [],
      selectedSucursalId: null,
    };
  }

  private persist(state: SessionState) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(state));
  }

  private isExpiredToken(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split(".")[1] ?? "")) as { exp?: number };
      if (!payload.exp) {
        return false;
      }

      return payload.exp * 1000 <= Date.now();
    } catch {
      return true;
    }
  }
}
