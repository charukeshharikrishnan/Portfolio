# Charukesh H: Portfolio Backend

Spring Boot 3 (Java 17) REST API + MySQL for the portfolio contact form.

## Endpoints
- `GET  /api/health` returns `{"status":"online"}`
- `POST /api/contact` with JSON `{name, email, subject, message}`. Returns 201, 400 (invalid), or 429 (rate limited)

## Setup
1. Database: `mysql -u root -p < database/schema.sql`, then create the app user (see comments in the file).
2. Environment: copy `.env.example` to `.env` and export the values (or set them in your host).
3. Run: `cd backend && mvn spring-boot:run`
4. Build: `mvn clean package` then `java -jar target/portfolio-1.0.0.jar`
5. Test: `curl -X POST localhost:8080/api/contact -H "Content-Type: application/json" -d '{"name":"Test","email":"a@b.co","subject":"Hi","message":"Hello from curl test"}'`

## Security
Server-side validation, HTML/control-char stripping, JPA parameterized queries, per-IP rate limit, CORS locked to `ALLOWED_ORIGIN`, security headers, no stack traces in errors, secrets only via environment. If you deploy behind a proxy, configure `server.forward-headers-strategy` so the rate limiter sees the real client IP.

## Known limits
Rate limiting is in-memory (per instance). Messages are stored only; nothing emails you yet, so check the table or add a mail service.
