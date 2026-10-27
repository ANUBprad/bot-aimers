package com.botaimers.arena;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class GridMapBoundsTest {
    @Test void negativeCoordinatesAreOutsideGrid() {
        GridMap map = new GridMap(3, 3, Set.of());
        assertFalse(map.inBounds(new GridPoint(-1, 0)));
        assertFalse(map.inBounds(new GridPoint(0, -1)));
        assertFalse(map.walkable(new GridPoint(-1, -1)));
    }

    @Test void boundaryCellsAreValid() {
        GridMap map = new GridMap(3, 3, Set.of());
        assertTrue(map.inBounds(new GridPoint(0, 0)));
        assertTrue(map.inBounds(new GridPoint(2, 2)));
        assertFalse(map.inBounds(new GridPoint(3, 2)));
        assertFalse(map.inBounds(new GridPoint(2, 3)));
    }

    @Test void rejectsWallsWithNegativeCoordinates() {
        assertThrows(IllegalArgumentException.class,
                () -> new GridMap(3, 3, Set.of(new GridPoint(-1, 1))));
        assertThrows(IllegalArgumentException.class,
                () -> new GridMap(3, 3, Set.of(new GridPoint(1, -1))));
    }
}
