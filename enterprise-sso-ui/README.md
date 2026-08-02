# Enterprise SSO Angular UI

Angular 21 LTS-style standalone frontend for the existing Spring Boot microservices branch `origin/notification-service`.

## Features
- Registration, login, refresh-token plumbing, logout, forgot-password request
- JWT parsing, route guards, HTTP interceptor, correlation IDs
- RBAC application catalog and ADMIN role-assignment page
- User profile through `/api/users/me`
- MFA setup screen for TOTP/email
- Docker multi-stage build: Node.js builds Angular; Nginx serves static files and proxies `/api` to `api-gateway:8080`
- Responsive, dependency-light UI with lazy-loaded pages

## Run locally
```bash
npm ci
npm start
```
Angular dev server runs at `http://localhost:4200` and proxies `/api` to `http://localhost:8080`.

## Add to your existing root Docker Compose
1. Copy this folder into the repository root as `enterprise-sso-ui/`.
2. Add this service to the existing `docker-compose.yml`:
```yaml
  sso-ui:
    build: ./enterprise-sso-ui
    depends_on:
      - api-gateway
    ports:
      - "4200:80"
    restart: unless-stopped
```
3. Build backend JARs first, then run:
```bash
mvn clean package -DskipTests
docker compose up --build
```
Open `http://localhost:4200`.

## Backend contracts used
- `POST /api/auth/register` `{username,email,password}`
- `POST /api/auth/login` `{email,password}`
- `POST /api/auth/refresh` `{refreshToken}`
- `POST /api/auth/logout` `{refreshToken}`
- `POST /api/auth/mfa/{email}` `{type}`
- `POST /api/auth/password-reset/request` `{email}`
- `POST /api/auth/admin/roles` `{email,roles}`
- `GET /api/users/me`

## Important backend gaps
### 1. MFA is not complete
The current login service generates access and refresh tokens before a second factor is verified. `mfaRequired` is only a flag. Add a two-stage API:
- `POST /api/auth/login` returns a short-lived `mfaChallengeToken` and no normal tokens when MFA is enabled.
- `POST /api/auth/mfa/verify` validates TOTP/email OTP and only then returns access/refresh tokens.

### 2. Integrated application SSO is only a catalog
The sample application links are placeholders. Real SSO requires one of:
- OIDC Authorization Code + PKCE (recommended), with this platform acting as an Authorization Server/IdP.
- SAML 2.0 for legacy enterprise service providers.
- A short-lived, one-time launch code exchanged server-to-server; never put the platform JWT in a query string.

### 3. App registry is currently frontend configuration
Move applications, launch URLs, required roles/scopes and status into an `application-registry-service` or auth-service endpoint such as `GET /api/apps/me`.

### 4. Production token storage
This educational SPA uses `sessionStorage` because the current backend returns tokens in JSON. A production browser architecture should preferably add a Backend-for-Frontend (BFF) and use Secure, HttpOnly, SameSite cookies, refresh-token rotation, CSRF defenses and strict CSP.

### 5. Gateway CORS
Docker uses same-origin Nginx proxying, so browser CORS is avoided. For separate UI/API domains, configure a strict allow-list in API Gateway rather than `*`.
