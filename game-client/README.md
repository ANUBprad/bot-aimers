# Bot-Aimers 2D desktop client

LibGDX-based graphical desktop game that reuses the Java 21 `engine-v2` battle logic. No art assets are required for this first prototype.

## Windows setup

Install the shared engine first, from the repository root:

```powershell
mvn -f engine-v2/pom.xml clean install
mvn -f game-client/pom.xml compile
mvn -f game-client/pom.xml exec:java
```

A 960×640 game window should open. Spring Boot is **not required** for local gameplay yet.

## Controls

- `W/A/S/D` or arrow keys: move one tile
- `F`: attack an adjacent opponent
- `G`: guard to reduce incoming damage
- `R`: restart

The opponent pursues the player using the shared A* pathfinding engine. Invalid movement consumes a turn; the CPU still acts.

## Architecture and roadmap

- `engine-v2`: reusable combat, grid, A* navigation, and turn logic
- `game-client`: LibGDX rendering, keyboard controls, health bars, and game-over UI
- `game-server`: Spring Boot match simulation and history API

The graphical client currently plays **offline**. Planned follow-ups include smooth movement animation, audio, menus, server-backed match recording, persistent player profiles, and multiplayer.
