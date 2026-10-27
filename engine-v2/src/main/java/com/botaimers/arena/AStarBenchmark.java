package com.botaimers.arena;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/**
 * Standalone, dependency-free A* microbenchmark. This is not JMH and should not
 * be interpreted as a statistically rigorous JVM benchmark.
 */
public final class AStarBenchmark {
    private static volatile int sink;

    private AStarBenchmark() {}

    public static void main(String[] args) throws IOException {
        int warmup = positive(args, 0, 1000);
        int iterations = positive(args, 1, 2000);
        Path output = Path.of(args.length > 2 ? args[2] : "reports/astar-baseline.csv");
        AStarPathfinder finder = new AStarPathfinder();
        List<String> rows = new ArrayList<>();
        rows.add("size,obstacle_percent,warmup,iterations,reachable,path_length,mean_us,p50_us,p95_us,p99_us");

        for (int size : new int[]{16, 32, 64}) {
            for (int density : new int[]{0, 15, 30}) {
                GridPoint start = new GridPoint(0, 0);
                GridPoint goal = new GridPoint(size - 1, size - 1);
                Set<GridPoint> walls = new HashSet<>();
                Random random = new Random(20261027L + size * 100L + density);
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        GridPoint point = new GridPoint(x, y);
                        if (!point.equals(start) && !point.equals(goal)
                                && random.nextInt(100) < density) walls.add(point);
                    }
                }
                GridMap map = new GridMap(size, size, walls);
                for (int i = 0; i < warmup; i++) {
                    sink = finder.findPath(map, start, goal).size();
                }
                long[] times = new long[iterations];
                int pathLength = 0;
                long total = 0;
                for (int i = 0; i < iterations; i++) {
                    long before = System.nanoTime();
                    List<GridPoint> path = finder.findPath(map, start, goal);
                    long elapsed = System.nanoTime() - before;
                    pathLength = path.size();
                    sink = pathLength;
                    times[i] = elapsed;
                    total += elapsed;
                }
                Arrays.sort(times);
                double mean = total / (double) iterations / 1000.0;
                String row = String.format(Locale.ROOT,
                        "%d,%d,%d,%d,%s,%d,%.3f,%.3f,%.3f,%.3f",
                        size, density, warmup, iterations, pathLength > 0,
                        pathLength, mean, percentile(times, 0.50) / 1000.0,
                        percentile(times, 0.95) / 1000.0,
                        percentile(times, 0.99) / 1000.0);
                rows.add(row);
                System.out.println(row);
            }
        }
        Path parent = output.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.write(output, rows);
        System.out.println("Saved benchmark: " + output.toAbsolutePath());
    }

    private static int positive(String[] args, int index, int fallback) {
        if (args.length <= index) return fallback;
        int value = Integer.parseInt(args[index]);
        if (value < 1) throw new IllegalArgumentException("Iterations must be positive");
        return value;
    }

    private static long percentile(long[] sorted, double quantile) {
        return sorted[(int) Math.ceil(quantile * sorted.length) - 1];
    }
}
