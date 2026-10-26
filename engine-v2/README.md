# Bot-Aimers — Tactical Engine (v0.1)

A standalone Java 21 tactical-combat prototype with deterministic AI policies, A* grid navigation, and a text-rendered arena. This module is independent of the legacy root Maven project.

## Requirements

- JDK 21 or newer
- Maven 3.9+

## Build and test

From `engine-v2`:

```powershell
mvn clean verify
```

## Run the tactical arena

```powershell
java -cp target/classes com.botaimers.TacticalDemo
```

The arena displays `A` and `B` for bots, `#` for obstacles, and `.` for open tiles. The demonstration then prints movement and attack events, the final arena, health totals, and the winner.

For the earlier non-spatial combat demo:

```powershell
java -cp target/classes com.botaimers.Main
```

## Architecture

- `engine`: bot attributes, deterministic damage, turn-based combat, and basic AI strategies.
- `arena`: grid coordinates, obstacle map, A* shortest-path navigation, spatial AI policies, tactical combat, and ASCII rendering.
- `TacticalDemo`: executable console demonstration.

## Current limitations

This is an automated AI-versus-AI simulation, **not yet a player-controlled graphical game**. Combat uses sequential turns and adjacent melee attacks. The event log records text actions rather than structured replay frames. The next milestones are interactive input, frame-by-frame replay, and a graphical client.
