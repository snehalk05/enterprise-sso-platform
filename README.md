# Enterprise SSO Platform — Version 1.1

This learning version demonstrates both synchronous and asynchronous inter-service communication.

## What was added

- **Eureka Discovery Server** on `http://localhost:8761`
- Every application registers with Eureka: `AUTH-SERVICE`, `USER-SERVICE`, `NOTIFICATION-SERVICE`, `AUDIT-SERVICE`, and `API-GATEWAY`
- Gateway routes now use service discovery: `lb://AUTH-SERVICE` and `lb://USER-SERVICE`
- A working **OpenFeign** call: `user-service -> auth-service`
- A Docker Compose setup that runs infrastructure and services together

## Communication map

```text
Client -> API Gateway -> Auth Service / User Service        synchronous REST
User Service -> Auth Service through OpenFeign              synchronous REST
Auth Service -> Kafka -> Audit Service                      asynchronous event
Auth Service -> Kafka -> Notification Service               asynchronous event
Auth Service -> RabbitMQ -> Notification Service            asynchronous command
```

## Why each technology exists

| Technology | In this project | Why |
|---|---|---|
| Gateway | client entry point | routes requests and checks JWT |
| Eureka | service registry | replaces hardcoded service host/port |
| OpenFeign | user-service calls auth-service | gets account status immediately |
| Kafka | audit and business events | many consumers can react independently |
| RabbitMQ | notification command | one worker performs a specific task |

## Run locally without Docker

1. Start MongoDB, Kafka, Zookeeper, RabbitMQ.
2. Run `mvn clean install -DskipTests` from the root.
3. Start services in this order:
   - discovery-server
   - auth-service
   - user-service
   - notification-service
   - audit-service
   - api-gateway
4. Visit Eureka: `http://localhost:8761`.

## Run with Docker Compose

First package the jar files:

```bash
mvn clean package -DskipTests
docker compose up --build
```

Useful URLs:

- Gateway: `http://localhost:8080`
- Eureka: `http://localhost:8761`
- RabbitMQ UI: `http://localhost:15672` (`guest` / `guest`)
- Auth Swagger: `http://localhost:8081/swagger-ui/index.html`

## Feign learning example

`GET /api/users/me` reaches user-service through the gateway.

1. Gateway validates the JWT and adds `X-User-Id`.
2. User-service loads the user profile from `sso_user`.
3. User-service uses `AuthServiceClient` (`@FeignClient(name = "auth-service")`).
4. Eureka finds a live AUTH-SERVICE instance.
5. The Feign client calls `GET /internal/users/{id}`.
6. User-service combines profile data plus account data into `UserProfileView`.

For local learning, `/internal/**` is permitted in auth-service. In a production version, it must be protected using service-to-service authentication (mTLS, OAuth2 client credentials, or a signed internal token).
