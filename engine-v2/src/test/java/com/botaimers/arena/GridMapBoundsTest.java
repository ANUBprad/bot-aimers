package com.botaimers.arena;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class GridMapBoundsTest {
    @Test void negativeCoordinatesAreRejectedAtConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new GridPoint(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new GridPoint(0, -1));
        assertThrows(IllegalArgumentException.class, () -> new GridPoint(-1, -1));
    }

    @Test void boundaryCellsAreValid() {
        GridMap map = new GridMap(3, 3, Set.of());
        assertTrue(map.inBounds(new GridPoint(0, 0)));
        assertTrue(map.inBounds(new GridPoint(2, 2)));
        assertFalse(map.inBounds(new GridPoint(3, 2)));
        assertFalse(map.inBounds(new GridPoint(2, 3)));
        assertFalse(map.inBounds(null));
        assertFalse(map.walkable(null));
    }

    @Test void rejectsWallsWithNegativeCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new GridPoint(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> new GridPoint(1, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new GridMap(3, 3, Set.of(new GridPoint(3, 1))));
    }
}
