package com.botaimers.engine;

/** Guards at low health, otherwise attacks. */
public final class DefensiveStrategy implements BotStrategy {
    private final int guardThreshold;
    public DefensiveStrategy(int guardThreshold) {
        if (guardThreshold < 1) throw new IllegalArgumentException("Threshold must be positive");
        this.guardThreshold = guardThreshold;
    }
    @Override public Action choose(Bot self, Bot opponent) {
        return self.health() <= guardThreshold ? Action.GUARD : Action.ATTACK;
    }
}
