package com.botaimers.arena;

import com.botaimers.engine.Bot;
import com.botaimers.engine.CombatEngine;
import java.util.List;
import java.util.Objects;

/** One player-controlled bot versus an advancing melee opponent. */
public final class PlayerBattle {
    public enum Command { UP, DOWN, LEFT, RIGHT, ATTACK, GUARD }
    private final GridMap map;
    private final Bot player;
    private final Bot enemy;
    private final AStarPathfinder pathfinder = new AStarPathfinder();
    private final CombatEngine combat = new CombatEngine();
    private GridPoint playerPosition;
    private GridPoint enemyPosition;
    private int turns;

    public PlayerBattle(GridMap map, Bot player, GridPoint playerStart,
                        Bot enemy, GridPoint enemyStart) {
        this.map = Objects.requireNonNull(map, "map");
        this.player = Objects.requireNonNull(player, "player");
        this.enemy = Objects.requireNonNull(enemy, "enemy");
        if (player == enemy || !player.alive() || !enemy.alive()
                || !map.walkable(playerStart) || !map.walkable(enemyStart)
                || playerStart.equals(enemyStart)) {
            throw new IllegalArgumentException("Invalid player battle setup");
        }
        playerPosition = playerStart;
        enemyPosition = enemyStart;
    }

    public String takeTurn(Command command) {
        Objects.requireNonNull(command, "command");
        if (finished()) throw new IllegalStateException("Battle already finished");
        StringBuilder log = new StringBuilder();
        boolean guarding = command == Command.GUARD;
        switch (command) {
            case UP -> movePlayer(0, -1, log);
            case DOWN -> movePlayer(0, 1, log);
            case LEFT -> movePlayer(-1, 0, log);
            case RIGHT -> movePlayer(1, 0, log);
            case ATTACK -> {
                if (adjacent(playerPosition, enemyPosition)) {
                    log.append("You hit for ").append(combat.attack(player, enemy)).append(" damage. ");
                } else log.append("Enemy is out of melee range. ");
            }
            case GUARD -> log.append("You brace for the next attack. ");
        }
        turns++;
        if (enemy.alive()) {
            if (adjacent(enemyPosition, playerPosition)) {
                long base = Math.max(1L, (long) enemy.attackPower() - player.defense());
                int damage = guarding ? (int) Math.min(player.health(), Math.max(1L, (base + 1) / 2))
                        : combat.attack(enemy, player);
                if (guarding) player.takeDamage(damage);
                log.append("Enemy hits for ").append(damage).append(" damage.");
            } else {
                GridPoint step = nextEnemyStep();
                if (!step.equals(enemyPosition)) {
                    enemyPosition = step;
                    log.append("Enemy advances.");
                } else log.append("Enemy cannot reach you.");
            }
        }
        return log.toString().trim();
    }

    private void movePlayer(int dx, int dy, StringBuilder log) {
        int x = playerPosition.x() + dx;
        int y = playerPosition.y() + dy;
        if (x < 0 || y < 0 || x >= map.width() || y >= map.height()) {
            log.append("Arena boundary. ");
            return;
        }
        GridPoint target = new GridPoint(x, y);
        if (!map.walkable(target) || target.equals(enemyPosition)) {
            log.append("Tile blocked. ");
        } else {
            playerPosition = target;
            log.append("You move to ").append(target).append(". ");
        }
    }

    private GridPoint nextEnemyStep() {
        List<GridPoint> best = List.of();
        int[][] steps = {{1,0},{0,1},{-1,0},{0,-1}};
        for (int[] step : steps) {
            int x = playerPosition.x() + step[0];
            int y = playerPosition.y() + step[1];
            if (x < 0 || y < 0 || x >= map.width() || y >= map.height()) continue;
            GridPoint destination = new GridPoint(x, y);
            if (!map.walkable(destination)) continue;
            List<GridPoint> path = pathfinder.findPath(map, enemyPosition, destination);
            if (!path.isEmpty() && (best.isEmpty() || path.size() < best.size())) best = path;
        }
        return best.size() > 1 ? best.get(1) : enemyPosition;
    }

    private static boolean adjacent(GridPoint a, GridPoint b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y()) == 1;
    }

    public boolean finished() { return !player.alive() || !enemy.alive(); }
    public String winner() {
        if (!finished()) return null;
        return player.alive() ? player.name() : enemy.name();
    }
    public int turns() { return turns; }
    public int playerHealth() { return player.health(); }
    public int enemyHealth() { return enemy.health(); }
    public GridPoint playerPosition() { return playerPosition; }
    public GridPoint enemyPosition() { return enemyPosition; }
    public GridMap map() { return map; }
}
