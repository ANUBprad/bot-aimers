# A* pathfinding benchmark

This is a **simple JVM microbenchmark**, not JMH. It measures the existing `AStarPathfinder.findPath` implementation without changing gameplay logic.

## Run on Windows PowerShell

From the repository root:

```powershell
mvn -f engine-v2/pom.xml clean install
java -cp engine-v2/target/classes com.botaimers.arena.AStarBenchmark 1000 2000 reports/astar-baseline.csv
```

Arguments: warm-up iterations per scenario, measured iterations per scenario, and output CSV path. Defaults: 1000, 2000, and `reports/astar-baseline.csv`. Both iteration counts must be positive.

The benchmark tests 16x16, 32x32 and 64x64 grids at 0%, 15%, and 30% **probabilistic obstacle placement**. Layouts are deterministic for a given size and density, using a fixed random seed. The reported percentage is the requested sampling probability, not necessarily the exact final wall fraction. Start and goal are always walkable. Some randomly generated maps may be unreachable; the CSV includes `reachable` and `path_length` to make that visible.

CSV columns: `size, obstacle_percent, warmup, iterations, reachable, path_length, mean_us, p50_us, p95_us, p99_us`. Each measurement is one invocation of `findPath` on a fixed grid; percentiles are nearest-rank statistics.

## Interpretation

- Run at least three separate JVM processes under similar system conditions before comparing results.
- The program prints each scenario and writes a CSV. No measurements are prefilled or claimed.
- Warm-up reduces some JVM startup/JIT effects, but does not eliminate GC, scheduling, thermal, or CPU-frequency noise.
- This benchmark does not measure LibGDX frame time, rendering, or real player input latency.
- Fixed-grid repeated calls may benefit from caches and differ from gameplay with changing start/goal positions.
- Use JMH for reliable optimization claims or close comparisons; do not describe this script as a scientific benchmark.
- Keep raw CSV results with JVM version, OS, CPU, and git commit when reporting performance.

The baseline is intended to identify broad performance patterns and guide future profiling, not enforce fragile CI timing thresholds.
