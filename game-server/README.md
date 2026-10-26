# Bot-Aimers Spring Boot game server

Spring Boot 3.5.7 backend for the Java 21 tactical combat engine.

## Prerequisites

- JDK 21+
- Maven 3.9+
- The sibling `engine-v2` module

## Build on Windows PowerShell

From the repository root:

```powershell
mvn -f engine-v2/pom.xml clean install
mvn -f game-server/pom.xml clean verify
```

## Start the backend

```powershell
mvn -f game-server/pom.xml spring-boot:run
```

The server listens on `http://localhost:8080` by default.

## Try the API

Open another PowerShell window:

```powershell
$match = Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/matches -ContentType "application/json" -Body '{"firstName":"Alpha","secondName":"Beta"}'
$match.result
Invoke-RestMethod -Uri http://localhost:8080/api/matches
Invoke-RestMethod -Uri "http://localhost:8080/api/matches/$($match.id)"
```

### Endpoints

| Method | Route | Description |
| --- | --- | --- |
| POST | `/api/matches` | Create and simulate a match; JSON body requires `firstName` and `secondName` |
| GET | `/api/matches` | List match history |
| GET | `/api/matches/{id}` | Fetch match details by UUID |

The server reuses `engine-v2` for battle resolution. The API currently simulates deterministic AI-versus-AI matches using fixed bot stats, policies, and a preset arena.

**Current limitations:** History is in memory and resets on restart. There is no authentication, database, real-time multiplayer, or game-client integration yet. Do not expose this development server publicly without appropriate security and resource limits.
