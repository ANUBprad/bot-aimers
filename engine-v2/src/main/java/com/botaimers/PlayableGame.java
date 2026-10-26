package com.botaimers;

import com.botaimers.arena.*;
import com.botaimers.engine.Bot;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/** Interactive console game. Run from a terminal with standard input. */
public final class PlayableGame {
    private PlayableGame() {}

    public static void main(String[] args) {
        GridMap map = new GridMap(9, 5, Set.of(
                new GridPoint(4, 0), new GridPoint(4, 1),
                new GridPoint(4, 3), new GridPoint(4, 4)));
        PlayerBattle battle = new PlayerBattle(map,
                new Bot("Player", 60, 12, 3), new GridPoint(1, 2),
                new Bot("CPU", 60, 10, 4), new GridPoint(7, 2));
        AsciiArenaRenderer renderer = new AsciiArenaRenderer();
        System.out.println("BOT-AIMERS | PLAYER VS CPU");
        System.out.println("W/A/S/D move | F attack | G guard | Q quit");
        try (Scanner scanner = new Scanner(System.in)) {
            while (!battle.finished()) {
                System.out.println();
                System.out.println(renderer.render(map, battle.playerPosition(), battle.enemyPosition()));
                System.out.printf("Turn %d | Your HP: %d | CPU HP: %d%n",
                        battle.turns() + 1, battle.playerHealth(), battle.enemyHealth());
                System.out.print("Action > ");
                if (!scanner.hasNextLine()) {
                    System.out.println("Input closed. Exiting.");
                    return;
                }
                String input = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
                if (input.equals("Q")) {
                    System.out.println("Game exited.");
                    return;
                }
                PlayerBattle.Command command = switch (input) {
                    case "W" -> PlayerBattle.Command.UP;
                    case "S" -> PlayerBattle.Command.DOWN;
                    case "A" -> PlayerBattle.Command.LEFT;
                    case "D" -> PlayerBattle.Command.RIGHT;
                    case "F" -> PlayerBattle.Command.ATTACK;
                    case "G" -> PlayerBattle.Command.GUARD;
                    default -> null;
                };
                if (command == null) {
                    System.out.println("Unknown command. Use W/A/S/D, F, G or Q.");
                    continue;
                }
                System.out.println(battle.takeTurn(command));
            }
        }
        System.out.println(renderer.render(map, battle.playerPosition(), battle.enemyPosition()));
        System.out.printf("Final HP: You %d | CPU %d%n", battle.playerHealth(), battle.enemyHealth());
        System.out.println("Winner: " + battle.winner());
    }
}
