# Isometric visual prototype — Operation Iron Veil

The LibGDX desktop client now draws an **asset-free, military sci-fi isometric arena** using `ShapeRenderer`: diamond floor tiles, elevated industrial cover, distinct player/hostile units, a tactical HUD, health bars, pause state, and mission-end messaging.

The visual projection does **not** change engine coordinates or combat rules. The existing `PlayerBattle`, A* AI, backend reporting and engine tests remain intact.

## Windows PowerShell

```powershell
cd C:\projects\bot-aimers
git pull origin main
mvn -f engine-v2/pom.xml clean install
mvn -f game-server/pom.xml clean verify
mvn -f game-client/pom.xml clean compile
mvn -f game-client/pom.xml exec:java
```

Controls: WASD/arrows move in **logical grid directions** (not screen diagonals), F attacks an adjacent hostile, G guards, P/Escape pauses, and R restarts. Combat is still turn-based, not real-time shooting.

## Known limitations

- Procedural geometric soldiers and cover are placeholders, not authored character art or sprite animations.
- No audio, particles, lighting shader, or animated interpolation yet.
- Rendering and native desktop launch require local testing. A successful engine/backend test run does not prove that the graphical client works.
- Match reporting is optional; without the Spring Boot backend, gameplay remains available.
- For future polish: add asset-licensed sprite sheets, hit feedback, depth sorting, smooth transitions, and resolution scaling.
