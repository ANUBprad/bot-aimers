package com.botaimers.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {
    private final TurnManager manager = new TurnManager();

    @Test void aggressiveDuelHasDeterministicWinner() {
        BattleResult result = manager.fight(
                new Bot("Alpha", 30, 12, 2),
                new Bot("Beta", 30, 8, 2),
                new AggressiveStrategy(), new AggressiveStrategy(), 20);
        assertEquals("Alpha", result.winner());
        assertEquals(3, result.rounds());
        assertFalse(result.draw());
    }

    @Test void guardingMitigatesDamage() {
        BattleResult result = manager.fight(
                new Bot("Alpha", 20, 10, 0),
                new Bot("Beta", 20, 10, 0),
                new AggressiveStrategy(), new DefensiveStrategy(20), 1);
        assertEquals(15, result.secondHealth());
        assertEquals(20, result.firstHealth());
        assertTrue(result.draw());
    }

    @Test void roundLimitStopsPassiveBots() {
        BotStrategy guard = (self, opponent) -> BotStrategy.Action.GUARD;
        BattleResult result = manager.fight(
                new Bot("A", 10, 2, 1), new Bot("B", 10, 2, 1),
                guard, guard, 4);
        assertTrue(result.draw());
        assertEquals(4, result.rounds());
    }

    @Test void rejectsInvalidRoundLimit() {
        Bot a = new Bot("A", 10, 2, 1);
        Bot b = new Bot("B", 10, 2, 1);
        assertThrows(IllegalArgumentException.class,
                () -> manager.fight(a, b, new AggressiveStrategy(),
                        new AggressiveStrategy(), 0));
    }
}
