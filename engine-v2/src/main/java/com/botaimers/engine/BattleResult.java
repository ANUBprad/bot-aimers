package com.botaimers.engine;

/** Immutable summary of a completed battle. */
public record BattleResult(String winner, int rounds, int firstHealth, int secondHealth, boolean draw) {
    public BattleResult {
        if (rounds < 0 || firstHealth < 0 || secondHealth < 0) {
            throw new IllegalArgumentException("Invalid battle result");
        }
    }
}
