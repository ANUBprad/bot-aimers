# Bot-Aimers

A playable **Java 21 / LibGDX 2D tactical combat game** with an independent combat engine and a Spring Boot match-history service.

**Status:** playable desktop prototype. Multiplayer and performance benchmarking are not implemented.

## Play locally

Requirements: JDK 21 or newer, Maven 3.9+, and a desktop environment capable of running LibGDX/LWJGL3.

```powershell
git clone https://github.com/ANUBprad/bot-aimers.git
cd bot-aimers
mvn -f engine-v2/pom.xml clean install
mvn -f game-client/pom.xml compile exec:java
```

The desktop client runs without a backend. The window is 960×640 with a 60 FPS foreground cap and vsync enabled; **these settings are not measured performance results**.

### Controls

| Key | Action |
| --- | --- |
| WASD or arrow keys | Move |
| F | Attack |
| G | Guard |
| R | Restart match |

Fight the CPU on a wall-separated grid. The enemy uses A* pathfinding to navigate obstacles.

## Modules

| Module | Purpose |
| --- | --- |
| `engine-v2/` | Java combat rules, turn management, grid movement, A* pathfinding and bot strategies |
| `game-client/` | LibGDX/LWJGL3 playable desktop client |
| `game-server/` | Spring Boot REST API, database-backed match history and player stats |
| `android/`, `desktop/`, `html/` | Legacy project directories; not the documented launch path for the current game |

## Run automated tests

```powershell
mvn -f engine-v2/pom.xml clean install
mvn -f game-server/pom.xml clean verify
```

A previous fresh-clone verification passed **40 tests** (31 engine, 9 backend), zero failures. Subsequent changes add boundary and match-history regression tests; rerun the commands above to verify the current total. Backend tests use H2 and do not require Docker.

## Optional PostgreSQL match history

Docker Desktop is required for this part only.

```powershell
docker compose -f game-server/docker-compose.yml up -d
mvn -f game-server/pom.xml spring-boot:run
```

Launch the game in another terminal and finish a match. Then query:

```powershell
Invoke-RestMethod http://localhost:8080/api/played-matches
Invoke-RestMethod http://localhost:8080/api/players/Player/stats
```

Match reports are sent asynchronously and are **unverified client submissions**; the statistics are not a competitive leaderboard. PostgreSQL persistence still requires end-to-end verification against a running Docker database. See [database setup](docs/POSTGRESQL_SETUP.md).

## Current limitations

- No networked multiplayer or account authentication
- No published FPS, frame-time or pathfinding benchmarks
- No embedded gameplay screenshot yet; add a real capture from a local run
- Desktop gameplay works offline, but offline matches cannot be uploaded retroactively

## Next milestones

1. Add reproducible performance benchmarks and profiling.
2. Improve combat feedback, movement animation and menus.
3. Verify database persistence across a server restart.
4. Add screenshots and a short gameplay recording captured from the actual application.
