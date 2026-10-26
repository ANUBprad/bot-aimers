# Phase 12 — PostgreSQL persistence

The Spring Boot backend now stores both AI-simulated matches and LibGDX-reported local matches in PostgreSQL. Flyway creates the schema automatically. The desktop game remains playable without the server.

## Prerequisites

Docker Desktop with Linux containers enabled; Java 21+ and Maven.

## Windows PowerShell

From `C:\projects\bot-aimers`:

```powershell
git pull origin main
mvn -f engine-v2/pom.xml clean install
mvn -f game-server/pom.xml clean verify
docker compose -f game-server/docker-compose.yml up -d
docker compose -f game-server/docker-compose.yml ps
mvn -f game-server/pom.xml spring-boot:run
```

The development database is exposed **only on localhost:5432**. Default local credentials are `botaimers` / `botaimers_dev`; override `DB_PASSWORD` for anything beyond a local prototype.

In another PowerShell window:

```powershell
mvn -f game-client/pom.xml exec:java
```

Finish a match, then inspect:

```powershell
Invoke-RestMethod http://localhost:8080/api/played-matches
Invoke-RestMethod http://localhost:8080/api/players/Player/stats
```

Restart Spring Boot, repeat the GET requests, and confirm the IDs are unchanged. To stop PostgreSQL without deleting history:

```powershell
docker compose -f game-server/docker-compose.yml down
```

**Never run `docker compose down -v` if you want to retain data.** It deletes the named PostgreSQL volume.

## Existing data and constraints

Previous Phase 11 matches were held in server memory and cannot be recovered after that process stops. The first PostgreSQL startup begins with an empty history unless you previously exported the records. Future games persist across backend restarts.

The database is intended for local development. Match reports are still client-submitted and `verified=false`, so player statistics are informational, not tamper-proof ranked scores.

Database connection can be overridden with `DB_URL`, `DB_USER`, and `DB_PASSWORD`. Maven tests use an isolated in-memory H2 database in PostgreSQL compatibility mode; no running Docker container is required for tests.
