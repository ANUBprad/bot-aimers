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
        return fight(first, firstStart, TacticalPolicies.aggressive(), second, secondStart,
                TacticalPolicies.aggressive(), maxRounds);
    }

    public TacticalBattleResult fight(Bot first, GridPoint firstStart, TacticalPolicy firstPolicy,
                                       Bot second, GridPoint secondStart, TacticalPolicy secondPolicy,
                                       int maxRounds) {
        Objects.requireNonNull(firstPolicy, "firstPolicy");
        Objects.requireNonNull(secondPolicy, "secondPolicy");
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
            int eventsBefore = events.size();
            TacticalPolicy.Action firstAction = Objects.requireNonNull(
                    firstPolicy.choose(first, a, second, b), "first action");
            if (adjacent(a, b)) {
                if (firstAction == TacticalPolicy.Action.ATTACK) {
                    int damage = combat.attack(first, second);
                    events.add(first.name() + " attacks " + second.name() + " for " + damage);
                } else if (firstAction == TacticalPolicy.Action.GUARD) {
                    events.add(first.name() + " guards");
                }
            } else if (firstAction == TacticalPolicy.Action.ADVANCE) {
                GridPoint next = nextStep(a, b);
                if (!next.equals(a)) {
                    a = next;
                    events.add(first.name() + " moves to " + a);
                }
            }
            if (!second.alive()) break;
            TacticalPolicy.Action secondAction = Objects.requireNonNull(
                    secondPolicy.choose(second, b, first, a), "second action");
            if (adjacent(b, a)) {
                if (secondAction == TacticalPolicy.Action.ATTACK) {
                    int damage = firstAction == TacticalPolicy.Action.GUARD
                            ? guardedAttack(second, first) : combat.attack(second, first);
                    events.add(second.name() + " attacks " + first.name() + " for " + damage);
                } else if (secondAction == TacticalPolicy.Action.GUARD) {
                    events.add(second.name() + " guards");
                }
            } else if (secondAction == TacticalPolicy.Action.ADVANCE) {
                GridPoint next = nextStep(b, a);
                if (!next.equals(b)) {
                    b = next;
                    events.add(second.name() + " moves to " + b);
                }
            }
            if (events.size() == eventsBefore) break;
        }
        String winner = first.alive() == second.alive() ? null
                : (first.alive() ? first.name() : second.name());
        return new TacticalBattleResult(winner, rounds, a, b,
                first.health(), second.health(), events);
    }

    private static int guardedAttack(Bot attacker, Bot defender) {
        int damage = (int) Math.max(1L,
                (Math.max(1L, (long) attacker.attackPower() - defender.defense()) + 1) / 2);
        int applied = Math.min(damage, defender.health());
        defender.takeDamage(applied);
        return applied;
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
