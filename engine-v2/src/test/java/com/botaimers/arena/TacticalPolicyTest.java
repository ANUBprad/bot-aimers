package com.botaimers.arena;

import com.botaimers.engine.Bot;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TacticalPolicyTest {
    @Test void aggressiveAdvancesUntilAdjacent() {
        Bot self = new Bot("A", 20, 8, 0);
        Bot enemy = new Bot("B", 20, 8, 0);
        TacticalPolicy policy = TacticalPolicies.aggressive();
        assertEquals(TacticalPolicy.Action.ADVANCE,
                policy.choose(self, new GridPoint(0, 0), enemy, new GridPoint(2, 0)));
        assertEquals(TacticalPolicy.Action.ATTACK,
                policy.choose(self, new GridPoint(0, 0), enemy, new GridPoint(1, 0)));
    }

    @Test void cautiousGuardsWhenLowOnHealthAndAdjacent() {
        TacticalPolicy policy = TacticalPolicies.cautious(20);
        assertEquals(TacticalPolicy.Action.GUARD, policy.choose(
                new Bot("A", 15, 10, 0), new GridPoint(0, 0),
                new Bot("B", 15, 10, 0), new GridPoint(1, 0)));
    }

    @Test void guardReducesIncomingDamageInArena() {
        TacticalBattleResult result = new TacticalArena(new GridMap(2, 1, Set.of())).fight(
                new Bot("A", 20, 10, 0), new GridPoint(0, 0),
                TacticalPolicies.cautious(20),
                new Bot("B", 20, 10, 0), new GridPoint(1, 0),
                TacticalPolicies.aggressive(), 1);
        assertEquals(15, result.firstHealth());
        assertEquals(20, result.secondHealth());
        assertTrue(result.events().stream().anyMatch(s -> s.contains("guards")));
    }

    @Test void defaultFightRemainsCompatible() {
        TacticalBattleResult result = new TacticalArena(new GridMap(2, 1, Set.of())).fight(
                new Bot("A", 10, 10, 0), new GridPoint(0, 0),
                new Bot("B", 10, 1, 0), new GridPoint(1, 0), 5);
        assertEquals("A", result.winner());
    }
}
