package com.botaimers.arena;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ArenaGeneratorTest {
    @Test void generatedArenasArePlayableAcrossManySeeds() {
        AStarPathfinder finder = new AStarPathfinder();
        for (long seed = 0; seed < 200; seed++) {
            ArenaGenerator.Arena arena = ArenaGenerator.generate(seed);
            assertTrue(arena.map().walkable(arena.playerSpawn()));
            assertTrue(arena.map().walkable(arena.enemySpawn()));
            assertFalse(finder.findPath(arena.map(), arena.playerSpawn(),
                    arena.enemySpawn()).isEmpty(), "seed " + seed);
        }
    }

    @Test void generationIsDeterministic() {
        ArenaGenerator.Arena first = ArenaGenerator.generate(12345);
        ArenaGenerator.Arena second = ArenaGenerator.generate(12345);
        assertEquals(first.theme(), second.theme());
        assertEquals(first.map().width(), second.map().width());
        assertEquals(first.map().height(), second.map().height());
        assertEquals(first.map().walls(), second.map().walls());
        assertEquals(first.playerSpawn(), second.playerSpawn());
        assertEquals(first.enemySpawn(), second.enemySpawn());
    }
}
