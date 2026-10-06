# Charukesh H: Cybersecurity Portfolio

```
portfolio/
  frontend/    index.html (the website, single file) + resume.pdf (you add)
  backend/     Spring Boot API + Dockerfile
  database/    schema.sql (MySQL)
  .env.example
```

## Quick start (website only)
Open `frontend/index.html` in a browser to preview. To publish, upload the `frontend` folder to Netlify, GitHub Pages or Cloudflare Pages. Edit the `CFG` block near the bottom of `index.html` to add your GitHub and LinkedIn URLs. The contact form currently opens your email app; it uses the backend only after the form is wired to your API URL.

---

# Backend

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

## Deploy on Render (Docker) + Aiven MySQL
1. Create a MySQL service on Aiven (or another MySQL host). Run `database/schema.sql` on it, and note host, port, user and password.
2. Push this project to a GitHub repo.
3. On Render: New > Web Service > connect the repo.
   - Language: **Docker**
   - Root Directory: `backend`
   - Instance type: Free (expect slow first request after idle)
4. Add environment variables:
   - `DB_URL` = `jdbc:mysql://HOST:PORT/DBNAME?sslMode=REQUIRED`
   - `DB_USER`, `DB_PASSWORD`
   - `ALLOWED_ORIGIN` = your website URL, e.g. `https://yourname.netlify.app` (no trailing slash)
5. Deploy, then open `https://YOUR-SERVICE.onrender.com/api/health`. You should see `{"status":"online"}`.
6. Use `https://YOUR-SERVICE.onrender.com/api/contact` as the API URL in the front end.
Render sets `PORT` automatically; the app already reads it.
