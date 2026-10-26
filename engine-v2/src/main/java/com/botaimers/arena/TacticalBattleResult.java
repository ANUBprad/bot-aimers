package com.botaimers.arena;

import java.util.List;

/** Final state and turn-by-turn log of a grid battle. */
public record TacticalBattleResult(String winner, int rounds, GridPoint firstPosition,
                                   GridPoint secondPosition, int firstHealth, int secondHealth,
                                   List<String> events) {
    public TacticalBattleResult {
        events = List.copyOf(events);
    }
    public boolean draw() { return winner == null; }
}
