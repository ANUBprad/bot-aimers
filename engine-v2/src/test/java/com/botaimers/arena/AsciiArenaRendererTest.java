package com.botaimers.arena;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class AsciiArenaRendererTest {
    private final AsciiArenaRenderer renderer = new AsciiArenaRenderer();

    @Test void drawsBotsWallsAndFloor() {
        GridMap map = new GridMap(3, 2, Set.of(new GridPoint(1, 0)));
        assertEquals("A#." + System.lineSeparator() + "..B",
                renderer.render(map, new GridPoint(0, 0), new GridPoint(2, 1)));
    }

    @Test void rejectsOverlappingBots() {
        GridMap map = new GridMap(2, 1, Set.of());
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(map, new GridPoint(0, 0), new GridPoint(0, 0)));
    }

    @Test void rejectsWallSpawn() {
        GridMap map = new GridMap(3, 1, Set.of(new GridPoint(1, 0)));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(map, new GridPoint(1, 0), new GridPoint(2, 0)));
    }
}
