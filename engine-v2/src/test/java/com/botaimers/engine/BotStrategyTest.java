package com.botaimers.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotStrategyTest {
    @Test void aggressiveAlwaysAttacks() {
        Bot a = new Bot("A", 10, 5, 1);
        Bot b = new Bot("B", 10, 5, 1);
        assertEquals(BotStrategy.Action.ATTACK, new AggressiveStrategy().choose(a, b));
    }
    @Test void defensiveGuardsBelowThreshold() {
        Bot a = new Bot("A", 10, 5, 1);
        Bot b = new Bot("B", 10, 5, 1);
        assertEquals(BotStrategy.Action.GUARD, new DefensiveStrategy(15).choose(a, b));
        assertEquals(BotStrategy.Action.ATTACK, new DefensiveStrategy(5).choose(a, b));
    }
    @Test void balancedAdaptsToOpponent() {
        Bot a = new Bot("A", 20, 5, 1);
        Bot b = new Bot("B", 50, 10, 1);
        assertEquals(BotStrategy.Action.GUARD, new BalancedStrategy().choose(a, b));
    }
}
