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
                 ↘ CANCELLED ↗
```

## Requirements

- Java 17+
- Maven 3.9+

## Run locally (H2)

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
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

## Docker

```bash
docker compose up --build
```

Default compose uses PostgreSQL. For a quick demo without Docker DB, prefer the `local` profile above.

## Tech

- Spring Boot 3.5 / Java 17
- Spring Data JPA
- springdoc OpenAPI
- H2 (`local`) / PostgreSQL (default)

## License

MIT
