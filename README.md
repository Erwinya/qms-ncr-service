# qms-ncr-service

REST API for **Nonconformance Reports (NCR)** in a Quality Management System.

Repository: [Erwinya/qms-ncr-service](https://github.com/Erwinya/qms-ncr-service)

## What it does

- Create NCRs with severity, lot/part context, and reporter
- List and filter by status
- Update details while the report is active
- Enforce a clear status workflow:

```text
OPEN → UNDER_REVIEW → CONTAINED → CLOSED
         ↓               ↑
      CANCELLED     UNDER_REVIEW
         ↑               │
         └───────────────┘
  (UNDER_REVIEW may also close directly)
```

This is an intentionally open demo API (no auth) for portfolio / local evaluation.

## Requirements

- Java 17+
- Maven 3.9+

## Run locally (H2 by default)

```powershell
.\mvnw.cmd spring-boot:run
```

Open:

- Swagger UI: http://localhost:8082/swagger-ui.html
- API root redirects to Swagger

## Example

```http
POST /api/v1/ncrs
Content-Type: application/json

{
  "title": "Thickness out of tolerance",
  "description": "Lot exceeded upper thickness limit.",
  "severity": "HIGH",
  "lotNumber": "LOT-4821",
  "partNumber": "WAFER-A",
  "reportedBy": "qa.engineer"
}
```

```http
PUT /api/v1/ncrs/{id}/status
Content-Type: application/json

{
  "status": "UNDER_REVIEW",
  "note": "Assigned to process engineering"
}
```

## Docker (PostgreSQL)

```bash
docker compose up --build
```

Compose sets `SPRING_PROFILES_ACTIVE=docker` so the API uses PostgreSQL (not the default H2 profile).

Published host ports:

| Service | Host port | Notes |
|---------|-----------|--------|
| API | `8082` | Swagger: http://localhost:8082/swagger-ui.html |
| Postgres | `5434` | Maps to container `5432` |

Credentials: database `qms_ncr`, user `qms`, password `qms`.

### Host app + Compose database

If you run the API on the host against the Compose Postgres container:

```powershell
$env:SPRING_PROFILES_ACTIVE="docker"
$env:DB_URL="jdbc:postgresql://localhost:5434/qms_ncr"
$env:DB_USERNAME="qms"
$env:DB_PASSWORD="qms"
.\mvnw.cmd spring-boot:run
```

Use port **5434** (not 5432) when connecting from the host.

## Tech

- Spring Boot 3.5 / Java 17
- Spring Data JPA
- springdoc OpenAPI
- H2 by default (`local` profile) / PostgreSQL via Docker (`docker` profile)

## License

MIT
