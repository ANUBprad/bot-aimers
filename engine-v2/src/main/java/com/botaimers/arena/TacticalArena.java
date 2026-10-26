package com.botaimers.arena;

import com.botaimers.engine.*;
import java.util.*;

/** Two-bot arena with orthogonal movement, obstacles, and adjacent attacks. */
public final class TacticalArena {
    private final GridMap map;
    private final AStarPathfinder pathfinder = new AStarPathfinder();
    private final CombatEngine combat = new CombatEngine();

    public TacticalArena(GridMap map) {
        this.map = Objects.requireNonNull(map, "map");
    }

    public TacticalBattleResult fight(Bot first, GridPoint firstStart, Bot second,
                                       GridPoint secondStart, int maxRounds) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        if (first == second || !first.alive() || !second.alive() || maxRounds < 1
                || !map.walkable(firstStart) || !map.walkable(secondStart)
                || firstStart.equals(secondStart)) {
            throw new IllegalArgumentException("Invalid tactical battle setup");
        }
        GridPoint a = firstStart;
        GridPoint b = secondStart;
        int rounds = 0;
        List<String> events = new ArrayList<>();
        while (rounds < maxRounds && first.alive() && second.alive()) {
            rounds++;
            if (adjacent(a, b)) {
                int damage = combat.attack(first, second);
                events.add(first.name() + " attacks " + second.name() + " for " + damage);
            } else {
                GridPoint next = nextStep(a, b);
                if (!next.equals(a)) {
                    a = next;
                    events.add(first.name() + " moves to " + a);
                }
            }
            if (!second.alive()) break;
            if (adjacent(b, a)) {
                int damage = combat.attack(second, first);
                events.add(second.name() + " attacks " + first.name() + " for " + damage);
            } else {
                GridPoint next = nextStep(b, a);
                if (!next.equals(b)) {
                    b = next;
                    events.add(second.name() + " moves to " + b);
                }
            }
            if (!adjacent(a, b) && events.isEmpty()) break;
        }
        String winner = first.alive() == second.alive() ? null
                : (first.alive() ? first.name() : second.name());
        return new TacticalBattleResult(winner, rounds, a, b,
                first.health(), second.health(), events);
    }

    private GridPoint nextStep(GridPoint from, GridPoint opponent) {
        // Opponent occupies its tile: navigate toward an unoccupied adjacent tile.
        List<GridPoint> best = List.of();
        int[][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
        for (int[] d : directions) {
            int x = opponent.x() + d[0];
            int y = opponent.y() + d[1];
            if (x < 0 || y < 0 || x >= map.width() || y >= map.height()) continue;
            GridPoint destination = new GridPoint(x, y);
            if (!map.walkable(destination)) continue;
            List<GridPoint> candidate = pathfinder.findPath(map, from, destination);
            if (!candidate.isEmpty() && (best.isEmpty() || candidate.size() < best.size())) {
                best = candidate;
            }
        }
        return best.size() > 1 ? best.get(1) : from;
    }

    private static boolean adjacent(GridPoint a, GridPoint b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y()) == 1;
    }
}
