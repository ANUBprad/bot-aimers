# Desktop client ↔ Spring Boot (Phase 11)

The LibGDX client still runs offline. At the end of each game it makes **one asynchronous best-effort HTTP POST** to `http://localhost:8080/api/played-matches`. When the server is unavailable, gameplay still works and the UI displays `Offline - result not saved`.

## Windows PowerShell

From the repository root, after `git pull origin main`:

```powershell
mvn -f engine-v2/pom.xml clean install
mvn -f game-server/pom.xml clean verify
mvn -f game-client/pom.xml compile
```

In terminal 1, start the backend:

```powershell
mvn -f game-server/pom.xml spring-boot:run
```

In terminal 2, start the game:

```powershell
mvn -f game-client/pom.xml exec:java
```

Finish a game and wait for the on-screen `Result saved to Spring Boot` message. In terminal 3:

```powershell
Invoke-RestMethod http://localhost:8080/api/played-matches
```

## API

- `POST /api/played-matches`: report a finished local game
- `GET /api/played-matches`: list local game reports
- Existing `/api/matches` endpoints continue to simulate AI-versus-AI matches independently.

**Security caveat:** Played match reports are *client supplied and unverified*. The server checks basic field consistency but does not replay or authenticate game actions. They must not be used for competitive leaderboards or ranked multiplayer. Persistence is in-memory only, and the current prototype is intended for localhost development. Future phases should introduce server-authoritative sessions and persistent storage.
