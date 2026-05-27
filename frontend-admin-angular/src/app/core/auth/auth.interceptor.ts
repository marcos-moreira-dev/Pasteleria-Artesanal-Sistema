import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, throwError } from "rxjs";
import { AuthService } from "./auth.service";

function buildRequestId(): string {
  const cryptoApi = globalThis.crypto;
  if (cryptoApi && typeof cryptoApi.randomUUID === "function") {
    return cryptoApi.randomUUID();
  }

  return `req-${Date.now()}-${Math.random().toString(16).slice(2, 10)}`;
}

function isAuthEndpoint(url: string): boolean {
  return url.includes("/auth/login");
}

function isProtectedApiError(error: HttpErrorResponse, url: string): boolean {
  return !isAuthEndpoint(url) && (error.status === 401 || error.status === 403);
}

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.token();
  const request = req.clone({
    setHeaders: {
      "X-Request-Id": buildRequestId(),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  });

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      // En Spring Security stateless, un JWT ausente, vencido o firmado con una clave vieja
      // puede terminar como 401 o 403 según el punto de la cadena. Para el usuario ambos
      // significan lo mismo: la sesión local ya no sirve y debe volver a iniciar sesión.
      if (isProtectedApiError(error, req.url)) {
        authService.logout();
        void router.navigateByUrl("/login");
      }

      return throwError(() => error);
    }),
  );
};
