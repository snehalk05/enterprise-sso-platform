import { Component, inject } from '@angular/core';
import {
    FormBuilder,
    ReactiveFormsModule,
    Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';

import { AuthService } from '../../core/services/auth.service';

@Component({
    standalone: true,
    imports: [ReactiveFormsModule, RouterLink],
    template: `
        <section class="auth">
            <div class="card box">
                <h1>Create your account</h1>

                <form
                        class="form"
                        [formGroup]="form"
                        (ngSubmit)="submit()"
                >
                    <div class="field">
                        <label for="username">Username</label>
                        <input
                                id="username"
                                autocomplete="name"
                                formControlName="username"
                        />
                    </div>

                    <div class="field">
                        <label for="email">Email</label>
                        <input
                                id="email"
                                type="email"
                                autocomplete="email"
                                formControlName="email"
                        />
                    </div>

                    <div class="field">
                        <label for="password">Password</label>
                        <input
                                id="password"
                                type="password"
                                autocomplete="new-password"
                                formControlName="password"
                        />

                        <small class="muted">
                            At least 8 characters.
                        </small>
                    </div>

                    @if (message) {
                        <div class="success">
                            {{ message }}
                        </div>
                    }

                    @if (error) {
                        <div class="error">
                            {{ error }}
                        </div>
                    }

                    <button
                            type="submit"
                            class="btn btn-primary"
                            [disabled]="form.invalid || loading"
                    >
                        {{ loading ? 'Creating account...' : 'Register' }}
                    </button>
                </form>

                <a routerLink="/login">Back to sign in</a>
            </div>
        </section>
    `,
    styles: [
        `
            .auth {
                min-height: calc(100vh - 68px);
                display: grid;
                place-items: center;
                padding: 24px;
            }

            .box {
                width: min(480px, 100%);
                display: grid;
                gap: 20px;
            }
        `
    ]
})
export class RegisterComponent {
    private readonly fb = inject(FormBuilder);
    private readonly auth = inject(AuthService);
    private readonly router = inject(Router);

    loading = false;
    message = '';
    error = '';

    form = this.fb.nonNullable.group({
        username: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(8)]]
    });

    submit(): void {
        if (this.form.invalid || this.loading) {
            this.form.markAllAsTouched();
            return;
        }

        this.loading = true;
        this.message = '';
        this.error = '';

        this.auth
            .register(this.form.getRawValue())
            .pipe(finalize(() => (this.loading = false)))
            .subscribe({
                next: response => {
                    this.message = response.message;

                    setTimeout(() => {
                        this.router.navigate(['/login'], {
                            queryParams: {
                                registered: 'true',
                                email: this.form.controls.email.value
                            }
                        });
                    }, 1200);
                },
                error: error => {
                    this.error =
                        error.error?.message ??
                        error.error?.error ??
                        'Registration failed';
                }
            });
    }
}