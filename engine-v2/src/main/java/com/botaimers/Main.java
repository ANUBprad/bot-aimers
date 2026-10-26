package com.botaimers;

import com.botaimers.engine.*;

/** Command-line demonstration of the independent Bot-Aimers combat engine. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        Bot alpha = new Bot("Alpha", 100, 18, 5);
        Bot beta = new Bot("Beta", 100, 15, 8);

        BattleResult result = new TurnManager().fight(
                alpha, beta, new AggressiveStrategy(), new BalancedStrategy(), 100);

        System.out.println("Bot-Aimers v0.1 | Tactical Duel");
        System.out.println("Rounds: " + result.rounds());
        System.out.println("Alpha HP: " + result.firstHealth());
        System.out.println("Beta HP: " + result.secondHealth());
        System.out.println(result.draw() ? "Draw" : "Winner: " + result.winner());
    }
}
