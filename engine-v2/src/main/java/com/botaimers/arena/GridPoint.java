package com.botaimers.arena;

/** Immutable grid coordinate. */
public record GridPoint(int x, int y) {
    public GridPoint {
        if (x < 0 || y < 0) throw new IllegalArgumentException("Coordinates must be nonnegative");
    }
}
