package com.botaimers.arena;

import java.util.Objects;

/** Text-based arena view: # walls, . floor, A/B combatants. */
public final class AsciiArenaRenderer {
    public String render(GridMap map, GridPoint first, GridPoint second) {
        Objects.requireNonNull(map, "map");
        if (!map.walkable(first) || !map.walkable(second) || first.equals(second)) {
            throw new IllegalArgumentException("Bots must occupy distinct walkable cells");
        }
        StringBuilder view = new StringBuilder();
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                GridPoint cell = new GridPoint(x, y);
                view.append(cell.equals(first) ? 'A' : cell.equals(second) ? 'B'
                        : map.walkable(cell) ? '.' : '#');
            }
            if (y + 1 < map.height()) view.append(System.lineSeparator());
        }
        return view.toString();
    }
}
