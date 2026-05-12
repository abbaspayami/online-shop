# Online Shop — Microservices Backend

A microservices-based backend for an online shop, built with Spring Boot 3.2, Spring Cloud, and secured with Keycloak.

---

## Architecture

```
Client
  │
  ▼
API Gateway (port 8080)          ← validates Keycloak JWT on every request
  │
  ├──▶ Service Discovery / Eureka (port 8761)   ← service registry
  │
  └──▶ [future microservices registered in Eureka]

Keycloak (port 9090)             ← identity & access management
Keycloak DB / Postgres           ← Keycloak's own database
```

---

## Services

| Service | Port | Description |
|---|---|---|
| `api-gateway` | 8080 | Entry point for all requests. Validates Keycloak JWT tokens. |
| `service-discovery` | 8761 | Eureka server. Registers and discovers all microservices. |
| `keycloak` | 9090 | Identity provider. Issues and validates OAuth2/OIDC tokens. |
| `security-service` | 8070 | ⚠️ **Deprecated** — replaced by Keycloak. Kept for reference only. |

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.2**
- **Spring Cloud 2023.0.0** (Gateway, Eureka)
- **Spring Security OAuth2 Resource Server** (JWT validation)
- **Keycloak 24** (Identity & Access Management)
- **PostgreSQL 15** (Keycloak database)
- **Docker & Docker Compose**

---

## Getting Started

### Prerequisites
- Docker & Docker Compose installed

### Run the project

```bash
docker compose up --build
```

This starts:
- Keycloak + its Postgres database
- Service Discovery (Eureka)
- API Gateway

### First time setup
The `keycloak/online-shop.json` realm is auto-imported on first boot with:
- Realm: `online-shop`
- Roles: `USER`, `ADMIN`
- Client: `online-shop-client`
- Seed users: `admin / admin123` and `user / user123`

---

## Authentication

### Get a token

```bash
curl -X POST http://localhost:9090/realms/online-shop/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=online-shop-client" \
  -d "client_secret=online-shop-secret" \
  -d "username=admin" \
  -d "password=admin123"
```

Response:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5...",
  "expires_in": 300,
  "token_type": "Bearer"
}
```

### Call a protected endpoint

```bash
curl http://localhost:8080/your-service/api/endpoint \
  -H "Authorization: Bearer <access_token>"
```

---

## Keycloak Admin Console

| URL | Credentials |
|---|---|
| `http://localhost:9090` | `admin / admin` |

> **Note:** Realm admin is separate from application users. Use `admin / admin` only for the Keycloak admin console.

---

## Project Structure

```
online-shop/
├── api-gateway/               ← Spring Cloud Gateway + Keycloak JWT validation
│   └── src/main/java/.../
│       ├── ApiGatewayApplication.java
│       └── config/
│           └── SecurityConfig.java
├── service-discovery/         ← Eureka Server
├── security-service/          ← ⚠️ Deprecated (replaced by Keycloak)
├── keycloak/
│   └── online-shop.json       ← Realm auto-import file
├── docker-compose.yml
└── pom.xml                    ← Parent multi-module Maven project
```

---

## Public Endpoints (no token required)

| Path | Description |
|---|---|
| `GET /actuator/health` | Gateway health check |
| `GET /actuator/info` | Gateway info |
| `/eureka/**` | Eureka dashboard |

All other endpoints require a valid Bearer token in the `Authorization` header.

---

## Local Development (without Docker)

Start services in this order:

```bash
# 1. Start Keycloak (Docker only)
docker compose up keycloak-db keycloak

# 2. Start Eureka
cd service-discovery && ./mvnw spring-boot:run

# 3. Start API Gateway
cd api-gateway && ./mvnw spring-boot:run
```
