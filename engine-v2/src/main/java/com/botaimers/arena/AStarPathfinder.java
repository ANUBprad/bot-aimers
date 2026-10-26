package com.botaimers.arena;

import java.util.*;

/** Shortest-path A* on a four-neighbor, unit-cost grid. */
public final class AStarPathfinder {
    private record Candidate(GridPoint point, int g, int f) {}
    private static final int[][] STEPS = {{1,0},{0,1},{-1,0},{0,-1}};

    public List<GridPoint> findPath(GridMap map, GridPoint start, GridPoint goal) {
        Objects.requireNonNull(map, "map");
        if (!map.walkable(start) || !map.walkable(goal)) {
            throw new IllegalArgumentException("Start and goal must be walkable");
        }
        if (start.equals(goal)) return List.of(start);

        PriorityQueue<Candidate> frontier = new PriorityQueue<>(
                Comparator.comparingInt(Candidate::f).thenComparingInt(Candidate::g));
        Map<GridPoint, Integer> distance = new HashMap<>();
        Map<GridPoint, GridPoint> parent = new HashMap<>();
        distance.put(start, 0);
        frontier.add(new Candidate(start, 0, heuristic(start, goal)));

        while (!frontier.isEmpty()) {
            Candidate current = frontier.remove();
            if (current.g() != distance.getOrDefault(current.point(), Integer.MAX_VALUE)) continue;
            if (current.point().equals(goal)) return reconstruct(parent, goal);

            for (int[] step : STEPS) {
                int x = current.point().x() + step[0];
                int y = current.point().y() + step[1];
                if (x < 0 || y < 0 || x >= map.width() || y >= map.height()) continue;
                GridPoint next = new GridPoint(x, y);
                if (!map.walkable(next)) continue;
                int nextG = current.g() + 1;
                if (nextG < distance.getOrDefault(next, Integer.MAX_VALUE)) {
                    distance.put(next, nextG);
                    parent.put(next, current.point());
                    frontier.add(new Candidate(next, nextG, nextG + heuristic(next, goal)));
                }
            }
        }
        return List.of();
    }

    private static int heuristic(GridPoint a, GridPoint b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private static List<GridPoint> reconstruct(Map<GridPoint, GridPoint> parent, GridPoint goal) {
        LinkedList<GridPoint> path = new LinkedList<>();
        for (GridPoint current = goal; current != null; current = parent.get(current)) {
            path.addFirst(current);
        }
        return List.copyOf(path);
    }
}
