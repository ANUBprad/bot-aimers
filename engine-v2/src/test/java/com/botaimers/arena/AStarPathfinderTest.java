package com.botaimers.arena;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class AStarPathfinderTest {
    private final AStarPathfinder finder = new AStarPathfinder();

    @Test void openGridUsesShortestRoute() {
        List<GridPoint> path = finder.findPath(
                new GridMap(5, 5, Set.of()), new GridPoint(0, 0), new GridPoint(4, 4));
        assertEquals(9, path.size());
        assertEquals(new GridPoint(0, 0), path.get(0));
        assertEquals(new GridPoint(4, 4), path.get(path.size() - 1));
    }

    @Test void avoidsWalls() {
        GridPoint wall = new GridPoint(1, 0);
        List<GridPoint> path = finder.findPath(
                new GridMap(3, 3, Set.of(wall)), new GridPoint(0, 0), new GridPoint(2, 0));
        assertEquals(5, path.size());
        assertFalse(path.contains(wall));
    }

    @Test void returnsEmptyWhenGoalUnreachable() {
        List<GridPoint> path = finder.findPath(
                new GridMap(3, 3, Set.of(new GridPoint(1, 0), new GridPoint(0, 1))),
                new GridPoint(0, 0), new GridPoint(2, 2));
        assertTrue(path.isEmpty());
    }

    @Test void sameStartAndGoalIsSingleCell() {
        GridPoint p = new GridPoint(1, 1);
        assertEquals(List.of(p), finder.findPath(new GridMap(3, 3, Set.of()), p, p));
    }

    @Test void rejectsBlockedEndpoints() {
        GridMap map = new GridMap(2, 2, Set.of(new GridPoint(1, 1)));
        assertThrows(IllegalArgumentException.class,
                () -> finder.findPath(map, new GridPoint(0, 0), new GridPoint(1, 1)));
    }
}
