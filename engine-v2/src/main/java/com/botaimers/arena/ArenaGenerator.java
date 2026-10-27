package com.botaimers.arena;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** Seeded arena layouts with guaranteed reachable opposing spawn points. */
public final class ArenaGenerator {
    public record Arena(GridMap map, GridPoint playerSpawn, GridPoint enemySpawn,
                        long seed, String theme) {}

    private ArenaGenerator() {}

    public static Arena generate(long seed) {
        Random rng = new Random(seed);
        int width = 9 + rng.nextInt(4);
        int height = 6 + rng.nextInt(3);
        GridPoint player = new GridPoint(1, height / 2);
        GridPoint enemy = new GridPoint(width - 2, height / 2);
        String[] themes = {"OUTPOST", "REACTOR", "RUINS", "DEPOT"};
        String theme = themes[rng.nextInt(themes.length)];
        for (int attempt = 0; attempt < 200; attempt++) {
            Set<GridPoint> walls = new HashSet<>();
            int density = 12 + rng.nextInt(16);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    GridPoint p = new GridPoint(x, y);
                    if (!p.equals(player) && !p.equals(enemy) && rng.nextInt(100) < density)
                        walls.add(p);
                }
            }
            GridMap map = new GridMap(width, height, walls);
            if (!new AStarPathfinder().findPath(map, player, enemy).isEmpty())
                return new Arena(map, player, enemy, seed, theme);
        }
        // Deterministic fallback ensures a playable mission even with unlucky layouts.
        return new Arena(new GridMap(width, height, Set.of()), player, enemy, seed, theme);
    }
}
