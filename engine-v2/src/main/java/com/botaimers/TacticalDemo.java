package com.botaimers;

import com.botaimers.arena.*;
import com.botaimers.engine.Bot;
import java.util.Set;

/** Runnable console demonstration of obstacle-aware tactical AI battles. */
public final class TacticalDemo {
    private TacticalDemo() {}

    public static void main(String[] args) {
        GridMap map = new GridMap(9, 5, Set.of(
                new GridPoint(4, 0), new GridPoint(4, 1),
                new GridPoint(4, 3), new GridPoint(4, 4)));
        GridPoint alphaStart = new GridPoint(1, 2);
        GridPoint betaStart = new GridPoint(7, 2);
        AsciiArenaRenderer renderer = new AsciiArenaRenderer();

        System.out.println("BOT-AIMERS | TACTICAL ARENA");
        System.out.println("A = Alpha, B = Beta, # = wall");
        System.out.println(renderer.render(map, alphaStart, betaStart));
        System.out.println();

        TacticalBattleResult result = new TacticalArena(map).fight(
                new Bot("Alpha", 60, 12, 3), alphaStart, TacticalPolicies.aggressive(),
                new Bot("Beta", 60, 10, 4), betaStart, TacticalPolicies.cautious(15), 40);

        for (String event : result.events()) System.out.println("  " + event);
        System.out.println();
        System.out.println("FINAL ARENA");
        System.out.println(renderer.render(map, result.firstPosition(), result.secondPosition()));
        System.out.printf("Rounds: %d | Alpha HP: %d | Beta HP: %d%n",
                result.rounds(), result.firstHealth(), result.secondHealth());
        System.out.println(result.draw() ? "Draw" : "Winner: " + result.winner());
    }
}
