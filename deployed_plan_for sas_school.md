# Production Deployment & CI/CD Implementation Plan (2 App Containers: Frontend + Combined Backend, MongoDB, Nginx, Cloudflare Tunnel & GitHub Actions)

## Goal Description

Containerize and deploy the entire **EduVias / School SaaS** platform (`Fourious7AB/eduvias`) onto your **Ubuntu/Debian Linux home server** using **two separate application Docker containers**—**1 Frontend container** and **1 Combined Backend container** (running all 9 Spring Boot microservices under `supervisord`)—paired with **MongoDB 7**, **Nginx**, **Cloudflare Tunnel (`cloudflared`)**, and automated **GitHub Actions CI/CD** (GHCR + Self-Hosted Runner).

### Container Inventory (5 Total Containers)

| Container | Image | Internal Ports | Purpose | Persistent Storage |
| :--- | :--- | :--- | :--- | :--- |
| **`eduvias-frontend`** | `ghcr.io/fourious7ab/eduvias-frontend` | `80` | Vite + React 18 SPA (`Saas_School_Frontend_New`) served statically via Nginx | None (stateless) |
| **`eduvias-backend`** | `ghcr.io/fourious7ab/eduvias-backend` | `8081, 8082, 8084, 8085, 8089, 8090, 8092, 8094, 8099` | All 9 Java 17 Spring Boot microservices managed independently by `supervisord` | `backend_data` volume (`/app/data`: uploads, assignments, submissions, ID cards, TC docs) |
| **`eduvias-mongodb`** | `mongo:7` | `27017` | Authenticated MongoDB 7 hosting all 9 databases | `mongo_data` volume (`/data/db`) |
| **`eduvias-nginx`** | `nginx:1.27-alpine` | `80` (`127.0.0.1:8080:80` local debug only) | Edge reverse-proxy gateway routing `/` -> `frontend:80` and `/service1/`–`/service9/` -> `backend:<port>` | Bind-mounted `./deploy/nginx/nginx.conf` |
| **`eduvias-cloudflared`** | `cloudflare/cloudflared:latest` | None (outbound only) | Zero-port-forwarding encrypted Cloudflare Tunnel routing public HTTPS domain -> `http://nginx:80` | None |

### Microservices Inside `eduvias-backend` (`supervisord`)

| Program Name | Port | Nginx Route | Database (`mongodb:27017`) | JVM Heap (`-Xms` / `-Xmx`) | Working Dir (`/app/data/...`) |
| :--- | :---: | :--- | :--- | :---: | :--- |
| `academic-main` | `8081` | `/service5/` | `sas_academicMain` | `64m` / `256m` | `/app/data/academic-main` |
| `new-library` | `8082` | `/service8/` | `sas_libraryDatabase` | `64m` / `256m` | `/app/data/new-library` |
| `routine-main` | `8084` | `/service7/` | `sas_routineMain` | `64m` / `256m` | `/app/data/routine-main` |
| `admission` | `8085` | `/service1/` | `sas_schoolAdmissionMain` | `96m` / `384m` | `/app/data/admission` |
| `teacher` | `8089` | `/service2/` | `sas_teachersdb` | `96m` / `320m` | `/app/data/teacher` |
| `settings` | `8090` | `/service3/` | `sas_setting` | `48m` / `160m` | `/app/data/settings` |
| `auth-service` | `8092` | `/service4/` | `auth_sas` | `64m` / `256m` | `/app/data/auth-service` |
| `result` | `8094` | `/service6/` | `sas_result1` | `96m` / `384m` | `/app/data/result` |
| `school-account` | `8099` | `/service9/` | `sas_schoolAccount` | `96m` / `384m` | `/app/data/school-account` |

> [!TIP]
> **Key Benefits of This Setup**
> - **True Frontend / Backend Container Isolation**: Deploying a frontend UI change rebuilds and restarts **only** `eduvias-frontend` (zero backend interruption). Deploying a backend change rebuilds and restarts **only** `eduvias-backend` (frontend stays online).
> - **Zero Inter-Service Network Latency**: All 9 Spring Boot services communicate over container `localhost:808x` inside `eduvias-backend`, while MongoDB and Nginx communicate over the isolated Docker bridge network.
> - **Fast Incremental Backend Builds**: `Dockerfile.backend` uses **9 parallel BuildKit stages** (one per microservice) with GitHub Actions layer caching—so editing `Result` gets 8/9 cache hits and only recompiles `Result`.
> - **Individual Service Control**: You can inspect or restart any single microservice without restarting the other 8 using `docker exec eduvias-backend supervisorctl status` or `supervisorctl restart result`.

---

## System & CI/CD Architecture

### 1. Home Server Runtime Architecture

```mermaid
flowchart LR
    User["Browser / Mobile User"] -->|"HTTPS (TLS 1.3)"| CF["Cloudflare Edge (DNS + WAF + DDoS + SSL)"]
    CF <-->|"Outbound QUIC/HTTP2 Tunnel (No Open Router Ports)"| Tunnel["Container 5: cloudflared"]

    subgraph HomeServer ["Ubuntu/Debian Home Server (Docker Compose)"]
        Tunnel -->|"http://nginx:80"| Nginx["Container 4: nginx (:80)"]
        Nginx -->|"/"| FE["Container 1: frontend (Vite React SPA)"]
        Nginx -->|"/service1/ .. /service9/"| BE

        subgraph BE ["Container 2: backend (supervisord — 9 Spring Boot JVMs)"]
            S1["admission :8085"]
            S2["teacher :8089"]
            S3["settings :8090"]
            S4["auth-service :8092"]
            S5["academic-main :8081"]
            S6["result :8094"]
            S7["routine-main :8084"]
            S8["new-library :8082"]
            S9["school-account :8099"]
        end

        BE -->|"mongodb://user:pass@mongodb:27017/<db>"| Mongo["Container 3: mongodb (MongoDB 7 + Auth)"]
        BE -.->|"Persistent Volume"| VolData["backend_data (/app/data: uploads, id_cards, output)"]
        Mongo -.->|"Persistent Volume"| VolMongo["mongo_data (/data/db)"]
    end
```

### 2. GitHub Actions CI/CD Pipeline

```mermaid
flowchart TD
    Push["Push to main / PR / Manual workflow_dispatch"] --> Detect["Job 1: detect-changes (frontend / backend / infra)"]
    Detect --> TestFE["Job 2a: Frontend CI (npm ci + npm run build)"]
    Detect --> TestBE["Job 2b: Backend CI (Maven tests on changed services)"]
    TestFE & TestBE -->|"PR stops here"| PRCheck["PR Status Check"]
    TestFE -->|"frontend changed on main"| BuildFE["Job 3a: Build & Push eduvias-frontend -> GHCR"]
    TestBE -->|"backend changed on main"| BuildBE["Job 3b: Build & Push eduvias-backend (9 parallel BuildKit stages) -> GHCR"]
    BuildFE & BuildBE --> Deploy["Job 4: deploy-home-server (Self-Hosted Linux Runner)"]
    Deploy --> Sync["Sync docker-compose.prod.yml + deploy/nginx + scripts to /opt/eduvias"]
    Sync --> Rollout["Pull & recreate ONLY changed container(s) (frontend / backend / nginx)"]
    Rollout --> Health["Verify all 9 /actuator/health + frontend + nginx smoke tests"]
    Health -->|"Pass"| Prune["Prune unused images (>7 days) & Complete"]
    Health -->|"Fail"| Rollback["Auto-Rollback to Previous Image Tags & Print Logs"]
```

---

## User Review Required

> [!CAUTION]
> **Exposed Credentials Already Present in Codebase / Git History**
> Plaintext credentials currently exist inside `application.properties`, `EmailConfig.java`, and commented lines in `Saas_School_Frontend_New/.env` (Gmail app passwords, Razorpay keys, Cloudinary API secret, and old VPS MongoDB passwords).
> - We will externalize all production secrets into `/opt/eduvias/.env` (untracked on the home server) and GitHub Secrets, and remove hardcoded secrets from `EmailConfig.java`.
> - **Action for you before go-live**: Rotate those Gmail App Passwords, Razorpay keys, and Cloudinary API secret once deployed.

> [!IMPORTANT]
> **Tracked Build Artifacts (`target/`) in Git**
> Currently, **967 compiled files** inside `*/target/*` are tracked in Git (361 MB `.git` repo) and there is no root `.gitignore`.
> - Adding a root `.gitignore` prevents future build artifacts from being committed. Confirm if you also want me to run `git rm -r --cached` on `*/target/*` to clean up git tracking (your local files stay untouched).

> [!IMPORTANT]
> **Memory Allocation for the Combined Backend Container**
> Because 9 JVMs run inside `eduvias-backend`, we must NOT use `-XX:MaxRAMPercentage=75.0` (which would cause OOM kills). Instead, `supervisord` starts each JVM with explicit `-Xms`/`-Xmx` bounds totaling **2.69 GB max heap**, with a `4g` container limit (`5.5 GB` total ceiling across all 5 containers; ~2.5–3.2 GB steady-state usage — ideal for an **8 GB+ RAM** Ubuntu home server).

> [!NOTE]
> **Preserving Your Current Uncommitted Work**
> Your workspace currently has uncommitted changes in `Result-0.0.1-SNAPSHOT` and `Saas_School_Frontend_New`. None of those changes will be reverted or overwritten.

---

## Open Questions

> [!NOTE]
> These do not block implementation (clean placeholders work in `.env.prod.example`), but let me know if you want them pre-filled now:
> 1. **Production Domain**: What domain/subdomain will point to your Cloudflare Tunnel (e.g., `https://edu.yourdomain.com`)?
> 2. **Home Server RAM**: Does your Ubuntu/Debian home server have **8 GB+ RAM**?
> 3. **Git `target/` Cleanup**: Should I also untrack existing `*/target/*` files from Git (`git rm -r --cached`) when adding `.gitignore`?
> 4. **Optional `schoolfeed` Service**: Assumed unused/excluded (commented out in frontend `.env`) unless you need it.

---

## Proposed Changes

Changes are organized logically by layer:
1. **Backend Spring Boot Docker-Readiness & Healthchecks**
2. **Container #1: Frontend Dockerfile & Static Nginx Config**
3. **Container #2: Combined Backend Dockerfile, `supervisord` & Runtime Scripts**
4. **Container #4: Edge Nginx Reverse-Proxy Gateway**
5. **Docker Compose Production & Local Verification Stacks**
6. **GitHub Actions CI/CD Workflow (Independent Frontend & Backend Builds/Deploys)**
7. **Home Server Bootstrap, Deploy, Backup & MongoDB Migration Scripts**
8. **Repository Hygiene & Operator Deployment Guide**

---

### 1. Backend Spring Boot Docker-Readiness & Healthchecks

All 9 services stay **100% compatible with local IntelliJ / `start-all-services.ps1` development** by keeping `localhost` defaults inside `${ENV_VAR:default}` placeholders.

#### [MODIFY] All 9 `pom.xml` files
Add `spring-boot-starter-actuator` to all 9 active microservices (`AcademicMain`, `Admission`, `auth-service-0.0.2`, `NewLibrary`, `Result`, `RoutineMain`, `SchoolAccount-0.0.2`, `Settings`, `teacher`):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### [MODIFY] All 9 `application.properties` files
Update each service's `src/main/resources/application.properties` to:
1. Externalize `spring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI:mongodb://localhost:27017/<db>?directConnection=true}`.
2. Fix `Admission`'s Windows path `file.upload-dir=C:/Users/VAIS-DT15/...` -> `file.upload-dir=${FILE_UPLOAD_DIR:./uploads/schoollogo/}`.
3. Externalize SMTP (`SPRING_MAIL_*`), Razorpay (`RAZORPAY_KEY_ID`, `RAZORPAY_KEY_SECRET`, `PAYMENT_WEBHOOK_SECRET`), and Cloudinary (`CLOUDINARY_*`) credentials to environment variables with safe local fallbacks.
4. Enable internal Actuator healthcheck only and reverse-proxy header forwarding:
   ```properties
   management.endpoints.web.exposure.include=health
   management.endpoint.health.show-details=never
   server.forward-headers-strategy=native
   ```

#### [MODIFY] [SecurityConfig.java](file:///c:/Users/VAIS-DT4/Desktop/DonotDeleteIt/Recovered-SchoolSaaS-20261121-152414/auth-service-0.0.2-SNAPSHOT/IntelliJ-Project/src/main/java/com/neel/Auth/security/SecurityConfig.java)
Permit `"/actuator/health"` in `requestMatchers(...)` so the backend container healthcheck succeeds without a JWT (Nginx blocks `/service*/actuator` from external access):

```diff
- .requestMatchers(new String[]{"/api/auth/login", "/api/auth/logout", "/api/auth/register", "/api/auth/forgot-password", "/api/auth/reset-password", "/api/auth/delete/{username}"})
+ .requestMatchers(new String[]{"/api/auth/login", "/api/auth/logout", "/api/auth/register", "/api/auth/forgot-password", "/api/auth/reset-password", "/api/auth/delete/{username}", "/actuator/health"})
```

#### [MODIFY] [EmailConfig.java](file:///c:/Users/VAIS-DT4/Desktop/DonotDeleteIt/Recovered-SchoolSaaS-20261121-152414/auth-service-0.0.2-SNAPSHOT/IntelliJ-Project/src/main/java/com/neel/Auth/config/EmailConfig.java)
Replace hardcoded Gmail host/port/username/password with `@Value("${spring.mail.*}")` so credentials come from environment variables instead of Java source.

#### [MODIFY] [AuthController.java](file:///c:/Users/VAIS-DT4/Desktop/DonotDeleteIt/Recovered-SchoolSaaS-20261121-152414/auth-service-0.0.2-SNAPSHOT/IntelliJ-Project/src/main/java/com/neel/Auth/controller/AuthController.java) & [EmailService.java](file:///c:/Users/VAIS-DT4/Desktop/DonotDeleteIt/Recovered-SchoolSaaS-20261121-152414/teacher-0.0.1-SNAPSHOT/IntelliJ-Project/src/main/java/com/neel/Teacher/service/EmailService.java)
Replace hardcoded `"https://edu.vaisacademy.com"` email links with `@Value("${app.frontend.url:https://edu.vaisacademy.com}") private String frontendUrl;`.

---

### 2. Container #1: Frontend Dockerfile & Static Nginx Config

#### [NEW] `.dockerignore` (repository root)
Excludes `node_modules/`, `*/target/`, `dist/`, `.git/`, `service-logs/`, `uploads/`, `.env*`, legacy backup folders (`Saas_School_Frontend_New - Copy/`, `auth-service-0.0.1-SNAPSHOT/`, `SchoolAccount-0.0.1-SNAPSHOT/`, `StudentAdmission/`), and docs so Docker build contexts stay fast and minimal.

#### [NEW] `deploy/docker/Dockerfile.frontend`
Dedicated **Frontend-only Docker container**:
- **Stage 1 (`node:22-alpine`)**: `npm ci`, injects build-time `VITE_REACT_SERVICE1_URL=/service1` .. `/service9`, `VITE_REACT_razorpay_key`, `VITE_REACT_VERSION`, and runs `npm run build`.
- **Stage 2 (`nginx:1.27-alpine`)**: Copies built `dist/` into `/usr/share/nginx/html` with `deploy/nginx/frontend.conf`.

#### [NEW] `deploy/nginx/frontend.conf`
Serves the Vite React SPA with `try_files $uri $uri/ /index.html;` (required for `createBrowserRouter`), immutable caching on `/assets/*`, `no-cache` on `index.html`, gzip compression, and `/healthz` healthcheck endpoint.

---

### 3. Container #2: Combined Backend Dockerfile, `supervisord` & Runtime Scripts

#### [NEW] `deploy/docker/Dockerfile.backend`
Dedicated **Backend-only Docker container** bundling all 9 Spring Boot microservices cleanly:
- **9 Parallel BuildKit Maven Builder Stages** (`maven:3.9.9-eclipse-temurin-17`):
  - One stage per service (`build-academic-main`, `build-admission`, `build-auth-service`, `build-new-library`, `build-result`, `build-routine-main`, `build-school-account`, `build-settings`, `build-teacher`).
  - Each stage copies its `pom.xml` first (`mvn dependency:go-offline -B`) and then `src/` (`mvn package -DskipTests -B`), ensuring per-service BuildKit cache isolation when only 1 microservice changes.
- **Final Runtime Stage (`eclipse-temurin:17-jre-jammy`)**:
  - Installs `supervisor`, `curl`, `fontconfig`, `fonts-dejavu-core` (prevents Java AWT / `IDCardGenerator` and iText PDF font crashes on Linux headless JVMs).
  - Creates non-root user `appuser` and copies all 9 built JARs into `/app/jars/`.
  - Copies `supervisord.conf`, `run-service.sh`, and `healthcheck-backend.sh`.
  - Runs `supervisord -n -c /etc/supervisor/supervisord.conf` as container entrypoint (properly handles `SIGTERM` for graceful shutdown of all 9 Spring Boot services!).

#### [NEW] `deploy/docker/backend/supervisord.conf`
Manages all 9 microservices inside `eduvias-backend`:
- Auto-restarts any crashed individual microservice (`autorestart=true`, `startretries=5`, `stopwaitsecs=20`) without affecting the other 8.
- Sets isolated working directories per service (`/app/data/admission`, `/app/data/teacher`, etc.) so relative paths like `uploads/assignments/`, `uploads/submissions/`, `id_cards/transfer_certificates/`, `bulk_id_cards/`, and `src/main/resources/output/` persist cleanly inside the `backend_data` Docker volume.
- Streams prefixed logs (`[admission]`, `[result]`, `[auth-service]`, etc.) directly to container `stdout` so `docker logs -f eduvias-backend` works cleanly with Docker's JSON log rotation.

#### [NEW] `deploy/docker/backend/run-service.sh`
Lightweight per-service launcher invoked by `supervisord`:
- Ensures the service's persistent data subdirectories exist inside `/app/data/<service>`.
- Builds the service-specific authenticated `SPRING_DATA_MONGODB_URI` (`mongodb://<user>:<pass>@<host>:<port>/<db>?authSource=admin&directConnection=true`) and isolates per-service env vars (e.g., `AUTH_MAIL_*` for `auth-service` vs `MAIL_*` for `admission`/`teacher`, and `FILE_UPLOAD_DIR=/app/data/admission/uploads/schoollogo/` for `admission`).
- Launches `java -Xms<min> -Xmx<max> -XX:+UseG1GC -XX:MaxMetaspaceSize=160m -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom -jar /app/jars/<service>.jar`.

#### [NEW] `deploy/docker/backend/healthcheck-backend.sh`
Checks `http://127.0.0.1:<port>/actuator/health` across all 9 ports (`8081, 8082, 8084, 8085, 8089, 8090, 8092, 8094, 8099`). Exits `0` only when all 9 services report `"UP"`.

---

### 4. Container #4: Edge Nginx Reverse-Proxy Gateway

#### [NEW] `deploy/nginx/nginx.conf`
Production edge reverse proxy behind `cloudflared`:
- **Cloudflare Real-IP restoration**: Trusts Docker bridge subnets (`10.0.0.0/8`, `172.16.0.0/12`, `192.168.0.0/16`) and reads `CF-Connecting-IP`.
- **Dynamic Docker DNS**: `resolver 127.0.0.11 valid=10s ipv6=off;` + variable `proxy_pass` targets (`$frontend_upstream`, `$backend_host`) so recreating `frontend` or `backend` independently **never** causes stale-IP `502 Bad Gateway` errors.
- **Exact Routing**:
  - `/` -> `http://frontend:80`
  - `/service1/` -> `http://backend:8085/` (Admission)
  - `/service2/` -> `http://backend:8089/` (Teacher)
  - `/service3/` -> `http://backend:8090/` (Settings)
  - `/service4/` -> `http://backend:8092/` (Auth-Service, with login/reset rate limiting)
  - `/service5/` -> `http://backend:8081/` (AcademicMain)
  - `/service6/` -> `http://backend:8094/` (Result)
  - `/service7/` -> `http://backend:8084/` (RoutineMain)
  - `/service8/` -> `http://backend:8082/` (NewLibrary)
  - `/service9/` -> `http://backend:8099/` (SchoolAccount)
- **Security & Uploads**:
  - Blocks `/service[1-9]/actuator` externally (`return 404;`).
  - `client_max_body_size 50M;` and `proxy_read_timeout 120s;` for bulk student Excel imports, assignments, PDFs, and ZIP ID-card exports.
  - Security headers (`X-Frame-Options`, `X-Content-Type-Options`, `Referrer-Policy`, `Permissions-Policy`), gzip, and `/nginx-health`.

---

### 5. Docker Compose Production & Local Verification Stacks

#### [NEW] `docker-compose.prod.yml`
Defines the 5 production containers (`frontend`, `backend`, `mongodb`, `nginx`, `cloudflared`):
- Separate image tags (`FRONTEND_IMAGE_TAG` and `BACKEND_IMAGE_TAG`, defaulting to `latest`) so frontend and backend can be deployed and rolled back **completely independently**!
- Isolated internal network (`eduvias_net`): neither `mongodb` nor `backend` publish any host ports; `nginx` optionally binds `127.0.0.1:${NGINX_LOCAL_PORT:-8080}:80` for local runner smoke tests.
- `cloudflared` uses `profiles: ["tunnel"]` or runs by default in prod with `CLOUDFLARE_TUNNEL_TOKEN`.
- Persistent named volumes (`mongo_data`, `backend_data`), container memory limits (`frontend: 128m`, `backend: 4g`, `mongodb: 1g`, `nginx: 128m`, `cloudflared: 128m`), `restart: unless-stopped`, JSON log rotation (`20m` × `5` files), and full healthchecks.

#### [NEW] `docker-compose.local.yml`
Local testing override file (`docker compose -f docker-compose.prod.yml -f docker-compose.local.yml up -d --build`) that builds both `frontend` and `backend` locally from source, exposes `http://localhost:8080`, and skips `cloudflared` so we can fully test the entire stack locally on your PC before deploying to your home server!

#### [NEW] `deploy/env/.env.prod.example`
Documented template for `/opt/eduvias/.env` on your home server (MongoDB auth, Cloudflare Tunnel token, `APP_FRONTEND_URL`, Gmail SMTP, Razorpay, Cloudinary, GHCR owner).

---

### 6. GitHub Actions CI/CD Workflow

#### [NEW] `.github/workflows/ci-cd.yml`
Independent Frontend & Backend CI/CD pipeline:
- **Triggers**:
  - `push` to `main` (builds & deploys **only** what changed: frontend, backend, and/or nginx/compose!)
  - `pull_request` to `main` (runs tests + builds only, no push or deploy)
  - `workflow_dispatch` with inputs:
    - `target` (`auto` | `all` | `frontend` | `backend` — choose exactly what to rebuild/deploy!)
    - `rollback_frontend_tag` / `rollback_backend_tag` (optional Git SHA tags to roll back independently!)
- **Jobs**:
  1. `detect-changes`: Uses `dorny/paths-filter@v3` to independently detect `frontend`, `backend`, and `infra` changes.
  2. `test-frontend` & `test-backend`: Runs `npm ci && npm run build` (frontend) and `mvn test` on `Result-0.0.1-SNAPSHOT` (backend).
  3. `build-frontend`: Builds & pushes `ghcr.io/fourious7ab/eduvias-frontend:<sha>` & `:latest` on GitHub `ubuntu-latest` with Buildx GHA cache.
  4. `build-backend`: Builds & pushes `ghcr.io/fourious7ab/eduvias-backend:<sha>` & `:latest` on GitHub `ubuntu-latest` with 9 parallel cached BuildKit stages.
  5. `deploy-home-server`: Runs on `[self-hosted, linux]` (`concurrency: production-deploy`), syncs compose/nginx/scripts to `/opt/eduvias`, and runs `deploy.sh --target <frontend|backend|all>` so **only the updated container is pulled and recreated**, verifies healthchecks + Nginx smoke tests, and auto-rolls back if anything fails!

---

### 7. Home Server Bootstrap, Deploy, Backup & Migration Scripts

#### [NEW] `deploy/scripts/setup-home-server.sh`
One-time Ubuntu/Debian home server setup: installs Docker Engine + Compose plugin, configures UFW firewall (LAN SSH only, zero public inbound ports), creates `/opt/eduvias` & `/opt/eduvias/backups`, guides GitHub Self-Hosted Runner `systemd` service setup, and installs daily MongoDB backup cron job.

#### [NEW] `deploy/scripts/deploy.sh`
Supports targeted rollouts (`--target frontend`, `--target backend`, `--target infra`, or `--target all`):
- Snapshots currently running image IDs for `frontend` and `backend` before deployment.
- Pulls and updates **only** the targeted container(s) (`docker compose up -d --no-deps frontend` or `backend`) so frontend updates never disrupt backend and vice versa.
- Waits for Docker `healthy` status on `mongodb`, `backend` (all 9 `/actuator/health` checks), `frontend`, and `nginx`, then runs HTTP smoke tests against `http://127.0.0.1:8080`.
- Automatically rolls back to the last good image on failure and prunes dangling images older than 7 days on success.

#### [NEW] `deploy/scripts/backup-mongo.sh`
Daily authenticated `mongodump --gzip --archive` of all 9 databases into `/opt/eduvias/backups/` with 14-day retention.

#### [NEW] `deploy/scripts/migrate-mongo.ps1` & `deploy/scripts/restore-mongo.sh`
Exports all 9 databases from your current Windows/LAN MongoDB (`school-saas-mongodb` or `192.168.11.157:27017`) and restores them cleanly into the authenticated `eduvias-mongodb` container on your home server.

---

### 8. Repository Hygiene & Operator Guide

#### [NEW] `.gitignore` (repository root)
Ignores Maven `target/`, `node_modules/`, `dist/`, `.env` / `.env.prod`, `service-logs/`, `uploads/`, `id_cards/`, `bulk_id_cards/`, `.idea/`, `*.log`, and `desktop.ini`.

#### [NEW] `DEPLOYMENT_GUIDE.md`
Step-by-step operator runbook covering Ubuntu server setup, Cloudflare Zero Trust Tunnel configuration (`http://nginx:80` + SSL/Caching/WAF settings), GitHub Actions Secrets & Self-Hosted Runner registration, MongoDB data migration, independent Frontend/Backend deployments, `supervisorctl` per-microservice management, backups, and rollbacks.

---

## Verification Plan

### Automated Tests & Local Verification (On This Machine)
1. **Backend Compilation & JUnit Tests**:
   - Compile all 9 microservices with the new Actuator dependency and externalized config, and run `mvn test` on `Result-0.0.1-SNAPSHOT`.
2. **Frontend Production Build**:
   - Run `npm run build` in `Saas_School_Frontend_New/` to verify clean bundling.
3. **Config & Script Syntax Validation**:
   - Validate `docker-compose.prod.yml` and `docker-compose.local.yml` with `docker compose config --quiet`.
   - Validate `deploy/nginx/nginx.conf` and `deploy/nginx/frontend.conf` with `nginx -t` inside `nginx:1.27-alpine`.
   - Validate all shell scripts (`bash -n`) and GitHub Actions YAML syntax.
4. **Full Local 4-Container Stack Smoke Test** (excluding `cloudflared`):
   - Run `docker compose -f docker-compose.prod.yml -f docker-compose.local.yml up -d --build`.
   - Verify **separate** running containers: `eduvias-frontend`, `eduvias-backend`, `eduvias-mongodb`, and `eduvias-nginx`.
   - Verify `docker exec eduvias-backend supervisorctl status` shows all 9 microservices `RUNNING`.
   - Verify all 4 containers report `healthy` (meaning all 9 `/actuator/health` endpoints returned `"UP"` against authenticated MongoDB).
   - Verify HTTP routing through Nginx (`http://localhost:8080`):
     - `/` and `/dashboard` return React `index.html` (`200`)
     - `/service1/actuator/health` returns `404` (blocked externally)
     - API routes through `/service3/`, `/service4/`, etc. respond properly from `eduvias-backend`.
   - Verify independent restart: restart `eduvias-frontend` alone and confirm `eduvias-backend` uptime is untouched (and vice versa) with zero Nginx `502` errors.

### Manual Verification (On Your Ubuntu Home Server)
1. Run `deploy/scripts/setup-home-server.sh` on the Ubuntu home server and register the GitHub Self-Hosted Runner.
2. Create the Cloudflare Tunnel in Cloudflare Zero Trust, map your domain to `http://nginx:80`, and populate `/opt/eduvias/.env`.
3. Trigger GitHub Actions (`workflow_dispatch`, `target: all`) to push both `eduvias-frontend` and `eduvias-backend` to GHCR and auto-deploy on the home server.
4. Open `https://<your-domain>`, verify HTTPS lock icon, log in via `/service4`, test core modules, and verify `backup-mongo.sh` creates an archive in `/opt/eduvias/backups/`.
