package com.botaimers.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CombatEngineTest {
    private final CombatEngine engine = new CombatEngine();

    @Test void damageAccountsForDefense() {
        Bot a = new Bot("Alpha", 100, 18, 5);
        Bot b = new Bot("Beta", 100, 15, 8);
        assertEquals(10, engine.attack(a, b));
        assertEquals(90, b.health());
    }

    @Test void minimumDamageIsOne() {
        Bot a = new Bot("A", 10, 0, 0);
        Bot b = new Bot("B", 10, 0, 100);
        assertEquals(1, engine.attack(a, b));
    }

    @Test void damageCannotExceedRemainingHealth() {
        Bot a = new Bot("A", 10, 50, 0);
        Bot b = new Bot("B", 4, 1, 0);
        assertEquals(4, engine.attack(a, b));
        assertFalse(b.alive());
        assertThrows(IllegalStateException.class, () -> engine.attack(a, b));
    }

    @Test void rejectsInvalidCombatants() {
        assertThrows(IllegalArgumentException.class, () -> new Bot(" ", 10, 2, 1));
        Bot a = new Bot("A", 10, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> engine.attack(a, a));
    }
}
