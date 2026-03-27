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
      if (error.status === 401 && !req.url.includes("/auth/login")) {
        authService.logout();
        void router.navigateByUrl("/login");
      }

      return throwError(() => error);
    }),
  );
};
