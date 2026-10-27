package com.botaimers.arena;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Rectangular four-directional arena with impassable cells. */
public final class GridMap {
    private final int width;
    private final int height;
    private final Set<GridPoint> walls;

    public GridMap(int width, int height, Set<GridPoint> walls) {
        if (width < 1 || height < 1) throw new IllegalArgumentException("Grid dimensions must be positive");
        Objects.requireNonNull(walls, "walls");
        this.width = width;
        this.height = height;
        this.walls = Set.copyOf(new HashSet<>(walls));
        if (this.walls.stream().anyMatch(p -> !inBounds(p))) {
            throw new IllegalArgumentException("Walls must be inside the grid");
        }
    }

    public int width() { return width; }
    public int height() { return height; }
    public boolean inBounds(GridPoint point) {
        return point != null && point.x() >= 0 && point.y() >= 0 && point.x() < width && point.y() < height;
    }
    public boolean walkable(GridPoint point) {
        return inBounds(point) && !walls.contains(point);
    }
    public Set<GridPoint> walls() { return walls; }
}
