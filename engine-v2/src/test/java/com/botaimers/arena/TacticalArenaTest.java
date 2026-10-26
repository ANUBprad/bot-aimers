package com.botaimers.arena;

import com.botaimers.engine.Bot;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TacticalArenaTest {
    @Test void botsNavigateAroundObstacleAndFight() {
        GridMap map = new GridMap(5, 3, Set.of(new GridPoint(2, 1)));
        TacticalBattleResult result = new TacticalArena(map).fight(
                new Bot("Alpha", 50, 12, 2), new GridPoint(0, 1),
                new Bot("Beta", 50, 7, 2), new GridPoint(4, 1), 30);
        assertEquals("Alpha", result.winner());
        assertTrue(result.events().stream().anyMatch(s -> s.contains("moves")));
        assertTrue(result.events().stream().anyMatch(s -> s.contains("attacks")));
        assertNotEquals(result.firstPosition(), result.secondPosition());
    }

    @Test void adjacentBotsFightWithoutMoving() {
        TacticalBattleResult result = new TacticalArena(new GridMap(2, 1, Set.of())).fight(
                new Bot("A", 10, 10, 0), new GridPoint(0, 0),
                new Bot("B", 10, 1, 0), new GridPoint(1, 0), 10);
        assertEquals("A", result.winner());
        assertEquals(1, result.rounds());
        assertEquals(new GridPoint(0, 0), result.firstPosition());
    }

    @Test void blockedPathEndsAtRoundLimitWithoutOverlap() {
        GridMap map = new GridMap(3, 1, Set.of(new GridPoint(1, 0)));
        TacticalBattleResult result = new TacticalArena(map).fight(
                new Bot("A", 10, 2, 0), new GridPoint(0, 0),
                new Bot("B", 10, 2, 0), new GridPoint(2, 0), 3);
        assertTrue(result.draw());
        assertEquals(3, result.rounds());
        assertEquals(new GridPoint(0, 0), result.firstPosition());
        assertEquals(new GridPoint(2, 0), result.secondPosition());
    }

    @Test void rejectsOverlappingSpawn() {
        TacticalArena arena = new TacticalArena(new GridMap(2, 2, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> arena.fight(
                new Bot("A", 10, 2, 0), new GridPoint(0, 0),
                new Bot("B", 10, 2, 0), new GridPoint(0, 0), 10));
    }
}
