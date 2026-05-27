import { CommonModule } from "@angular/common";
import { HttpErrorResponse } from "@angular/common/http";
import { Component, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../core/auth/auth.service";
import { getBackendBrandingAsset } from "../../shared/utils/backend-asset.util";
import { businessShellConfig } from "../../core/config/business-shell.config";

@Component({
  selector: "app-login",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <main class="login-screen">
      <section class="login-panel">
        <div class="brand-band">
          <img [src]="logoSquareUrl" [alt]="shellConfig.brand.logoAlt" width="60" height="60" />
          <div>
            <p class="eyebrow">{{ shellConfig.login.eyebrow }}</p>
            <h1>{{ shellConfig.login.title }}</h1>
          </div>
        </div>

        <p class="copy">{{ shellConfig.login.copy }}</p>

        <form [formGroup]="form" (ngSubmit)="submit()" class="login-form">
          <label>
            Usuario
            <input type="text" formControlName="username" placeholder="admin" />
          </label>
          <label>
            Contraseña
            <input type="password" formControlName="password" placeholder="********" />
          </label>
          <button type="submit" [disabled]="form.invalid || loading()">
            {{ loading() ? "Ingresando..." : "Entrar al panel" }}
          </button>
        </form>

        <p class="hint">{{ shellConfig.login.localAccessLabel }}: <strong>{{ shellConfig.login.localUsers.join(", ") }}</strong>.</p>
        <p class="hint hint--secondary">{{ shellConfig.login.supportHint }}</p>
        <p class="error" *ngIf="error()">{{ error() }}</p>
      </section>
    </main>
  `,
  styles: [`
    .login-screen {
      min-height: 100vh;
      display: grid;
      place-items: center;
      padding: 2rem;
      background:
        radial-gradient(circle at top left, rgba(255, 228, 212, 0.92), transparent 30%),
        radial-gradient(circle at bottom right, rgba(123, 61, 39, 0.16), transparent 25%),
        linear-gradient(135deg, #2c1711 0%, #5d2b1b 50%, #f5ede8 50%, #f7f0ea 100%);
    }
    .login-panel {
      width: min(460px, 100%);
      padding: 2rem;
      border-radius: 4px;
      background: rgba(255, 249, 245, 0.94);
      box-shadow: 0 24px 80px rgba(38, 18, 13, 0.22);
      display: grid;
      gap: 1.3rem;
    }
    .brand-band {
      display: grid;
      grid-template-columns: 60px 1fr;
      gap: 0.9rem;
      align-items: center;
    }
    .brand-band img {
      width: 60px;
      height: 60px;
      object-fit: cover;
      border-radius: 4px;
      border: 1px solid #ead2c5;
      background: #fff;
    }
    .eyebrow { margin: 0; text-transform: uppercase; letter-spacing: 0.24em; font-size: 0.72rem; color: #8d563e; }
    h1 { margin: 0.35rem 0 0; font: 700 2.2rem/1 var(--font-display, "Cormorant Garamond", Georgia, serif); color: #331a12; }
    .copy, .hint { margin: 0; color: #6c5147; line-height: 1.6; }
    .hint--secondary { font-size: 0.95rem; color: #7b6056; }
    .login-form { display: grid; gap: 1rem; }
    label { display: grid; gap: 0.45rem; font-weight: 600; color: #4d342b; }
    input {
      border: 1px solid #d8c3b7;
      border-radius: 2px;
      padding: 0.9rem 1rem;
      background: #fff;
      font: inherit;
    }
    button {
      border: 0;
      border-radius: 2px;
      padding: 0.95rem 1rem;
      background: linear-gradient(135deg, #6a321f, #391c14);
      color: white;
      font-weight: 700;
      cursor: pointer;
    }
    button:disabled { opacity: 0.6; cursor: not-allowed; }
    .error { margin: 0; color: #ad2f2f; font-weight: 600; }
  `]
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly shellConfig = businessShellConfig;
  readonly logoSquareUrl = getBackendBrandingAsset(this.shellConfig.brand.logoSquareFileName);

  readonly form = this.fb.nonNullable.group({
    username: ["admin", Validators.required],
    password: ["admin12345", Validators.required]
  });

  submit() {
    if (this.form.invalid) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.authService.login(this.form.getRawValue().username, this.form.getRawValue().password).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigateByUrl("/");
      },
      error: (error: HttpErrorResponse) => {
        this.loading.set(false);
        if (error.status === 0) {
          this.error.set("No se pudo conectar con el backend local. Inicia el backend y vuelve a intentar.");
          return;
        }

        this.error.set("No se pudo autenticar la sesión. Verifica las credenciales del sistema.");
      }
    });
  }
}
